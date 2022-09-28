package de.lancom.genesis.java

import org.gradle.internal.impldep.org.hamcrest.CoreMatchers
import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.equalTo
import org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test

class SpotbugsTest {

    @Test
    fun `check with default validation fails on invalid file`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("spotbugs/build.gradle.template")
            }
            file("src/main/java/test/Main.java") {
                fromClasspath("spotbugs/source-fail.java.template")
            }
            execute(":spotbugsMain") {
                assertThat(success, equalTo(false))
                assertThat(build.task(":spotbugsMain")?.outcome, equalTo(TaskOutcome.FAILED))
                assertThat(build.output, CoreMatchers.containsString("SpotBugs ended with exit code 1"))
            }
        }
    }

    @Test
    fun `check with default validation succeeds on valid file`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("spotbugs/build.gradle.template")
            }
            file("src/main/java/test/Main.java") {
                fromClasspath("spotbugs/source-success.java.template")
            }

            execute(":spotbugsMain") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":spotbugsMain")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }

    @Test
    fun `check with custom validation succeeds on invalid file`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("spotbugs/build.gradle.template")
            }
            file("src/main/java/test/Main.java") {
                fromClasspath("spotbugs/source-fail.java.template")
            }
            file("config/spotbugs/include.xml") {
                fromClasspath("spotbugs/spotbugs.xml.template")
            }
            execute(":spotbugsMain") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":spotbugsMain")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }

}
