import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.lancom.genesis"
version = "0.1.0"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.21"
    id("maven-publish")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.12.0"
    id("com.palantir.idea-test-fix") version "0.1.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.mock-server:mockserver-junit-jupiter:5.11.2")

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
        events = setOf(PASSED, FAILED)
    }
}

tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions.jvmTarget = "1.8"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
