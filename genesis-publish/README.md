# Genesis Java Gradle Plugin

Basic publication for Gradle projects. 

[Changelog](CHANGELOG.md)

## Activation
```kotlin 
plugins {
    id("de.lancom.genesis.publish") version "<version>"
}
```

## Configuration
```kotlin 
genesisPublish {
    withReleaseUrl("<url>") 
    withSnapshotUrl("<url>")
    withUser("<user>")
    withPassword("<password>")
    withComponent("<component>")
    withInsecure(<true|false>)
}
```
## Properties
- `publishReleaseUrl` override release url
- `publishSnapshotUrl` override snapshot url
- `publishUrl` override actual used url
- `publishUser` override repository credential user
- `publishPassword` override repository credential password
- `publishComponent` override published component
- `publishInsecure` override insecure publishing (https)

## Example

An example project can be found in the `examples/project`.

Execute `./gradlew -p example <tasks>` to run tasks of the [example project](./example).

## Plugins

- [Maven Publish](https://docs.gradle.org/current/userguide/publishing_maven.html)

## Maintainers
- [Artur Taube](https://github.com/Adduh)
- [Jonas von Andrian](https://github.com/johnny)
- [Maya Naydenova](https://github.com/mnaydeno)
