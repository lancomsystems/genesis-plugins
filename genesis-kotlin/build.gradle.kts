dependencies {
    implementation(gradleApi())
    implementation(libs.ioGitlabArturboschDetekt.detektGradlePlugin)
    implementation(libs.orgJetbrainsDokka.dokkaGradlePlugin)
    implementation(libs.orgJetbrainsKotlin.kotlinAllopen)
    implementation(libs.orgJetbrainsKotlin.kotlinGradlePlugin)
    implementation(libs.orgJetbrainsKotlin.kotlinNoarg)
    implementation(libs.orgJetbrainsKotlin.kotlinReflect)
    implementation(libs.orgJetbrainsKotlin.kotlinStdlibJdk8)
    implementation(libs.orgJlleitschuhGradle.ktlintGradle)

    testImplementation(gradleTestKit())
    testImplementation(libs.orgJunitJupiter.junitJupiterApi)

    testRuntimeOnly(libs.orgJunitJupiter.junitJupiterEngine)
}

gradlePlugin {
    plugins {
        register("kotlin") {
            id = "de.lancom.genesis.kotlin"
            displayName = "Lancom Genesis Kotlin Plugin"
            description = "Plugin for basic configuration of Kotlin based Gradle projects"
            implementationClass = "de.lancom.genesis.kotlin.KotlinPlugin"
            tags.set(listOf("kotlin"))
        }
    }
}
