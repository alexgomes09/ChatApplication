package com.example.domain.usecases

import com.example.domain.repository.ChatListRepository
import javax.inject.Inject


class ChatListUseCase @Inject constructor(
    private val chatListRepository: ChatListRepository
) {

    fun getAllChatItem() = chatListRepository.getAllChatItem()

    fun getChatItemById(chatItemId:Long) = chatListRepository.getChatItemById(chatItemId)

    fun queryChatExistence(chatName:String) = chatListRepository.queryChatExistence(chatName)

    fun insertChatItem(chatName:String) = chatListRepository.insertChatItem(chatName)

    fun deleteSelectedChatList(ids: List<Long>) = chatListRepository.deleteSelectedChatList(ids)

    fun toggleMuteChatItem(ids: List<Long>) = chatListRepository.toggleMuteChatItem(ids)
}