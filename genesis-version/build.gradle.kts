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
    tags = listOf("java")
}
