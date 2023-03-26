dependencies {
    implementation(gradleApi())

    implementation(libs.orgEclipseJgit.orgEclipseJgit)
    implementation(libs.orgApacheIvy.ivy)

    testImplementation(gradleTestKit())
    testImplementation(libs.orgJunitJupiter.junitJupiterApi)
    testImplementation(libs.orgJunitJupiter.junitJupiterParams)
    testImplementation(libs.orgAssertj.assertjCore)
    testImplementation(libs.orgFreemarker.freemarker)

    testRuntimeOnly(libs.orgJunitJupiter.junitJupiterEngine)
}

gradlePlugin {
    plugins {
        register("kotlin") {
            id = "de.lancom.genesis.version"
            displayName = "LANCOM Genesis Version Plugin"
            description = "Basic version handling for Gradle projects"
            implementationClass = "de.lancom.genesis.version.GenesisVersionPlugin"
            tags.set(listOf("java"))
        }
    }
}
