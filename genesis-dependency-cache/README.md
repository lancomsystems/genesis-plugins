# Genesis Dependency Cache Plugin

Downloads all dependencies for a gradle build without running the build.
This cache can then be used to generate a [read only cache of gradle dependencies](https://docs.gradle.org/current/userguide/dependency_resolution.html#sub:shared-readonly-cache).

[Changelog](CHANGELOG.md)

## Activation
```kotlin 
plugins {
    id("de.lancom.genesis.dependency-cache") version "<version>"
}
```

## Configuration

No configuration possible.

## Tasks

### `cacheDependencies`

Downloads all dependencies for the current build. This cache can then be used to generate a [read only cache of gradle dependencies](https://docs.gradle.org/current/userguide/dependency_resolution.html#sub:shared-readonly-cache).

In the following example `/gradle-cache` holds the global gradle cache and `/dependency-cache` contains the read only dependency cache.

```shell
gradle cacheDependencies --no-daemon -g /gradle-cache
rsync --exclude=\*.lock --exclude=gc.properties -rv --ignore-existing /gradle-cache/caches/modules-2 /dependency-cache

GRADLE_RO_DEP_CACHE=/dependency-cache gradle assemble 
```

## Example

An example project can be found in the `examples/project`.

Execute `./gradlew -p example cacheDependencies` to run tasks of the [example project](./example).

## Maintainers
- [Artur Taube](https://github.com/Adduh)
- [Jonas von Andrian](https://github.com/johnny)
- [Maya Naydenova](https://github.com/mnaydeno)
