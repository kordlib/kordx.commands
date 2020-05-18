package com.gitlab.kordlib.kordx.commands.model.prefix

typealias PrefixSupplier<S> = suspend (S) -> String
