package de.lancom.genesis.publish

import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.containsString
import org.gradle.internal.impldep.org.hamcrest.CoreMatchers.equalTo
import org.gradle.internal.impldep.org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockserver.integration.ClientAndServer
import org.mockserver.junit.jupiter.MockServerExtension
import org.mockserver.junit.jupiter.MockServerSettings
import org.mockserver.mock.Expectation.`when`
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response

@ExtendWith(MockServerExtension::class)
@MockServerSettings(ports = [8888])
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

            execute("-PpublishComponent=invalid", ":publish") {
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
    fun `publish release with invalid release url fails`() {
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("release/build.gradle.template")
            }
            execute("-PpublishReleaseUrl=http://localhost:3333", ":publish") {
                assertThat(success, equalTo(false))
                assertThat(
                    this.build.output, containsString(
                        "Could not write to resource 'http://localhost:3333/test/test-project/1.0/test-project-1.0.jar'."
                    )
                )
            }
        }
    }

    @Test
    fun `publish release succeeds`(clientAndServer: ClientAndServer) {
        clientAndServer.upsert(
            `when`(
                request()
                    .withMethod("PUT")
                    .withPath("(/release/test/test-project/1.0/test-project-1.0.*|/release/test/test-project/maven-metadata.xml.*)")
            ).thenRespond(
                response()
                    .withStatusCode(200)
            )
        )
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("release/build.gradle.template")
            }
            execute(":publish") {
                assertThat(success, equalTo(true))
            }
        }
    }

    @Test
    fun `publish snapshot to publish folder succeeds`(clientAndServer: ClientAndServer) {
        clientAndServer.upsert(
            `when`(
                request()
                    .withMethod("PUT")
                    .withPath("(/snapshot/test/test-project/1.0-SNAPSHOT/(test-project-1.0|maven-metadata.xml).*|/snapshot/test/test-project/maven-metadata.xml.*)")
            ).thenRespond(
                response()
                    .withStatusCode(200)
            )
        )
        testProject {
            file("settings.gradle") {
                fromClasspath("settings.gradle.template")
            }
            file("build.gradle") {
                fromClasspath("snapshot/build.gradle.template")
            }

            execute(":publish") {
                assertThat(success, equalTo(true))
            }
        }
    }
}
