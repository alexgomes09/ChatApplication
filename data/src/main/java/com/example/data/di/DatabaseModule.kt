package com.example.data.di

import android.content.Context
import com.example.data.dao.ChatDao
import com.example.data.dao.ChatItemDao
import com.example.data.db.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideChatListDao(@ApplicationContext context: Context): ChatItemDao =
        ChatDatabase.getDatabase(context).chatListDao()

    @Provides
    @Singleton
    fun provideChatDao(@ApplicationContext context: Context): ChatDao =
        ChatDatabase.getDatabase(context).chatDao()
}