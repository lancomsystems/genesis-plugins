package de.lancom.genesis.version

import org.assertj.core.api.Assertions.assertThat
import org.gradle.util.internal.VersionNumber
import org.junit.jupiter.api.Test

class VersionExtractorTest {
    @Test
    fun `inline build gradle kotlin`() {
        val version = VersionExtractor.extractVersion("""
            group = "org.example"
            version = "1.2.3"
        """.trimIndent())
        assertThat(version).isEqualTo(VersionNumber.parse("1.2.3"))
    }

    @Test
    fun `gradle properties kotlin`() {
        val version = VersionExtractor.extractVersion("""
            group = "org.example"
            version = serviceVersion
            
            serviceVersion: 1.2.3
        """.trimIndent())
        assertThat(version).isEqualTo(VersionNumber.parse("1.2.3"))
    }

    @Test
    fun `subprojects kotlin`() {
        val version = VersionExtractor.extractVersion("""
            subprojects {
                group = "org.example"
                version = "1.2.3"
            }            
        """.trimIndent())
        assertThat(version).isEqualTo(VersionNumber.parse("1.2.3"))
    }

    @Test
    fun `inline build gradle groovy`() {
        val version = VersionExtractor.extractVersion("""
            group "org.example"
            version '1.2.3'
        """.trimIndent())
        assertThat(version).isEqualTo(VersionNumber.parse("1.2.3"))
    }

    @Test
    fun `gradle properties groovy`() {
        val version = VersionExtractor.extractVersion("""
            group "org.example"
            version serviceVersion
            
            serviceVersion: 1.2.3
        """.trimIndent())
        assertThat(version).isEqualTo(VersionNumber.parse("1.2.3"))
    }
}
