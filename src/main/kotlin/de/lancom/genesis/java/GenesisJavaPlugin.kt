package de.lancom.genesis.java

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

class GenesisJavaPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(JavaPlugin::class.java)
        project.extensions.create("genesisJava", GenesisJavaExtension::class.java)
    }
}
