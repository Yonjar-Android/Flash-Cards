package com.example.flashcards.flashCards.di

import android.content.Context
import com.example.flashcards.flashCards.utils.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ResourceProviderModule {

    @Provides
    @Singleton
    fun provideResourceProvider(@ApplicationContext context: Context): ResourceProvider {
        return ResourceProvider(context)
    }
}