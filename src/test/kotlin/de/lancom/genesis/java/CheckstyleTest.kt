package de.lancom.genesis.java

import org.gradle.internal.impldep.org.hamcrest.CoreMatchers
import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.equalTo
import org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test

class CheckstyleTest {

    @Test
    fun `check with default validation fails on invalid file` () {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("checkstyle/build.gradle.template")
            }
            file("config/checkstyle/checkstyle.xml") {
                fromClasspath("checkstyle/checkstyle.xml.template")
            }
            file("src/main/java/test/Main.java") {
                fromClasspath("checkstyle/source-fail.java.template")
            }

            execute(":checkstyleMain") {
                assertThat(success, equalTo(false))
                assertThat(build.task(":checkstyleMain")?.outcome, equalTo(TaskOutcome.FAILED))
                assertThat(build.output, CoreMatchers.containsString("NewlineAtEndOfFile"))
                assertThat(build.output, CoreMatchers.containsString("DesignForExtension"))
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
                fromClasspath("checkstyle/build.gradle.template")
            }
            file("config/checkstyle/checkstyle.xml") {
                fromClasspath("checkstyle/checkstyle.xml.template")
            }
            file("src/main/java/test/Main.java") {
                fromClasspath("checkstyle/source-success.java.template")
            }

            execute(":checkstyleMain") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":checkstyleMain")?.outcome, equalTo(TaskOutcome.SUCCESS))
            }
        }
    }

}