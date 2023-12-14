package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.api.APIConfig
import com.example.storyapp.database.StoryDatabase
import com.example.storyapp.repository.MainRepository

object Injection {
    fun provideRepository(context: Context): MainRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = APIConfig.getApiService()
        return MainRepository(database, apiService)
    }
}