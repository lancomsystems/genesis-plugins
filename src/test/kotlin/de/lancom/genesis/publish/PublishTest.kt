package de.lancom.genesis.publish

import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.containsString
import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.equalTo
import org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

class PublishTest {

    @Test
    fun `publish invalid component fails`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("release/build.gradle.template")
            }

            execute("-Ppublish_component=invalid", ":publish") {
                assertThat(success, equalTo(false))
                assertThat(
                    this.build.output, containsString(
                        "SoftwareComponentInternal with name 'invalid' not found."
                    )
                )
            }
        }
    }

    @Test
    fun `publish release fails`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("release/build.gradle.template")
            }
            execute(":publish") {
                assertThat(success, equalTo(false))
                assertThat(
                    this.build.output, containsString(
                        "Could not write to resource 'https://255.255.255.255/release/test/test-project/1.0/test-project-1.0.jar'."
                    )
                )
            }
        }
    }

    @Test
    fun `publish release with invalid release url fails`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("release/build.gradle.template")
            }
            execute("-Ppublish_release_url=https://1.1.1.255", ":publish") {
                assertThat(success, equalTo(false))
                assertThat(
                    this.build.output, containsString(
                        "Could not write to resource 'https://1.1.1.255/test/test-project/1.0/test-project-1.0.jar'."
                    )
                )
            }
        }
    }

    @Test
    fun `publish release to publish folder succeeds`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("release/build.gradle.template")
            }
            execute("-Ppublish_url=file://${root.absolutePath}/publish", ":publish") {
                assertThat(success, equalTo(true))
                assertThat(root.resolve("publish/test/test-project/1.0").exists(), equalTo(true))
            }
        }
    }

    @Test
    fun `publish release to custom release folder succeeds`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("release/build.gradle.template")
            }

            execute("-Ppublish_release_url=file://${root.absolutePath}/publish/release", ":publish") {
                assertThat(success, equalTo(true))
                assertThat(root.resolve("publish/release/test/test-project/1.0/test-project-1.0.jar").exists(), equalTo(true))
            }
        }
    }

    @Test
    fun `publish snapshot to publish folder succeeds`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("snapshot/build.gradle.template")
            }

            execute("-Ppublish_url=file://${root.absolutePath}/publish", ":publish") {
                assertThat(success, equalTo(true))
                assertThat(root.resolve("publish/test/test-project/1.0-SNAPSHOT").exists(), equalTo(true))
            }
        }
    }

    @Test
    fun `publish snapshot fails`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("snapshot/build.gradle.template")
            }
            execute(":publish") {
                assertThat(success, equalTo(false))
                assertThat(
                    this.build.output, containsString(
                        "Could not GET 'https://255.255.255.255/snapshot/test/test-project/1.0-SNAPSHOT/maven-metadata.xml'."
                    )
                )
            }
        }
    }

    @Test
    fun `publish snapshot with invalid snapshot url fails`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("snapshot/build.gradle.template")
            }
            execute("-Ppublish_snapshot_url=https://1.1.1.255",":publish") {
                assertThat(success, equalTo(false))
                assertThat(
                    this.build.output, containsString(
                        "Could not GET 'https://1.1.1.255/test/test-project/1.0-SNAPSHOT/maven-metadata.xml'."
                    )
                )
            }
        }
    }

    @Test
    fun `publish snapshot to custom snapshot folder succeeds`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("snapshot/build.gradle.template")
            }

            execute("-Ppublish_snapshot_url=file://${root.absolutePath}/publish/snapshot", ":publish") {
                assertThat(success, equalTo(true))
                assertThat(root.resolve("publish/snapshot/test/test-project/1.0-SNAPSHOT").exists(), equalTo(true))
            }
        }
    }

}