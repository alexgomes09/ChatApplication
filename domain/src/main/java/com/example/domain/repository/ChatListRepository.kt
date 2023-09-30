package com.example.domain.repository

import com.example.data.dao.ChatItemDao
import com.example.data.model.ChatItem
import javax.inject.Inject


class ChatListRepository @Inject constructor(
    private val chatItemDao: ChatItemDao
) {

    fun getAllChatItem() = chatItemDao.getAllChatList()

    fun queryChatExistence(chatName: String) = chatItemDao.queryChatExistence(chatName)

    fun insertChatItem(chatName: String): Long {
        ChatItem(chatName = chatName).also {
            return chatItemDao.insert(it)
        }
    }

    fun getChatItemById(chatItemId: Long): ChatItem {
        return chatItemDao.getChatItemById(chatItemId)
    }

    fun deleteSelectedChatList(ids: List<Long>) = chatItemDao.deleteItemByIds(ids)

    fun toggleMuteChatItem(ids: List<Long>) {

        chatItemDao.updateMuteStatus(ids)

    }
}