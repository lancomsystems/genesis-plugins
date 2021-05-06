package de.lancom.genesis.version

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ApplyVersionTypeTest {

    @Test
    fun `configured unmodified version type is applied correctly`() = execute(
        check = {
            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3-foo")
        },
    )

    @Test
    fun `command line default version type is applied correctly`() = execute(
        commandlineParameters = listOf("-PversionType=default"),
        check = {
            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3-foo")
        },
    )

    @Test
    fun `config default version type is applied correctly`() = execute(
        configParameters = listOf("withType('default')"),
        check = {
            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3-foo")
        },
    )

    @Test
    fun `command line snapshot version type is applied correctly`() = execute(
        commandlineParameters = listOf("-PversionType=snapshot"),
        check = {
            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3-SNAPSHOT")
        }
    )

    @Test
    fun `config snapshot version type is applied correctly`() = execute(
        configParameters = listOf("withType('snapshot')"),
        check = {
            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3-SNAPSHOT")
        }
    )

    @Test
    fun `command line release version type is applied correctly`() = execute(
        commandlineParameters = listOf("-PversionType=release"),
        check = {
            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3")
        },
    )

    @Test
    fun `config release version type is applied correctly`() = execute(
        configParameters = listOf("withType('release')"),
        check = {
            assertThat(success).isTrue
            assertThat(build.output.trim()).isEqualTo("1.2.3")
        },
    )

    @Test
    fun `command line hotfix version type fails`() = execute(
        commandlineParameters = listOf("-PversionType=hotfix"),
        check = {
            assertThat(success).isFalse
            assertThat(build.output).contains("Version hotfix number 1 has more than 0 digits")
        }
    )

    @Test
    fun `config hotfix version type fails`() = execute(
        configParameters = listOf("withType('hotfix')"),
        check = {
            assertThat(success).isFalse
            assertThat(build.output).contains("Version hotfix number 1 has more than 0 digits")
        }
    )

    @Test
    fun `command line build version type is applied correctly`() = execute(
        commandlineParameters = listOf("-PversionType=build"),
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

    @Test
    fun `config build version type is applied correctly`() = execute(
        configParameters = listOf("withType('build')"),
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
        configParameters: List<String> = emptyList(),
        commandlineParameters: List<String> = emptyList(),
        setup: TestSetup.() -> Unit = {},
        check: TestResult.() -> Unit = {},
    ) {
        testProject {
            file("settings.gradle") {
                fromTemplate("settings.gradle.ftl")
            }
            file("build.gradle") {
                fromTemplate(
                    "build.gradle.ftl", mapOf(
                        "version" to "1.2.3-foo",
                        "config" to configParameters
                    )
                )
            }

            setup()

            execute(commandlineParameters + listOf("-q", ":printVersion")) {
                check()
            }
        }
    }

}
