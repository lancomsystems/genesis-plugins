plugins {
    id("maven-publish")
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
}

genesisVersion {
    <#list (config ! []) as line>
    ${line ! ""}
    </#list>
}
