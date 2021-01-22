package de.lancom.genesis.java

import com.github.spotbugs.snom.SpotBugsExtension
import com.github.spotbugs.snom.SpotBugsPlugin
import com.github.spotbugs.snom.SpotBugsTask
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.*
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer

open class GenesisJavaExtension(
    private val project: Project
) {
    private val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)

    @JvmOverloads
    fun withCheckstyle(
        checkstyleVersion: String = "8.39"
    ) {
        project.pluginManager.apply(CheckstylePlugin::class.java)
        project.extensions.configure(CheckstyleExtension::class.java) {
            it.toolVersion = checkstyleVersion
        }
    }

    @JvmOverloads
    fun withPmd(
        pmdVersion: String = "6.21.0"
    ) {
        project.pluginManager.apply(PmdPlugin::class.java)
        project.extensions.configure(PmdExtension::class.java) {
            it.toolVersion = pmdVersion
            val pmdConfig = project.file("config/pmd/pmd.xml")
            if (pmdConfig.exists()) {
                it.ruleSets = emptyList()
                it.ruleSetFiles = project.files(pmdConfig)
            }
        }
    }

    @JvmOverloads
    fun withSpotBugs(
        spotbugsVersion: String = "4.2.0"
    ) {
        project.pluginManager.apply(SpotBugsPlugin::class.java)
        project.extensions.configure(SpotBugsExtension::class.java) {
            it.toolVersion.set(spotbugsVersion)
            val spotbugsConfig = project.file("config/spotbugs/spotbugs.xml")
            if (spotbugsConfig.exists()) {
                it.includeFilter.set(spotbugsConfig)
            }
        }
    }

    fun withJavadocJar() {
        javaExtension.withJavadocJar()
    }

    fun withSourcesJar() {
        javaExtension.withSourcesJar()
    }

}