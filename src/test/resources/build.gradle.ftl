plugins {
    id("maven-publish")
    id("java")
    id("de.lancom.genesis.version")
}

group = "org.example"
version = "${version}"

publishing {
    repositories {
        maven {
            name = "internalTest"
            url = uri("$projectDir/repository")
        }
    }
    publications {
      main(MavenPublication) {
        from components.java
      }
      client(MavenPublication) {
        from components.java
        artifactId = "test-project-client"
      }
    }
}

genesisVersion {
    <#list (config ! []) as line>
    ${line ! ""}
    </#list>
}
