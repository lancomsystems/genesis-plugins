package de.lancom.genesis.java

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

private val javaPlugins = setOf(
    "java-platform",
    "java-library",
    "java",
)

class GenesisJavaPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("genesis java plugin")
        if (!javaPlugins.any(project.pluginManager::hasPlugin)) {
            project.pluginManager.apply(JavaPlugin::class.java)
        }
        project.extensions.create("genesisJava", GenesisJavaExtension::class.java)
    }
}
