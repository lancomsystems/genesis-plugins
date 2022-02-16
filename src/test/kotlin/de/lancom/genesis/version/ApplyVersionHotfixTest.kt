package de.lancom.genesis.version

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ApplyVersionHotfixTest {

    @Test
    fun `invalid version without hotfix digits fails`() = execute(
        version = "1.2.3-foo",
        check = {
            assertThat(success).isFalse
            assertThat(build.output).contains(
                "Version number 1.2.3-foo does not reserve 2 digits in patch for hotfixes"
            )
        },
    )

    @Test
    fun `invalid version without hotfix digits and unmodified type fails`() = execute(
        version = "1.2.3-foo",
        check = {
            assertThat(success).isFalse
            assertThat(build.output).contains(
                "Version number 1.2.3-foo does not reserve 2 digits in patch for hotfixes"
            )
        },
    )

    @Test
    fun `invalid version without hotfix digits and snapshot type fails`() = execute(
        version = "1.2.3-foo",
        type = "snapshot",
        check = {
            assertThat(success).isFalse
            assertThat(build.output).contains(
                "Version number 1.2.3-foo does not reserve 2 digits in patch for hotfixes"
            )
        }
    )

    @Test
    fun `invalid version without hotfix digits and release type fails`() = execute(
        version = "1.2.3-foo",
        type = "release",
        check = {
            assertThat(success).isFalse
            assertThat(build.output).contains(
                "Version number 1.2.3-foo does not reserve 2 digits in patch for hotfixes"
            )
        },
    )

    @Test
    fun `invalid version without hotfix digits and hotfix type fails`() = execute(
        version = "1.2.3-foo",
        type = "hotfix",
        check = {
            assertThat(success).isFalse
            assertThat(build.output).contains(
                "Version number 1.2.3-foo does not reserve 2 digits in patch for hotfixes"
            )
        }
    )

    @Test
    fun `valid version without hotfix digits and hotfix type succeeds`() = execute(
        version = "1.2.300-foo",
        type = "hotfix",
        check = {
            assertThat(success).isTrue
            assertThat(build.output).contains("1.2.304")
        }
    )

    @Test
    fun `valid version with hotfix and qualifier`() = execute(
        version = "1.2.300",
        type = "hotfix",
        additionalArguments = arrayOf("-PversionQualifier=foo"),
        check = {
            assertThat(success).isTrue
            assertThat(build.output).contains("1.2.304-foo")
        }
    )

    private fun execute(
        type: String? = null,
        version: String? = null,
        additionalArguments: Array<String> = emptyArray(),
        check: TestResult.() -> Unit = {},
    ) {
        testProject {
            debug = true
            file("settings.gradle") {
                fromTemplate("settings.gradle.ftl")
            }
            file("build.gradle") {
                val variables = mapOf(
                    "version" to version,
                    "config" to listOf(
                        type?.let { "withType('$it')" },
                        "withHotfixDigits(2)",
                    )
                )
                fromTemplate("build.gradle.ftl", variables)
            }
            file("repository/org/example/test-project/maven-metadata.xml") {
                fromTemplate(
                    "maven-metadata.xml.ftl", mapOf(
                        "versions" to listOf(
                            "1.1.100",
                            "1.2.100",
                            "1.2.200",
                            "1.2.201",
                            "1.2.300",
                            "1.2.301",
                            "1.2.302",
                            "1.2.303",
                            "1.2.304-pre1",
                            "1.2.400",
                            "1.2.500",
                            "1.2.a",
                            "3.1.100",
                        )
                    )
                )
            }

            execute("-q", ":printVersion", *additionalArguments) {
                check()
            }
        }
    }
}
