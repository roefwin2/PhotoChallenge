package com.example.photochallenge.core.utils.di

import com.example.photochallenge.core.utils.ImageStorage
import org.koin.dsl.module

val utilsModule = module {
    single { ImageStorage(get()) }
}