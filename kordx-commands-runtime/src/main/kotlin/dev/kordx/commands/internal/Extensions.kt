@file:Suppress("UNCHECKED_CAST", "NOTHING_TO_INLINE")

package dev.kordx.commands.internal

internal inline fun<R> unsafeCast(item: Any?) = item as R
internal inline fun<R> Any?.cast() = unsafeCast<R>(this)
