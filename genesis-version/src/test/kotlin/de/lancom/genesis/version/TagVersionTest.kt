package de.lancom.genesis.version

import org.assertj.core.api.Assertions.assertThat
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.junit.jupiter.api.Test

class TagVersionTest {

    @Test
    fun `tagVersion tagPrefix is overwritten by extension`() = execute(
        check = {
            assertThat(success).isTrue

            val tagNames = setup.git.tagList().call().map {
                it.name.removePrefix("refs/tags/")
            }
            assertThat(tagNames).contains("VERSION-1.2.3")
        }
    )

    @Test
    fun `tag with custom prefix via command line is created`() = execute(
        commandlineParameters = listOf(
            "-PversionTagPrefix=v"
        ),
        check = {
            assertThat(success).isTrue

            val tagNames = setup.git.tagList().call().map {
                it.name.removePrefix("refs/tags/")
            }
            assertThat(tagNames).contains("v1.2.3")
        }
    )

    @Test
    fun `tag with custom prefix via config is created`() = execute(
        configParameters = listOf(
            "withTagPrefix('v')"
        ),
        check = {
            assertThat(success).isTrue

            val tagNames = setup.git.tagList().call().map {
                it.name.removePrefix("refs/tags/")
            }
            assertThat(tagNames).contains("v1.2.3")
        }
    )

    private fun execute(
        configParameters: List<String> = emptyList(),
        commandlineParameters: List<String> = emptyList(),
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

            val repository = FileRepositoryBuilder()
                .setMustExist(false)
                .setWorkTree(root)
                .build()

            repository.create()
            val git = Git(repository)
            git.commit().setMessage("test").setAll(true).call()

            execute(commandlineParameters + listOf("-q", "-PversionType=release", ":tagVersion")) {
                check()
            }
        }
    }
}
