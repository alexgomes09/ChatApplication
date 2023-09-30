package com.example.domain.usecases

import com.example.data.model.Chat
import com.example.domain.repository.ChatRepository
import javax.inject.Inject


class ChatUseCase @Inject constructor(
    private val chatRepository: ChatRepository
) {

    fun getAllChatByChatItemId(chatItemId: Long) = chatRepository.getAllChatByChatItemId(chatItemId)

    fun insertChat(chat: Chat) = chatRepository.insertChat(chat)

    fun deleteSelectedChat(chats: List<Chat>) = chatRepository.deleteSelectedChat(chats)

}