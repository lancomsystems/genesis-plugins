package de.lancom.genesis.java

import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.equalTo
import org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Test

class JavadocJarTest {

    @Test
    fun `javadoc jar is created`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("javadocJar/build.gradle.template")
            }
            file("src/main/java/Main.java") {
                fromClasspath("javadocJar/source.java.template")
            }

            execute(":javadocJar") {
                assertThat(success, equalTo(true))
                assertThat(build.task(":javadocJar")?.outcome, equalTo(TaskOutcome.SUCCESS))
                assertThat(fileExists("build/libs/test-project-javadoc.jar"), equalTo(true))
            }
        }
    }

}