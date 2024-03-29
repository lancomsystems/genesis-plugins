plugins {
    id("de.lancom.genesis.kotlin") version "2.0.13"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

genesisKotlin {
    withJavadocJar()
    withSourcesJar()
    withDetekt()
    withKtlint()
}
