package de.lancom.genesis.version

import org.apache.ivy.core.search.ModuleEntry
import org.apache.ivy.core.search.OrganisationEntry
import org.apache.ivy.core.settings.IvySettings
import org.apache.ivy.plugins.resolver.IBiblioResolver
import org.eclipse.jgit.api.CreateBranchCommand
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.RefNotFoundException
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.transport.TagOpt
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.treewalk.filter.PathFilterGroup
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import org.gradle.util.internal.VersionNumber
import java.io.File

object Util {
    fun listRevisions(
        url: String,
        group: String,
        name: String
    ) = IBiblioResolver().apply {
        isM2compatible = true
        settings = IvySettings()
        root = url
    }.let { resolver ->
        resolver.listRevisions(ModuleEntry(OrganisationEntry(resolver, group), name)).map { it.revision }
    }

    fun createGitRepo(path: File) = Git(
        FileRepositoryBuilder()
            .setMustExist(true)
            .setWorkTree(path)
            .findGitDir(path)
            .build()
    )

    fun checkBranchVersion(branch: String, git: Git, project: Project) {
        val currentVersion = VersionNumber.parse(project.version.toString())

        val fileContent = getFileContentForBranch(git, branch, "build.gradle", "build.gradle.kts", "gradle.properties")

        val branchVersion = VersionExtractor.extractVersion(fileContent)

        project.logger.lifecycle("Checking project version $currentVersion against branch version $branchVersion")
        if (currentVersion <= branchVersion) {
            throw GradleException("Build version $currentVersion is not newer than master version $branchVersion")
        }
    }

    fun checkPublishedVersion(project: Project) {
        val projectVersion = VersionNumber.parse(project.version.toString())
        val repositoryUrls = project.extensions.getByType(PublishingExtension::class.java).repositories.mapNotNull {
            (it as? MavenArtifactRepository)?.url?.toString()
        }

        val repositoryVersion = repositoryUrls.flatMap { url ->
            listRevisions(url, project.group.toString(), project.name).map { VersionNumber.parse(it) }
        }.maxOrNull()

        if (repositoryVersion != null && repositoryVersion >= projectVersion) {
            throw GradleException("Project version $projectVersion is not newer than repository $repositoryVersion!")
        }

    }

    private fun getFileContentForBranch(git: Git, branch: String, vararg matchingFileNames: String): String {
        val branchHead = getBranchHead(git, branch)
        val walk = RevWalk(git.repository)
        val commit = walk.parseCommit(branchHead.objectId)
        val treeWalker = TreeWalk(git.repository)
        treeWalker.addTree(commit.tree)
        treeWalker.isRecursive = true
        treeWalker.filter = PathFilterGroup.createFromStrings(*matchingFileNames)

        treeWalker.next() || throw GradleException("Could not find any file matching $matchingFileNames.")

        val fileContents = StringBuilder()
        do {
            fileContents.append(git.repository.open(treeWalker.getObjectId(0)).bytes.decodeToString())
        } while (treeWalker.next())
        return fileContents.toString()
    }

    private fun getBranchHead(git: Git, branch: String): Ref {
        val existingBranchHead = git.repository.findRef(branch)
        if (existingBranchHead != null) {
            return existingBranchHead
        }

        git.remoteList().call().first {
            try {
                git.branchCreate().setName(branch)
                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
                    .setStartPoint("${it.name}/$branch")
                    .setForce(true)
                    .call()
                true
            } catch (e: RefNotFoundException) {
                false
            }
        }
        return git.repository.findRef(branch)!!
    }

}
