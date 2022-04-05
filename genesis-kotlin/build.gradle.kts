val kotlinVersion = "1.6.20"

dependencies {
    implementation(gradleApi())
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:1.6.10")
    implementation("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.19.0")
    implementation("org.jetbrains.kotlin:kotlin-allopen:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-noarg:$kotlinVersion")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:10.2.1")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

gradlePlugin {
    plugins {
        register("kotlin") {
            id = "de.lancom.genesis.kotlin"
            displayName = "Lancom Genesis Kotlin Plugin"
            description = "Plugin for basic configuration of Kotlin based Gradle projects"
            implementationClass = "de.lancom.genesis.kotlin.KotlinPlugin"
        }
    }
}

pluginBundle {
    tags = listOf("kotlin")
}
