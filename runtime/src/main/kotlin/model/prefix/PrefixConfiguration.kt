package dev.kord.x.commands.model.prefix

/**
 * Configuration for the [PrefixBuilder]. Represents a `(PrefixBuilder) -> Unit`.
 */
interface PrefixConfiguration {

    /**
     * Applies some configuration to the [builder].
     */
    fun apply(builder: PrefixBuilder)

}
