package de.lancom.genesis.version

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ApplyVersionOverrideTypeTest {

    @Test
    fun `configured unmodified version type is applied correctly`() = execute(
        check = {
            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3-foo")
        },
    )

    @Test
    fun `configured default version type is applied correctly`() = execute(
        type = "default",
        check = {
            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3-foo")
        },
    )

    @Test
    fun `configured snapshot version type is applied correctly`() = execute(
        type = "snapshot",
        check = {
            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3-SNAPSHOT")
        }
    )

    @Test
    fun `configured release version type is applied correctly`() = execute(
        type = "release",
        check = {
            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3")
        },
    )

    @Test
    fun `configured hotfix version type fails`() = execute(
        type = "hotfix",
        check = {
            assertThat(success).isFalse
            assertThat(build.output).contains("Version hotfix number 1 has more than 0 digits")
        }
    )

    @Test
    fun `configured build version type is applied correctly`() = execute(
        type = "build",
        setup = {
            git.add().addFilepattern(".").call()
            git.commit().setMessage("test").setAll(true).call()
        },
        check = {
            val user = System.getProperty("user.name")
            val sha = setup.git.log().call().first().abbreviate(7).name()

            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3-$user-$sha")
        }
    )

    private fun execute(
        type: String? = null,
        setup: TestSetup.() -> Unit = {},
        check: TestResult.() -> Unit = {},
    ) {
        testProject {
            file("settings.gradle") {
                fromTemplate("settings.gradle.ftl")
            }
            file("build.gradle") {
                fromTemplate("build.gradle.ftl", mapOf(
                    "version" to "1.2.3-foo"
                ))
            }

            val versionParameter = type?.let { "-PversionType=$it" }

            setup()

            execute("-q", versionParameter, ":printVersion") {
                check()
            }
        }
    }

}
