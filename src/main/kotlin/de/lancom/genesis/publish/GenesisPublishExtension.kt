package de.lancom.genesis.publish

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

open class GenesisPublishExtension(
    private val project: Project
) {

    private val releaseUrl = project.objects.property(String::class.java)
    private val snapshotUrl = project.objects.property(String::class.java)
    private val user = project.objects.property(String::class.java)
    private val password = project.objects.property(String::class.java)
    private val component = project.objects.property(String::class.java)
    private val insecure = project.objects.property(Boolean::class.java)

    init {
        project.afterEvaluate {
            project.extensions.configure(PublishingExtension::class.java) {

                val releaseUrlProperty = project.findProperty("publishReleaseUrl")?.toString()
                val snapshotUrlProperty = project.findProperty("publishSnapshotUrl")?.toString()
                val urlProperty = project.findProperty("publishUrl")?.toString()
                val userProperty = project.findProperty("publishUser")?.toString()
                val passwordProperty = project.findProperty("publishPassword")?.toString()
                val componentProperty = project.findProperty("publishComponent")?.toString()
                val insecureProperty = project.findProperty("publishInsecure")?.let { it.toString() == "true" }

                val snapshot = project.version.toString().endsWith("-SNAPSHOT")
                val releaseUrl = releaseUrlProperty ?: releaseUrl.orNull
                val snapshotUrl = snapshotUrlProperty ?: snapshotUrl.orNull ?: releaseUrl
                val user = userProperty ?: user.orNull
                val password = passwordProperty ?: password.orNull
                val component = componentProperty ?: component.orNull
                val insecure = insecureProperty ?: insecure.orNull ?: false

                val url = urlProperty ?: if (snapshot) snapshotUrl else releaseUrl

                if (url != null) {
                    it.repositories.maven {
                        it.name = "publish"
                        it.url = project.uri(url)
                        it.isAllowInsecureProtocol = insecure
                        if (user != null) {
                            it.credentials {
                                it.username = user
                                it.password = password
                            }
                        }
                    }
                }
                if (component != null) {
                    it.publications {
                        it.create("main", MavenPublication::class.java) {
                            it.from(project.components.getByName(component))
                        }
                    }
                }

            }
        }
    }

    fun withReleaseUrl(releaseUrl: String) {
        this.releaseUrl.set(releaseUrl)
    }

    fun withSnapshotUrl(snapshotUrl: String) {
        this.snapshotUrl.set(snapshotUrl)
    }

    fun withUser(user: String) {
        this.user.set(user)
    }

    fun withPassword(password: String) {
        this.password.set(password)
    }

    fun withComponent(component: String) {
        this.component.set(component)
    }

    fun withInsecure(insecure: Boolean) {
        this.insecure.set(insecure)
    }

}
