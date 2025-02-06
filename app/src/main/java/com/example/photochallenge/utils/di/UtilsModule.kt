package com.example.photochallenge.utils.di

import com.example.photochallenge.utils.ImageStorage
import org.koin.dsl.module

val utilsModule = module {
    single { ImageStorage(get()) }
}