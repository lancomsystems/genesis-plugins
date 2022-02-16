package de.lancom.genesis.version

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.util.internal.VersionNumber

internal fun determineHotfixVersion(
    project: Project,
    baseVersion: VersionNumber,
    hotfixSize: Int,
): Int {
    var hotfix = 0
    project.extensions.findByType(PublishingExtension::class.java)?.also {
        if (it.publications.size == 0) {
            throw GradleException("No publications defined, cannot determine hotfix")
        }
        if (it.repositories.size == 0) {
            throw GradleException("No repositories defined, cannot determine hotfix")
        }

        it.publications.forEach { publication ->
            if (publication is MavenPublication) {
                it.repositories.forEach { repository ->
                    println(repository.name)
                    if (repository is MavenArtifactRepository) {
                        Util.listRevisions(
                            repository.url.toString(),
                            project.group.toString(),
                            publication.artifactId,
                        ).forEach {
                            try {
                                VersionNumber.parse(it).also {
                                    var match = true
                                    match = match && it.major == baseVersion.major
                                    match = match && it.minor == baseVersion.minor
                                    match = match && (it.micro / hotfixSize) == (baseVersion.micro / hotfixSize)
                                    match = match && it.qualifier == baseVersion.qualifier

                                    if (match) {
                                        hotfix = maxOf(it.micro % hotfixSize + 1, hotfix)
                                    }
                                }
                            } catch (_: Exception) {
                                project.logger.warn("Artifact version $it cannot be parsed.")
                            }
                        }
                    }
                }
            }
        }
    }
    if (hotfix == 0) {
        throw GradleException(
            "Hotfix not necessary. No version of '$baseVersion' released yet"
        )
    }

    if (hotfix >= hotfixSize) throw GradleException(
        "Cannot release a new hotfix. Next hotfix '$hotfix' would leak into the next patch version."
    )


    return hotfix
}
