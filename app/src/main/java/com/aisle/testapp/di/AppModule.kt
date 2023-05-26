package com.aisle.testapp.di

import android.content.Context
import android.content.SharedPreferences
import com.aisle.testapp.data.ApiService
import com.aisle.testapp.data.LoginRepository
import com.aisle.testapp.data.LoginRepositoryImpl
import com.aisle.testapp.data.NotesRepository
import com.aisle.testapp.data.NotesRepositoryImpl
import com.aisle.testapp.others.AppConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideLoginRepository(apiService: ApiService):LoginRepository{
        return LoginRepositoryImpl(apiService)
    }

    @Singleton
    @Provides
    fun provideNotesRepository(apiService: ApiService):NotesRepository{
        return NotesRepositoryImpl(apiService)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context):SharedPreferences{
        return context.getSharedPreferences(AppConstants.USER_SHARED_PREF, Context.MODE_PRIVATE)
    }

}