# Genesis Java Gradle Plugin

Basic configuration for Java based Gradle projects. 

[Changelog](CHANGELOG.md)

## Activation
```kotlin 
plugins {
    id("de.lancom.genesis.java") version "<version>"
}
```

## Configuration
```kotlin 
genesisJava {

    // register additional artifacts
    withJavadocJar() 
    withSourcesJar()
    
    // enable code quality checks
    withCheckstyle(
        checkstyleVersion = "8.39"
    )
    withPmd(
        pmdVersion = "6.21.0"
    )
    withSpotBugs(
        spotbugsVersion = "4.2.0"
    )
}
```

#### Checkstyle
Detekt can be configured using the `config/checkstyle/checkstyle.xml` config file like
the [example file](example/config/checkstyle/checkstyle.xml) file.
For additional information see the [Checkstyle documentation](https://checkstyle.org/config.html).

#### Pmd
KtLinkt can be configured using the `config/pmd/pmd.xml` rule file like the [example file](example/config/pmd/pmd.xml).
For additional information see the [Pmd documentation](https://pmd.github.io/pmd-6.26.0/pmd_userdocs_making_rulesets.html).

#### Spotbugs
Spotbugs can be configured using the `config/spotbugs/spotbugs.xml` include file like the [example file](example/config/spotbugs/spotbugs.xml).
For additional information see the [Spotbugs documentation](https://spotbugs.readthedocs.io/en/stable/filter.html).

### Example

An example project can be found in the `examples/project`.

Execute `./gradlew -p example <tasks>` to run tasks of the [example project](./example).

## Plugins

- [SpotBugs Plugin](https://github.com/spotbugs/spotbugs-gradle-plugin)
- [Checkstyle Plugin](https://docs.gradle.org/current/userguide/checkstyle_plugin.html)
- [Pmd Plugin](https://docs.gradle.org/current/userguide/pmd_plugin.html)

## Maintainers
- [Artur Taube](https://github.com/Adduh)
- [Frederic Kneier](https://github.com/frederic-kneier)
- [Jonas von Andrian](https://github.com/johnny)
- [Maya Naydenova](https://github.com/mnaydeno)
