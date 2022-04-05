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
    tags = listOf("cache", "dependency", "dependencies")
}
