@file:Suppress("UNCHECKED_CAST")

package com.gitlab.kordlib.kordx.commands.internal

internal fun<T, R> unsafeCast(item: T) = item as R
internal fun<T, R> T.cast() = unsafeCast<T,R>(this)