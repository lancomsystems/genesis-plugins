package de.lancom.genesis.kotlin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("unused")
/**
 * Genesis Kotlin plugin.
 */
class KotlinPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        println("genesis kotlin plugin")
        project.plugins.apply(KotlinPluginWrapper::class.java)

        val javaExtension = project.extensions.getByType(JavaPluginExtension::class.java)
        val extension = project.extensions.create("genesisKotlin", KotlinExtension::class.java, project)

        project.tasks.withType(KotlinCompile::class.java).configureEach { task ->
            val target = javaExtension.toolchain.languageVersion.get().asInt()
            println("using $target as kotlin jvmTarget (${project.name})")
            task.kotlinOptions.jvmTarget = "$target"
            task.kotlinOptions.freeCompilerArgs = extension.freeCompilerArgs.get()
        }
    }
}
