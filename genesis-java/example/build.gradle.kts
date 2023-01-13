plugins {
    id("de.lancom.genesis.java") version "2.0.8"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

genesisJava {
    withJavadocJar()
    withSourcesJar()
    withCheckstyle()
    withPmd()
    withSpotBugs()
}
