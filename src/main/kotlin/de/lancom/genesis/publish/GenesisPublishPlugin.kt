package de.lancom.genesis.publish

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.publish.plugins.PublishingPlugin

class GenesisPublishPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        project.pluginManager.apply(MavenPublishPlugin::class.java)
        project.extensions.create("genesisPublish", GenesisPublishExtension::class.java, project)

    }
}