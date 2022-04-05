import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.lancom.genesis"
version = "2.0.0"

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.orgJetbrainsKotlinJvm)
    id("java-gradle-plugin")
    alias(libs.plugins.comGradlePluginPublish)
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
    apply(plugin = "java-gradle-plugin")
    apply(plugin = "com.gradle.plugin-publish")

    tasks.withType(Test::class.java).all {
        useJUnitPlatform()
        testLogging {
            events = setOf(PASSED, FAILED)
        }
    }

    tasks.withType(KotlinCompile::class.java).all {
        kotlinOptions.jvmTarget = "1.8"
    }

    tasks.withType(JavaCompile::class.java).all {
        targetCompatibility = "1.8"
        sourceCompatibility = "1.8"
    }

    rootProject.tasks.getByName("publishPlugins").dependsOn(tasks.getByName("publishPlugins"))

    pluginBundle {
        website = "https://github.com/lancomsystems/genesis-plugins"
        vcsUrl = "https://github.com/lancomsystems/genesis-plugins.git"
    }
}