package de.lancom.genesis.dependency.cache

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer

class GenesisDependencyCachePlugin : Plugin<Project> {
    override fun apply(project: Project) {

        fun Configuration.isDeprecated() =
            this is org.gradle.internal.deprecation.DeprecatableConfiguration && resolutionAlternatives != null

        fun ConfigurationContainer.resolveAll() = this
            .filter { it.isCanBeResolved && !it.isDeprecated() }
            .forEach { it.resolve() }

        project.tasks.register("cacheDependencies") {
            it.doLast {
                project.configurations.resolveAll()
                project.buildscript.configurations.resolveAll()
            }
        }
    }
}
