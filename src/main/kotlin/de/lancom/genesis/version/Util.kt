package de.lancom.genesis.version

import org.apache.ivy.core.search.ModuleEntry
import org.apache.ivy.core.search.OrganisationEntry
import org.apache.ivy.core.settings.IvySettings
import org.apache.ivy.plugins.resolver.IBiblioResolver
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.treewalk.filter.PathFilterGroup
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import org.gradle.util.VersionNumber
import java.io.File

object Util {
    fun listRevisions(
        url: String,
        group: String,
        name: String
    ) = IBiblioResolver().apply {
        isM2compatible = true
        settings = IvySettings()
        root = url
    }.let {
        it.listRevisions(ModuleEntry(OrganisationEntry(it, group), name)).map { it.revision }
    }

    fun createGitRepo(path: File) = Git(
        FileRepositoryBuilder()
            .setMustExist(true)
            .setWorkTree(path)
            .findGitDir(path)
            .build()
    )

    fun checkDiffVersion(branch: String, git: Git, project: Project) {
        val currentVersion = VersionNumber.parse(project.version.toString())
        val versionDiffRegex = "^version\\s*[=:]?\\s*([\"'])(.+)\\1$".toRegex(RegexOption.MULTILINE)

        val fileContent = getFileContentForBranch(git, branch, "build.gradle", "build.gradle.kts")

        val branchVersion = fileContent?.let {
            versionDiffRegex.find(it)
        }?.groupValues?.get(2)?.let {
            VersionNumber.parse(it)
        } ?: throw GradleException("Unable to determine branch version from diff. Missing or too many changes?")

        project.logger.lifecycle("Checking project version $currentVersion against branch version $branchVersion")
        if (currentVersion <= branchVersion) {
            throw GradleException("Build version $currentVersion is not newer than master version $branchVersion")
        }
    }

    fun checkRepositoryVersion(project: Project) {
        val projectVersion = VersionNumber.parse(project.version.toString())
        val repositoryUrls = project.extensions.getByType(PublishingExtension::class.java).repositories.mapNotNull {
            (it as? MavenArtifactRepository)?.url?.toString()
        }

        val repositoryVersion = repositoryUrls.flatMap {
            listRevisions(it, project.group.toString(), project.name).map { VersionNumber.parse(it) }
        }.maxOrNull()

        if (repositoryVersion != null && repositoryVersion >= projectVersion) {
            throw GradleException("Project version $projectVersion is not newer than repository $repositoryVersion!")
        }

    }

    private fun getFileContentForBranch(git: Git, branch: String, vararg matchingFileNames: String): String? {
        val head = git.repository.findRef(branch)
        val walk = RevWalk(git.repository)
        val commit = walk.parseCommit(head.getObjectId())
        val treeWalker = TreeWalk(git.repository)
        treeWalker.addTree(commit.tree)
        treeWalker.isRecursive = true
        treeWalker.filter = PathFilterGroup.createFromStrings(*matchingFileNames);
        treeWalker.next() || throw GradleException("Could not find build.gradle file.")
        return git.repository.open(treeWalker.getObjectId(0)).bytes.decodeToString()
    }

}
