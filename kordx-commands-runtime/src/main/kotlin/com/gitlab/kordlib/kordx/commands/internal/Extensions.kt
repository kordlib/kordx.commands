@file:Suppress("UNCHECKED_CAST")

package com.gitlab.kordlib.kordx.commands.internal

internal fun<R> unsafeCast(item: Any?) = item as R
internal fun<R> Any?.cast() = unsafeCast<R>(this)