import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "de.lancom.genesis"
version = "0.1.3"

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.20"
    id("maven-publish")
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.21.0"
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(gradleApi())

    implementation("org.eclipse.jgit:org.eclipse.jgit:6.1.0.202203080745-r")
    implementation("org.apache.ivy:ivy:2.5.0")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("org.assertj:assertj-core:3.22.0")
    testImplementation("org.freemarker:freemarker:2.3.31")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

gradlePlugin {
    plugins {
        register("kotlin") {
            id = "de.lancom.genesis.version"
            displayName = "LANCOM Genesis Version Plugin"
            description = "Basic version handling for Gradle projects"
            implementationClass = "de.lancom.genesis.version.GenesisVersionPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/lancomsystems/genesis-version"
    vcsUrl = "https://github.com/lancomsystems/genesis-version.git"
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

tasks.withType(JavaCompile::class.java).all {
    targetCompatibility = "1.8"
}
