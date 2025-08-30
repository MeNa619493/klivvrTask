package com.example.klivvrtask.di

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class IODispatcher

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class DefaultDispatcher