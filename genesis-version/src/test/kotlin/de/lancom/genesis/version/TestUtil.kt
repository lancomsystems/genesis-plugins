package de.lancom.genesis.version

import freemarker.template.Configuration
import freemarker.template.Template
import org.eclipse.jgit.api.Git
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure
import java.io.File

/**
 * Provides an api for the creation of files in the test project.
 */
interface TestFile {
    fun fromResource(path: String)
    fun fromTemplate(path: String, variables: Map<String, Any?> = emptyMap())
    fun fromText(text: String)
}

/**
 * Provides an api to setup a GradleRunner test.
 */
interface TestSetup {

    /**
     * Git repository.
     */
    val git: Git

    /**
     * Root directory of the test project.
     */
    val root: File

    /**
     * Enable debug support for the test.
     * Might lead to classpath problems.
     */
    var debug: Boolean

    fun file(path: String, block: TestFile.() -> Unit)

    fun execute(vararg arguments: String?, result: TestResult.() -> Unit) {
        execute(arguments.toList(), result)
    }

    fun execute(arguments: List<String?> = emptyList(), result: TestResult.() -> Unit)
}

/**
 * Provides information about the test result.
 */
interface TestResult {
    val setup: TestSetup
    val success: Boolean
    val build: BuildResult
}

/**
 *  Utility function that allows easy setup, execution and validation of projects for unit tests using gradle runner.
 *  @see GradleRunner
 */
inline fun testProject(block: TestSetup.() -> Unit) {
    val root = File.createTempFile("pluginTest", "").apply {
        delete()
        mkdirs()
    }

    val classLoader = Thread.currentThread().contextClassLoader

    try {
        block(object : TestSetup {
            private val configuration by lazy { Configuration(Configuration.VERSION_2_3_30) }

            override val root: File get() = root
            override var debug: Boolean = false

            override val git by lazy {
                Git.init().setDirectory(root).call().apply {
                    add().addFilepattern(".").call()
                    commit().setMessage("test").setAll(true).call()
                }
            }

            override fun file(path: String, block: TestFile.() -> Unit) {
                val file = root.resolve(path).apply {
                    parentFile.mkdirs()
                }
                block(object : TestFile {
                    override fun fromResource(path: String) {
                        val resource = checkNotNull(classLoader.getResource(path)) {
                            "Could not find $path"
                        }
                        file.writeBytes(resource.readBytes())
                    }

                    override fun fromTemplate(path: String, variables: Map<String, Any?>) {
                        val resource = checkNotNull(classLoader.getResource(path)) {
                            "Could not find $path"
                        }
                        file.writer().use {
                            Template(path, resource.readText(), configuration).process(variables, it)
                        }
                        println(file.name)
                        println(file.readText())
                    }

                    override fun fromText(text: String) {
                        file.writeText(text.trimIndent())
                    }
                })
            }

            override fun execute(arguments: List<String?>, result: TestResult.() -> Unit) {
                val setup = this
                val buildResult = try {
                    object : TestResult {
                        override val setup: TestSetup = setup
                        override val success = true
                        override val build = GradleRunner.create().run {
                            withProjectDir(root)
                            withArguments(arguments.filterNotNull())
                            withPluginClasspath()
                            withDebug(debug)
                            build()
                        }
                    }
                } catch (ex: UnexpectedBuildFailure) {
                    object : TestResult {
                        override val setup: TestSetup = setup
                        override val success = false
                        override val build = ex.buildResult
                    }
                }
                try {
                    println(buildResult.build.output)
                    result(buildResult)
                } catch (ex: AssertionError) {
                    println(buildResult.build.output)
                    throw ex
                }
            }

        })
    } finally {
        root.deleteRecursively()
    }

}

fun TestSetup.branch(name: String) {
    println("changing branch to $name")
    git.checkout().setCreateBranch(true).setName(name).call()
}

fun TestSetup.commit() {
    println("committing all")
    git.add().addFilepattern(".").call()
    git.commit().setMessage("Commit").setAll(true).call()
}
