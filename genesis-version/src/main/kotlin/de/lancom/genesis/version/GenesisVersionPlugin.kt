package de.lancom.genesis.version

import org.gradle.api.Plugin
import org.gradle.api.Project

class GenesisVersionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("genesisVersion", GenesisVersionExtension::class.java, project)
    }
}
