package de.lancom.genesis.version

import org.gradle.api.GradleException
import org.gradle.util.internal.VersionNumber

object VersionExtractor {
    private val versionRegex = getRegex("version")

    fun extractVersion(filesContent: String): VersionNumber? {
        val groupValues = versionRegex.find(filesContent)?.groupValues
            ?: throw GradleException("Unable to determine base branch version")

        if (groupValues[1].isBlank()) {
            val identifier = groupValues[2]
            val version = getRegex(identifier).find(filesContent)?.groupValues?.get(2)
                ?: throw GradleException("Unable to resolve identifier $identifier")
            return VersionNumber.parse(version)
        } else {
            return VersionNumber.parse(groupValues[2])
        }
    }

    private fun getRegex(versionVariable: String): Regex {
        return "^$versionVariable\\s*[=:]?\\s*([\"']?)(.+)\\1$".toRegex(RegexOption.MULTILINE)
    }
}
