import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.lancom.genesis"
version = "1.0.0"

val kotlinVersion = "1.4.21"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.5.31"
    id("maven-publish")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.16.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())
    implementation("gradle.plugin.com.github.spotbugs.snom:spotbugs-gradle-plugin:4.7.5")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

gradlePlugin {
    plugins {
        register("kotlin") {
            id = "de.lancom.genesis.java"
            displayName = "Lancom Genesis Java Plugin"
            description = "Plugin for basic configuration of Java based Gradle projects"
            implementationClass = "de.lancom.genesis.java.GenesisJavaPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/lancomsystems/genesis-java"
    vcsUrl = "https://github.com/lancomsystems/genesis-java.git"
    tags = listOf("java")
}



tasks.withType(Test::class.java) {
    useJUnitPlatform()
    testLogging {
        events = setOf(PASSED, FAILED)
    }
}

tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions.jvmTarget = "1.8"
}
