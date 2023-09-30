package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ChatItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatItemDao {

    @Query("SELECT * FROM chat_list WHERE `chatItemId` = :chatItemId")
    fun getChatItemById(chatItemId: Long): ChatItem

    @Query("SELECT chat_list.*, chat.message AS lastMessage, chat.sentTimeStamp AS lastMessageTimeStamp FROM chat_list LEFT JOIN ( SELECT chatItemId, message, sentTimeStamp FROM chat WHERE chat.sentTimeStamp = ( SELECT MAX(sentTimeStamp) FROM chat AS c WHERE c.chatItemId = chat.chatItemId ) ) AS chat ON chat_list.chatItemId = chat.chatItemId;")
    fun getAllChatList(): Flow<List<ChatItem>>

    @Query("SELECT * FROM chat_list WHERE `chatName` LIKE :chatName")
    fun queryChatExistence(chatName: String): ChatItem?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(chatItem: ChatItem): Long

    @Query("DELETE FROM chat_list WHERE chatItemId IN (:ids)")
    fun deleteItemByIds(ids: List<Long>)

    @Query("UPDATE chat_list SET isChatMuted = CASE WHEN isChatMuted =0 THEN 1 ELSE 0 END WHERE chatItemId IN (:ids)")
    fun updateMuteStatus(ids: List<Long>)
}
