import org.gradle.api.tasks.testing.logging.TestLogEvent

group = "de.lancom.genesis"
version = "0.1.0"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    id("maven-publish")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.12.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
}

gradlePlugin {
    plugins {
        register("publish") {
            id = "de.lancom.genesis.publish"
            displayName = "Lancom Genesis Publish Plugin"
            description = "Plugin for publishing to maven repository"
            implementationClass = "de.lancom.genesis.publish.GenesisPublishPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/lancomsystems/genesis-publish"
    vcsUrl = "https://github.com/lancomsystems/genesis-publish.git"
    tags = listOf("maven", "publish")
}

tasks.withType(Test::class.java) {
    useJUnitPlatform()
    testLogging {
        events = setOf(
            TestLogEvent.PASSED,
            TestLogEvent.FAILED
        )
    }
}

tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class.java).all {
    kotlinOptions.jvmTarget = "1.8"
}
