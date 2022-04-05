rootProject.name = "genesis-dependency-cache"

include(":genesis-dependency-cache")
include(":genesis-java")
include(":genesis-kotlin")
include(":genesis-publish")
include(":genesis-version")

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        clear()
        addAll(pluginManagement.repositories)
    }
}
