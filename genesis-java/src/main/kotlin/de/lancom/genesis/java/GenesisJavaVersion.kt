package de.lancom.genesis.java

import org.gradle.api.Project

enum class GenesisJavaVersion {
    Java11,
    Java17,
    ;

    companion object {
        fun projectJavaVersion(project: Project): GenesisJavaVersion {
            return when (project.properties["genesisJavaVersion"]) {
                "java17" ->
                    Java17

                else ->
                    Java11
            }
        }
    }
}
