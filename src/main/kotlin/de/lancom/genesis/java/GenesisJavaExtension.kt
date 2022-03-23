package de.lancom.genesis.java

import com.github.spotbugs.snom.SpotBugsExtension
import com.github.spotbugs.snom.SpotBugsPlugin
import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.*
import org.gradle.api.tasks.compile.JavaCompile
import java.io.File

private fun File.existsOrNull(): File? {
    return if (exists()) {
        this
    } else {
        null
    }
}

open class GenesisJavaExtension(
    private val project: Project
) {
    private val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)

    fun enableDefaults() {
        if (project.findProperty("checkstyle.enabled") != "false") {
            withCheckstyle()
        }
        if (project.findProperty("pmd.enabled") != "false") {
            withPmd()
        }
        if (project.findProperty("spotbugs.enabled") != "false") {
            withSpotBugs("4.2.0")
        }
        disableChecksForGeneratedSources()
    }

    fun disableChecksForGeneratedSources() {
        listOf(
            "checkstyleGeneratedClientPublic",
            "pmdGeneratedClientPublic",
            "spotbugsGeneratedClientPublic",
        ).mapNotNull(project.tasks::findByName).forEach { task ->
            task.enabled = false
        }

        listOf(
            "checkstyleGeneratedClientInternal",
            "pmdGeneratedClientInternal",
            "spotbugsGeneratedClientInternal",
        ).mapNotNull(project.tasks::findByName).forEach { task ->
            task.enabled = false
        }
    }

    fun enableJava11() {
        project.tasks.withType(JavaCompile::class.java) {
            it.apply {
                if (!name.startsWith("compileGeneratedClient")) {
                    sourceCompatibility = "11"
                    targetCompatibility = "11"
                }
            }
        }
    }

    fun enableLibraryDefaults() {
        // register additional artifacts
        withJavadocJar()
        withSourcesJar()
    }

    private fun baseDirectory(): File {
        return project.buildscript.sourceFile!!.parentFile
    }

    private fun findConfig(name: String): File? {
        return File(baseDirectory().parentFile, "config/$name").existsOrNull()
            ?: File(baseDirectory().parentFile, "config").listFiles { _, filename ->
                project.name.startsWith(filename)
            }?.map { config ->
                File(config, name)
            }?.firstNotNullOfOrNull(File::existsOrNull)
    }

    @JvmOverloads
    fun withCheckstyle(
        checkstyleVersion: String = "9.2.1",
        configFile: File? = findConfig("checkstyle/checkstyle.xml"),
    ) {
        project.pluginManager.apply(CheckstylePlugin::class.java)
        project.extensions.configure(CheckstyleExtension::class.java) {
            it.toolVersion = checkstyleVersion
            if (configFile != null) {
                it.configFile = configFile
            }
        }
        project.tasks.named("checkstyleTest", Checkstyle::class.java) {
            it.enabled = false
        }
    }

    @JvmOverloads
    fun withPmd(
        pmdVersion: String = "6.41.0",
        configFile: File? = findConfig("pmd/pmd.xml"),
    ) {
        project.pluginManager.apply(PmdPlugin::class.java)
        project.extensions.configure(PmdExtension::class.java) {
            it.toolVersion = pmdVersion
            it.ruleSets = emptyList()
            if (configFile != null) {
                it.ruleSetFiles = project.files(configFile)
            }
        }
        project.tasks.named("pmdTest", Pmd::class.java) {
            it.enabled = false
        }
    }

    @JvmOverloads
    fun withSpotBugs(
        spotbugsVersion: String = "4.5.3",
        includeFile: File? = findConfig("spotbugs/include.xml"),
        excludeFile: File? = findConfig("spotbugs/exclude.xml"),
    ) {
        project.pluginManager.apply(SpotBugsPlugin::class.java)
        project.extensions.configure(SpotBugsExtension::class.java) {
            it.toolVersion.set(spotbugsVersion)
            includeFile?.let(it.includeFilter::set)
            excludeFile?.let(it.excludeFilter::set)
        }
        project.tasks.named("spotbugsMain", SpotBugsTask::class.java) {
            it.reports.maybeCreate("html").apply {
                require(true)
                outputLocation.set(project.layout.buildDirectory.file("reports/spotbugs/main.html"))
            }
        }
        project.tasks.named("spotbugsTest", SpotBugsTask::class.java) {
            it.enabled = false
        }
    }

    fun withJavadocJar() {
        javaExtension.withJavadocJar()
    }

    fun withSourcesJar() {
        javaExtension.withSourcesJar()
    }

}
