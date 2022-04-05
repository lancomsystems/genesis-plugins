dependencies {
    implementation(gradleApi())

    testImplementation(gradleTestKit())
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.mock-server:mockserver-junit-jupiter:5.13.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
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
    tags = listOf("maven", "publish")
}
