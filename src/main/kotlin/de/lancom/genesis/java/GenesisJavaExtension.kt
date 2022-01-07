package de.lancom.genesis.java

import com.github.spotbugs.snom.SpotBugsExtension
import com.github.spotbugs.snom.SpotBugsPlugin
import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.*

open class GenesisJavaExtension(
    private val project: Project
) {
    private val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)

    @JvmOverloads
    fun withCheckstyle(
        checkstyleVersion: String = "9.2.1"
    ) {
        project.pluginManager.apply(CheckstylePlugin::class.java)
        project.extensions.configure(CheckstyleExtension::class.java) {
            it.toolVersion = checkstyleVersion
            it.configFile = project.rootProject.file("config/checkstyle/checkstyle.xml")
        }
        project.tasks.named("checkstyleTest", Checkstyle::class.java) {
            it.enabled = false
        }
    }

    @JvmOverloads
    fun withPmd(
        pmdVersion: String = "6.41.0"
    ) {
        project.pluginManager.apply(PmdPlugin::class.java)
        project.extensions.configure(PmdExtension::class.java) {
            it.toolVersion = pmdVersion
            val pmdConfig = project.rootProject.file("config/pmd/pmd.xml")
            if (pmdConfig.exists()) {
                it.ruleSets = emptyList()
                it.ruleSetFiles = project.files(pmdConfig)
            }
        }
        project.tasks.named("pmdTest", Pmd::class.java) {
            it.enabled = false
        }
    }

    @JvmOverloads
    fun withSpotBugs(
        spotbugsVersion: String = "4.5.3"
    ) {
        project.pluginManager.apply(SpotBugsPlugin::class.java)
        project.extensions.configure(SpotBugsExtension::class.java) {
            it.toolVersion.set(spotbugsVersion)
            val includeConfig = project.rootProject.file("config/spotbugs/include.xml")
            if (includeConfig.exists()) {
                it.includeFilter.set(includeConfig)
            }

            val excludeConfig = project.rootProject.file("config/spotbugs/exclude.xml")
            if (excludeConfig.exists()) {
                it.excludeFilter.set(excludeConfig)
            }
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
