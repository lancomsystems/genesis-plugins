dependencies {
    implementation(gradleApi())
    implementation(libs.gradlePluginComGithubSpotbugsSnom.spotbugsGradlePlugin)

    testImplementation(gradleTestKit())
    testImplementation(libs.orgJunitJupiter.junitJupiterApi)

    testRuntimeOnly(libs.orgJunitJupiter.junitJupiterEngine)
}

gradlePlugin {
    plugins {
        register("kotlin") {
            id = "de.lancom.genesis.java"
            displayName = "Lancom Genesis Java Plugin"
            description = "Plugin for basic configuration of Java based Gradle projects"
            implementationClass = "de.lancom.genesis.java.GenesisJavaPlugin"
            @Suppress("UnstableApiUsage")
            tags.set(listOf("java"))
        }
    }
}
