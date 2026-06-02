package com.example.standardapp.di

import android.content.Context
import com.example.standardapp.data.FirstMockRepository
import com.example.standardapp.data.SecondMockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMockRepository(@ApplicationContext context: Context): FirstMockRepository {
        return FirstMockRepository(context)
    }

    @Provides
    @Singleton
    fun provideSecondMockRepository(@ApplicationContext context: Context): SecondMockRepository {
        return SecondMockRepository(context)
    }
}
