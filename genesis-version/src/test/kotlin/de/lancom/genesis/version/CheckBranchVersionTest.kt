package de.lancom.genesis.version

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class CheckBranchVersionTest {

    @Test
    fun `check if a newer project version against a branch succeeds`() = execute(
        baseBranchVersion = "1.2.3-bar",
        version = "1.2.4-bar",
        check = {
            assertThat(success).isTrue
        }
    )

    @Test
    fun `check if same project version against a branch fails`() = execute(
        baseBranchVersion = "1.2.3-bar",
        version = "1.2.3-bar",
        check = {
            assertThat(success).isFalse
        }
    )

    @Test
    fun `check if older project version against a branch fails`() = execute(
        baseBranchVersion = "1.2.3-bar",
        version = "1.2.2-bar",
        check = {
            assertThat(success).isFalse
        }
    )

    @Test
    fun `check branch version with change build gradle - gradle properties`() {
        testProject {
            debug = true
            branch("base")
            file("settings.gradle") {
                fromTemplate("settings.gradle.ftl")
            }
            file("build.gradle") {
                fromTemplate(
                    "build.gradle.ftl", mapOf(
                        "version" to "1.2.3-bar",
                        "config" to listOf(
                            "withBranch(\"${"base"}\")"
                        )
                    )
                )
            }

            commit()
            branch("feature")

            file("build.gradle") {
                fromTemplate(
                    "build.gradle.withProperties.ftl", mapOf(
                        "version" to "serviceVersion",
                        "config" to listOf(
                            "withBranch(\"${"base"}\")"
                        )
                    )
                )
            }

            file("gradle.properties") {
                fromTemplate(
                    "gradle.properties.ftl", mapOf(
                        "version" to "1.2.4-bar"
                    )
                )
            }

            commit()

            execute("-q", "-PversionType=default", ":checkBranchVersion", "--stacktrace") {
                assertThat(success).isTrue
            }
        }
    }

    @Test
    fun `check branch version with gradle properties`() {
        testProject {
            debug = true
            branch("base")
            file("settings.gradle") {
                fromTemplate("settings.gradle.ftl")
            }
            file("build.gradle") {
                fromTemplate(
                    "build.gradle.withProperties.ftl", mapOf(
                        "version" to "serviceVersion",
                        "config" to listOf(
                            "withBranch(\"${"base"}\")"
                        )
                    )
                )
            }

            file("gradle.properties") {
                fromTemplate(
                    "gradle.properties.ftl", mapOf(
                        "version" to "1.2.3-bar"
                    )
                )
            }

            commit()
            branch("feature")

            file("gradle.properties") {
                fromTemplate(
                    "gradle.properties.ftl", mapOf(
                        "version" to "1.2.4-bar"
                    )
                )
            }

            commit()

            execute("-q", "-PversionType=default", ":checkBranchVersion", "--stacktrace") {
                assertThat(success).isTrue
            }
        }
    }

    @Test
    fun `check branch version with non existant base branch`() {
        val missingBranchName = "notExistant"
        testProject {
            debug = true
            branch("base")
            file("settings.gradle") {
                fromTemplate("settings.gradle.ftl")
            }
            file("build.gradle") {
                fromTemplate(
                    "build.gradle.withProperties.ftl", mapOf(
                        "version" to "serviceVersion",
                        "config" to listOf(
                            "withBranch(\"$missingBranchName\")"
                        )
                    )
                )
            }

            file("gradle.properties") {
                fromTemplate(
                    "gradle.properties.ftl", mapOf(
                        "version" to "1.2.3-bar"
                    )
                )
            }

            commit()

            execute("-q", "-PversionType=default", ":checkBranchVersion", "--stacktrace") {
                assertThat(success).isFalse()
                assertThat(this.build.output).contains(missingBranchName)
            }
        }
    }

    private fun execute(
        baseBranchVersion: String,
        baseBranch: String = "base",
        version: String,
        branch: String = "feature",
        check: TestResult.() -> Unit = {},
    ) {
        testProject {
            debug = true
            branch(baseBranch)
            file("settings.gradle") {
                fromTemplate("settings.gradle.ftl")
            }
            file("build.gradle") {
                fromTemplate(
                    "build.gradle.ftl", mapOf(
                        "version" to baseBranchVersion,
                        "config" to listOf(
                            "withBranch(\"$baseBranch\")"
                        )
                    )
                )
            }

            commit()
            branch(branch)

            file("build.gradle") {
                fromTemplate(
                    "build.gradle.ftl", mapOf(
                        "version" to version,
                        "config" to listOf(
                            "withBranch(\"$baseBranch\")"
                        )
                    )
                )
            }

            commit()

            execute("-q", "-PversionType=default", ":checkBranchVersion", "--stacktrace") {
                check()
            }
        }
    }

}
