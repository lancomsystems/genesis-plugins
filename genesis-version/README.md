# Genesis Version Gradle Plugin

Version handling for Gradle projects. 

[Changelog](CHANGELOG.md)

## Activation
```kotlin 
plugins {
    id("de.lancom.genesis.version") version "<version>"
}
```

## Configuration
```kotlin 
genesisVersion {
    withHotfixDigits(0)
    withType("default"|"snapshot"|"release"|"hotfix"|"build")
    withTagPrefix("VERSION-")
    withBranch("master")
}
```

### Version Types

#### `default`
Does not modify the configured version.

#### `snapshot` 
Replaces the version qualifier with "SNAPSHOT"

#### `release` 
Removes the version qualifier

#### `hotfix` 
Uses the next free hotfix version for the given version available in the publishing repository

#### `build` 
Replaces the version qualifier the git commit hash and the system user


### Tasks

#### `printVersion`
Prints the project version.

#### `tagVersion`
Creates a git tag using the project version and tag prefix.

#### `checkBranchVersion`
Checks that the project version is newer that the one configured in the latest commit of the configured git branch.

#### `checkPublishedVersion`
Checks that the project version is newer that the one latest one published in the publishing maven repository.

### Example

An example project can be found in the `examples/project`.

Execute `./gradlew -p example <tasks>` to run tasks of the [example project](./example).

## Maintainers
- [Artur Taube](https://github.com/Adduh)
- [Jonas von Andrian](https://github.com/johnny)
- [Maya Naydenova](https://github.com/mnaydeno)
