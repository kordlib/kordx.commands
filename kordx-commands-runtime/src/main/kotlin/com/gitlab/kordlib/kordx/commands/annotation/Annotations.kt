package com.gitlab.kordlib.kordx.processor


@Target(AnnotationTarget.FUNCTION)
annotation class SupplyCommandModule


@Target(AnnotationTarget.FUNCTION)
annotation class SupplyModuleModifier

@Target(AnnotationTarget.FUNCTION)
annotation class SupplyEventFilter


@Target(AnnotationTarget.FUNCTION)
annotation class SupplyEventHandler

@Target(AnnotationTarget.FUNCTION)
annotation class SupplyEventSource

@Target(AnnotationTarget.FUNCTION)
annotation class SupplyPrecondition


@Target(AnnotationTarget.FUNCTION)
annotation class SupplyPrefix
