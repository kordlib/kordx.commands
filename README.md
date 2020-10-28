# kordx.commands 
[![Discord](https://img.shields.io/discord/556525343595298817.svg?color=&label=Kord&logo=discord&style=for-the-badge)](https://discord.gg/6jcx5ev)
[![Download](https://img.shields.io/bintray/v/kordlib/Kord/kordx.commands?color=&style=for-the-badge) ](https://bintray.com/kordlib/Kord/kordx.commands/_latestVersion) 

> This library is in an experimental stage, as such we can't guarantee API stability between releases.
> While we'd love for you to try out our library, we don't recommend you use this in production just yet.

kordx.commands strives to be a full featured generic command library that offers a type safe and extensible commands.
While we aim to provide the tools that you need, we also want to provide the tools to build the tools that you need.

[[_TOC_]]

## Installation

Replace `{version}` with the latest version number on bintray. [![Download](https://img.shields.io/bintray/v/kordlib/Kord/kordx.commands?color=&style=for-the-badge) ](https://bintray.com/kordlib/Kord/kordx.commands/_latestVersion) 

Replace `{kotlinVersion}` with the latest kotlin version.

### kordx.commands.kord

#### Gradle (groovy)

```groovy
plugins {
    id("org.jetbrains.kotlin.kapt") version "{kotlinVersion}"
}

repositories {
    maven { url "https://dl.bintray.com/kordlib/Kord" }
    jcenter()
}

dependencies {
    implementation("com.gitlab.kordlib.kordx:kordx-commands-runtime-kord:{version}")
    kapt("com.gitlab.kordlib.kordx:kordx-commands-processor:{version}")
}
```

#### Gradle (kotlin)

```kotlin
plugins {
    id("org.jetbrains.kotlin.kapt") version "{kotlinVersion}"
}

repositories {
    maven(url = "https://dl.bintray.com/kordlib/Kord")
    jcenter()
}

dependencies {
    implementation("com.gitlab.kordlib.kordx:kordx-commands-runtime-kord:{version}")
    kapt("com.gitlab.kordlib.kordx:kordx-commands-processor:{version}")
}
```

#### Maven

Add an execution of the kapt goal from kotlin-maven-plugin before compile
```xml
 <plugin>
    <executions>
        <execution>
            <id>kapt</id>
            <goals>
                <goal>kapt</goal>
            </goals>
            <configuration>
                <sourceDirs>
                    <sourceDir>src/main/kotlin</sourceDir>
                    <sourceDir>src/main/java</sourceDir>
                </sourceDirs>
                <annotationProcessorPaths>
                    <annotationProcessorPath>
                        <groupId>com.gitlab.kordlib.kordx</groupId>
                        <artifactId>kordx-commands-processor</artifactId>
                        <version>{version}</version>
                    </annotationProcessorPath>
                </annotationProcessorPaths>
            </configuration>
        </execution>
    </executions>
</plugin>
```

```xml
<repository>
    <id>bintray-kord</id>
    <url>https://dl.bintray.com/kordlib/Kord</url>
</repository>
```

```xml
<dependency>
    <groupId>com.gitlab.kordlib.kordx</groupId>
    <artifactId>kordx-commands-runtime-kord</artifactId>
    <version>{version}</version>
</dependency>
```


## implemented and planned targets

kordx.commands currently only supports `Kord`, we plan on supporting other popular Discord JVM APIs (JDA, D4J) as well as a CLI target.
If you have a suggestion for more targets, you can create an issue (or go to our Discord server) to discuss your idea!

## Quick example

> This snippet assumes you're using kordx.commands.kord and the annotation processor. 
>
> For more information on how to set up a bot and all the features this library offers, check out the [wiki](https://github.com/kordlib/kordx.commands/wiki/Kord-Discord-bot).

```kotlin
@file:Autowired

suspend fun main() = bot("token") {
    configure()
}

val prefix = prefix {
    kord { literal("!") or mention() }
}

fun testModule() = module("test-module") {
    command("add") {
        invoke(IntArgument, IntArgument) { a, b ->
            respond("${a + b}")
        }
    }
}
```
