package com.example.domain.repository

import com.example.data.dao.ChatDao
import com.example.data.model.Chat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class ChatRepository @Inject constructor(
    private val chatDao: ChatDao
) {

    fun getAllChatByChatItemId(chatItemId: Long) = chatDao.getAllChatByChatItemId(chatItemId)

    fun insertChat(chat: Chat): Long {
        return chatDao.insert(chat)
    }

    fun deleteSelectedChat(chats: List<Chat>) {
        CoroutineScope(Dispatchers.IO).launch {
            chats.forEach {
                it.isDeleted = true
                it.isSelected = false
            }

            chats.map { it.chatId }.also {
                chatDao.deleteSelectedChat(it)
            }
        }
    }
}