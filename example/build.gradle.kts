plugins {
    id("de.lancom.genesis.publish") version "0.1.1"
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

genesisPublish {
    withReleaseUrl("$buildDir/publish/release")
    withSnapshotUrl("$buildDir/publish/snapshot")
    withComponent("java")
}
