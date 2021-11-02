package de.lancom.genesis.version

import de.lancom.genesis.version.Util.createGitRepo
import org.eclipse.jgit.lib.Constants
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import org.gradle.util.internal.VersionNumber

open class GenesisVersionExtension(
    private val project: Project
) {

    private var applied = false

    val tagPrefix = project.objects.property(String::class.java)
    val type = project.objects.property(VersionType::class.java)
    val hotfixDigits = project.objects.property(Int::class.javaObjectType)
    val checkBranch = project.objects.property(String::class.java)

    init {
        project.tasks.create("printVersion").doFirst {
            println(project.version)
        }

        project.tasks.create("tagVersion").doFirst {
            val tagPrefixProperty = project.findProperty("versionTagPrefix")?.toString()
            val tagPrefixValue = tagPrefixProperty ?: tagPrefix.orNull ?: "VERSION-"
            createGitRepo(project.rootDir).tag().run {
                name = "$tagPrefixValue${project.version}"
                call()
            }
        }

        project.tasks.create("checkBranchVersion").doFirst {
            val checkBranchProperty = project.findProperty("versionCheckBranch")?.toString()
            val checkBranchValue = checkBranchProperty ?: checkBranch.orNull ?: "master"
            Util.checkDiffVersion(checkBranchValue, createGitRepo(project.rootDir), project)
        }

        project.tasks.create("checkPublishedVersion").doFirst {
            Util.checkPublishedVersion(project)
        }

        project.afterEvaluate {
            if (!applied) {
                apply()
            }
        }
    }

    fun apply() {
        applied = true
        val versionOverrideProperty = project.findProperty("versionOverride")?.toString()
        if (versionOverrideProperty != null) {
            project.version = versionOverrideProperty
            return
        }

        val versionTypeProperty = project.findProperty("versionType")?.let {
            VersionType.valueOf(it.toString().uppercase())
        }

        val versionTypeValue = versionTypeProperty ?: type.orNull ?: VersionType.DEFAULT

        val versionBaseProperty = project.findProperty("versionBase")?.toString()
        val versionValue = VersionNumber.parse(versionBaseProperty ?: project.version.toString())

        val versionMajorProperty = project.findProperty("versionMajor")?.toString()?.toInt()
        val versionMinorProperty = project.findProperty("versionMinor")?.toString()?.toInt()
        val versionPatchProperty = project.findProperty("versionPatch")?.toString()?.toInt()
        val versionQualifierProperty = project.findProperty("versionQualifier")?.toString()

        val major = versionMajorProperty ?: versionValue.major
        val minor = versionMinorProperty ?: versionValue.minor
        val patch = versionPatchProperty ?: versionValue.micro
        var qualifier = versionQualifierProperty ?: versionValue.qualifier
        var hotfix = 0

        val hotfixDigitsProperty = project.findProperty("versionHotfixDigits")?.toString()?.toInt()
        val hotfixDigitsValue = hotfixDigitsProperty ?: hotfixDigits.orNull ?: 0

        val hotfixSize = Math.pow(10.0, hotfixDigitsValue.toDouble()).toInt()

        if (hotfixSize > 1 && patch < hotfixSize) {
            throw GradleException("Version number $versionValue does not reserve $hotfixDigitsValue digits in patch for hotfixes")
        }

        when (versionTypeValue) {
            VersionType.SNAPSHOT -> {
                qualifier = "SNAPSHOT"
            }
            VersionType.RELEASE -> {
                qualifier = null
            }
            VersionType.BUILD -> {
                val userName = System.getProperty("user.name")

                val git = createGitRepo(project.rootDir)

                val sha = git.repository.findRef(Constants.HEAD).objectId.abbreviate(7).name()
                qualifier = "$userName-$sha"
            }
            VersionType.HOTFIX -> {
                qualifier = null
                hotfix = 1

                project.extensions.findByType(PublishingExtension::class.java)?.also {
                    it.repositories.forEach { repository ->
                        if (repository is MavenArtifactRepository) {

                            Util.listRevisions(
                                repository.url.toString(),
                                project.group.toString(),
                                project.name.toString()
                            ).forEach {
                                try {
                                    VersionNumber.parse(it).also {
                                        var match = true
                                        match = match && it.major == major
                                        match = match && it.minor == minor
                                        match = match && it.micro / hotfixSize == patch / hotfixSize
                                        match = match && it.qualifier == qualifier

                                        if (match) {
                                            hotfix = maxOf(it.micro % hotfixSize + 1, hotfix)
                                        }
                                    }
                                } catch (_: Exception) {
                                    project.logger.warn("Artifact version $it cannot be parsed.")
                                }
                            }
                        }
                    }
                }
            }
            else -> Unit
        }

        if (hotfix >= hotfixSize) throw GradleException(
            "Version hotfix number $hotfix has more than $hotfixDigitsValue digits"
        )

        project.version = VersionNumber(major, minor, patch + hotfix, qualifier).toString()
    }

    fun withHotfixDigits(digits: Int) {
        this.hotfixDigits.set(digits)
    }

    fun withType(type: String) {
        this.type.set(VersionType.valueOf(type.uppercase()))
    }

    fun withType(type: VersionType) {
        this.type.set(type)
    }

    fun withTagPrefix(prefix: String) {
        this.tagPrefix.set(prefix)
    }

    fun withBranch(branch: String) {
        this.checkBranch.set(branch)
    }

}
