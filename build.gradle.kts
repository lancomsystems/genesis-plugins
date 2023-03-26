import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.lancom.genesis"
version = "2.0.11"

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.orgJetbrainsKotlinJvm)
    id("java-gradle-plugin")
    alias(libs.plugins.comGradlePluginPublish)
}

tasks.getByName("publishPlugins").enabled = false

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
        kotlinOptions.jvmTarget = "11"
    }

    tasks.withType(JavaCompile::class.java).all {
        targetCompatibility = "11"
        sourceCompatibility = "11"
    }

    rootProject.tasks.getByName("publishPlugins").dependsOn(tasks.getByName("publishPlugins"))

    @Suppress("UnstableApiUsage")
    gradlePlugin {
        website.set("https://github.com/lancomsystems/genesis-plugins")
        vcsUrl.set("https://github.com/lancomsystems/genesis-plugins.git")
    }
}
