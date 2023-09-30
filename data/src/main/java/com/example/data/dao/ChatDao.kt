package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.model.Chat

@Dao
interface ChatDao {

    @Transaction
    @Query("SELECT * FROM chat WHERE chatItemId = :chatItemId")
    fun getAllChatByChatItemId(chatItemId: Long): List<Chat>

    @Query("SELECT * FROM chat WHERE chatId = :chatId")
    fun getChatById(chatId: Long): Chat?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(chat: Chat): Long

    @Query("UPDATE chat SET isDeleted = 1 WHERE chatId IN (:ids)")
    fun deleteSelectedChat(ids: List<Long>)
}
