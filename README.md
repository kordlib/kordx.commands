# kordx.commands 
[![Discord](https://img.shields.io/discord/556525343595298817.svg?color=&label=Kord&logo=discord&style=for-the-badge)](https://discord.gg/6jcx5ev)
[![Download](https://img.shields.io/bintray/v/kordlib/Kord/kordx.commands?color=&style=for-the-badge) ](https://bintray.com/kordlib/Kord/kordx.commands/_latestVersion) 

> This library is in an experimental stage, as such we can't guarantee API stability between releases.
> While we'd love for you to try out our library, we don't recommend you use this in production just yet.

kordx.commands strives to be a full featured generic command library that offers a type safe and extensible commands.
While we aim to provide the tools that you need, we also want to provide the tools to build the tools that you need.

[[_TOC_]]

## Installation
No longer available


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
