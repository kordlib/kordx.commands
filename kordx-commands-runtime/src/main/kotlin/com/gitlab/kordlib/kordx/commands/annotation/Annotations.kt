package com.gitlab.kordlib.kordx.commands.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.FILE)
annotation class AutoWired

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER)
annotation class ModuleName(val name: String)
