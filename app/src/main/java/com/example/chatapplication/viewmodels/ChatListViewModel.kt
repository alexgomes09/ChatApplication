package com.example.chatapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.example.data.model.ChatItem
import com.example.domain.usecases.ChatListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val chatListUseCase: ChatListUseCase
) : ViewModel() {

    private var _chatItem: MutableStateFlow<List<ChatItem>> = MutableStateFlow(emptyList())
    val chatList = _chatItem

    private var _onSelectionMode: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val onSelectionMode = _onSelectionMode


    init {
        getAllChatList()
    }

    fun getAllChatList() {
        CoroutineScope(Dispatchers.IO).launch {
            chatListUseCase.getAllChatItem().collect {
                _chatItem.emit(it)
            }
        }
    }

    fun deleteSelectedChatList() {
        CoroutineScope(Dispatchers.IO).launch {
            _chatItem.collect {

                val ids = it
                    .filter { it.isSelected }
                    .map { it.chatItemId }

                chatListUseCase.deleteSelectedChatList(ids)
            }
        }
    }

    fun toggleMuteChatItem() {
        CoroutineScope(Dispatchers.IO).launch {
            _chatItem.collect {

                val ids = it
                    .filter { it.isSelected }
                    .map { it.chatItemId }

                chatListUseCase.toggleMuteChatItem(ids)
            }
        }
    }

    fun toggleSelection(chatItem: ChatItem) {
        chatItem.isSelected = !chatItem.isSelected

        CoroutineScope(Dispatchers.Main).launch {
            _chatItem.collect {
                val totalSelected = it.count { it.isSelected }
                if (totalSelected == 0) {
                    exitSelectionMode()
                }
            }
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