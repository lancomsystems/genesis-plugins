dependencies {
    implementation(gradleApi())
    implementation("gradle.plugin.com.github.spotbugs.snom:spotbugs-gradle-plugin:4.7.5")

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
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
    tags = listOf("java")
}
