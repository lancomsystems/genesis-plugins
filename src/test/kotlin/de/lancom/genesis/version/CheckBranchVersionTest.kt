package de.lancom.genesis.version

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class CheckBranchVersionTest {

    @Test
    fun `check if a newer project version against a branch succeeds`() = execute(
        version = "1.2.4-bar",
        check = {
            assertThat(success).isTrue
        }
    )

    @Test
    fun `check if same project version against a branch fails`() = execute(
        version = "1.2.3-bar",
        check = {
            assertThat(success).isFalse
        }
    )

    @Test
    fun `check if older project version against a branch fails`() = execute(
        version = "1.2.2-bar",
        check = {
            assertThat(success).isFalse
        }
    )

    private fun execute(
        version: String,
        branch: String = "feature",
        check: TestResult.() -> Unit = {},
    ) {
        testProject {
            file("settings.gradle") {
                fromTemplate("settings.gradle.ftl")
            }
            file("build.gradle") {
                fromTemplate(
                    "build.gradle.ftl", mapOf(
                        "version" to "1.2.3-bar"
                    )
                )
            }

            commit()
            branch(branch)

            file("build.gradle") {
                fromTemplate(
                    "build.gradle.ftl", mapOf(
                        "version" to version
                    )
                )
            }

            commit()

            execute("-q", "-PversionType=default", ":checkBranchVersion") {
                check()
            }
        }
    }

}
