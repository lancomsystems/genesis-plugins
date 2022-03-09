import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.lancom.genesis"
version = "0.1.0"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.10"
    id("maven-publish")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.20.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())

    testImplementation(gradleTestKit())
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

gradlePlugin {
    plugins {
        register("dependency-cache") {
            id = "de.lancom.genesis.dependency-cache"
            displayName = "Lancom Genesis Dependency Cache Plugin"
            description = "Caches all dependencies so they can be used by a read only cache."
            implementationClass = "de.lancom.genesis.dependency.cache.GenesisDependencyCachePlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/lancomsystems/genesis-dependency-cache"
    vcsUrl = "https://github.com/lancomsystems/genesis-dependency-cache.git"
    tags = listOf("cache", "dependency", "dependencies")
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

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
