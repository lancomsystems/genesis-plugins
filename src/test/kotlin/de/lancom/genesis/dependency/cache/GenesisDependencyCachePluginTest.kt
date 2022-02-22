package de.lancom.genesis.dependency.cache

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GenesisDependencyCachePluginTest {

    @Test
    fun `cacheDependencies runs successfully`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("build.gradle.template")
            }

            execute(":cacheDependencies") {
                assertThat(success).isTrue()
            }
        }
    }
}
