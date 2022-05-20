package dev.kordx.processor

import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement


/**
 * Returns true of the element is a class
 */
val Element.isClass get() = this is TypeElement

/**
 * Returns the element as a [TypeElement]
 */
val Element.asClass get() = this as TypeElement

/**
 * Returns true if the element is a conventional property.
 * i.e. follows the naming convention of `getX` and has no parameters.
 */
val ExecutableElement.isProperty get() = simpleName.startsWith("get") && parameters.size == 0

/**
 * Returns the name of an element as it would be represented in kotlin source code.
 */
val ExecutableElement.simpleKotlinName
    get() = when (isProperty) {
        true -> simpleName.toString().removePrefix("get").decapitalize()
        false -> simpleName.toString()
    }

/**
 * Returns a valid call to an element, resolving function parameters with Koin's [org.koin.core.Koin.get].
 */
val ExecutableElement.resolved: String
    get() = when (isProperty) {
        true -> simpleName.toString().removePrefix("get").decapitalize()
        else -> "${simpleName}(${parameters.joinToString(",") { "get()" }})" //functionCall(get(),get(),...)
    }
