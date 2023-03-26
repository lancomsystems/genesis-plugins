dependencies {
    implementation(gradleApi())

    testImplementation(gradleTestKit())
    testImplementation(libs.orgAssertj.assertjCore)
    testImplementation(libs.orgJunitJupiter.junitJupiterApi)

    testRuntimeOnly(libs.orgJunitJupiter.junitJupiterEngine)
}

gradlePlugin {
    plugins {
        register("dependency-cache") {
            id = "de.lancom.genesis.dependency-cache"
            displayName = "Lancom Genesis Dependency Cache Plugin"
            description = "Caches all dependencies so they can be used by a read only cache."
            implementationClass = "de.lancom.genesis.dependency.cache.GenesisDependencyCachePlugin"
            @Suppress("UnstableApiUsage")
            tags.set(listOf("cache", "dependency", "dependencies"))
        }
    }
}
