dependencies {
    implementation(gradleApi())

    testImplementation(gradleTestKit())
    testImplementation(libs.orgJunitJupiter.junitJupiterApi)
    testImplementation(libs.orgMockServer.mockserverJunitJupiter)

    testRuntimeOnly(libs.orgJunitJupiter.junitJupiterEngine)
}

gradlePlugin {
    plugins {
        register("publish") {
            id = "de.lancom.genesis.publish"
            displayName = "Lancom Genesis Publish Plugin"
            description = "Plugin for publishing to maven repository"
            implementationClass = "de.lancom.genesis.publish.GenesisPublishPlugin"
            tags.set(listOf("maven", "publish"))
        }
    }
}
