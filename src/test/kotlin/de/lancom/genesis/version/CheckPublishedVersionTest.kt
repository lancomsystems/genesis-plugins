package de.lancom.genesis.version

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class CheckPublishedVersionTest {

    @Test
    fun `check if a older project version against a repository fails`() {
        testProject {
            file("settings.gradle") {
                fromTemplate("settings.gradle.ftl")
            }
            file("build.gradle") {
                fromTemplate("build.gradle.ftl", mapOf(
                    "version" to "3.1.100"
                ))
            }
            file("repository/org/example/test-project/maven-metadata.xml") {
                fromTemplate(
                    "maven-metadata.xml.ftl", mapOf(
                        "versions" to listOf(
                            "3.1.100",
                            "3.1.200",
                        )
                    )
                )
            }

            execute("-q", "-PversionType=default", ":checkPublishedVersion") {
                assertThat(success).isFalse
                assertThat(build.output).contains("Project version 3.1.100 is not newer than repository 3.1.200!")
            }
        }
    }

    @Test
    fun `check if an equal project version against a repository fails`() {
        testProject {
            file("settings.gradle") {
                fromTemplate("settings.gradle.ftl")
            }
            file("build.gradle") {
                fromTemplate("build.gradle.ftl", mapOf(
                    "version" to "3.1.200"
                ))
            }
            file("repository/org/example/test-project/maven-metadata.xml") {
                fromTemplate(
                    "maven-metadata.xml.ftl", mapOf(
                        "versions" to listOf(
                            "3.1.100",
                            "3.1.200",
                        )
                    )
                )
            }

            execute("-q", "-PversionType=default", ":checkPublishedVersion") {
                assertThat(success).isFalse
                assertThat(build.output).contains("Project version 3.1.200 is not newer than repository 3.1.200!")
            }
        }
    }

    @Test
    fun `check if a newer project version against a repository fails`() {
        testProject {
            file("settings.gradle") {
                fromTemplate("settings.gradle.ftl")
            }
            file("build.gradle") {
                fromTemplate("build.gradle.ftl", mapOf(
                    "version" to "3.1.300"
                ))
            }
            file("repository/org/example/test-project/maven-metadata.xml") {
                fromTemplate(
                    "maven-metadata.xml.ftl", mapOf(
                        "versions" to listOf(
                            "3.1.100",
                            "3.1.200",
                        )
                    )
                )
            }

            execute("-q", "-PversionType=default", ":checkPublishedVersion") {
                assertThat(success).isTrue()
            }
        }
    }

}
