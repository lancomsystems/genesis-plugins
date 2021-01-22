package de.lancom.genesis.java

import org.gradle.internal.impldep.org.hamcrest.CoreMatchers
import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.equalTo
import org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test

class PmdTest {

    @Test
    fun `check with default validation fails on invalid file` () {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("pmd/build.gradle.template")
            }
            file("src/main/java/test/Main.java") {
                fromClasspath("pmd/source-fail.java.template")
            }
            execute(":pmdMain") {
                assertThat(success, equalTo(false))
                assertThat(build.task(":pmdMain")?.outcome, equalTo(TaskOutcome.FAILED))
                assertThat(build.output, CoreMatchers.containsString("2 PMD rule violations were found"))
            }
        }
    }

    @Test
    fun `check with default validation succeeds on valid file` () {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("pmd/build.gradle.template")
            }
            file("src/main/java/test/Main.java") {
                fromClasspath("pmd/source-success.java.template")
            }

            execute(":pmdMain") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":pmdMain")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }

    @Test
    fun `check with custom validation succeeds on invalid file` () {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("pmd/build.gradle.template")
            }
            file("src/main/java/test/Main.java") {
                fromClasspath("pmd/source-fail.java.template")
            }
            file("config/pmd/pmd.xml") {
                fromClasspath("pmd/pmd.xml.template")
            }
            execute(":pmdMain") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":pmdMain")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }

}