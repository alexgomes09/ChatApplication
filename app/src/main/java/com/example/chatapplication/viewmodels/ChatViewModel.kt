package com.example.chatapplication.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.model.Chat
import com.example.data.model.ChatItem
import com.example.domain.usecases.ChatListUseCase
import com.example.domain.usecases.ChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatListUseCase: ChatListUseCase,
    private val chatUseCase: ChatUseCase
) : ViewModel() {

    var chatItemId = -1L

    private val _chatList: MutableList<Chat> = mutableListOf()
    val chatList: List<Chat> = _chatList

    private val _onChatItem: MutableLiveData<ChatItem> = MutableLiveData()
    val onChatItem = _onChatItem

    private val _onChatInserted: MutableLiveData<Chat> = MutableLiveData()
    val onChatInserted = _onChatInserted

    private var _onSelectionMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val onSelectionMode = _onSelectionMode

    fun getChatItemById(id: Long = chatItemId) {
        CoroutineScope(Dispatchers.IO).launch {
            onChatItem.postValue(chatListUseCase.getChatItemById(id))
        }
    }

    fun getAllChatByChatItemId(id: Long = chatItemId) {
        CoroutineScope(Dispatchers.IO).launch {
            chatUseCase.getAllChatByChatItemId(id).also {
                _chatList.addAll(it.reversed())
            }
        }
    }

    fun insertChat(message: String) {
        CoroutineScope(Dispatchers.IO).launch {

            Chat(message = message).also {

                it.chatItemId = chatItemId
                it.chatId = chatUseCase.insertChat(it)

                _chatList.add(0, it)
                _onChatInserted.postValue(it)
            }
        }

    }

    fun toggleSelection(chat: Chat) {
        chat.isSelected = !chat.isSelected

        val totalSelected = _chatList.count { it.isSelected }
        if(totalSelected == 0){
            exitSelectionMode()
        }
    }

    fun deleteSelectedChat() {

        _chatList.filter { it.isSelected }.also {
            chatUseCase.deleteSelectedChat(it)
        }
    }

    fun enterSelectionMode() {
        CoroutineScope(Dispatchers.Main).launch {
            _onSelectionMode.emit(true)
        }
    }

    fun exitSelectionMode() {
        CoroutineScope(Dispatchers.Main).launch {
            _onSelectionMode.emit(false)
        }
    }


}