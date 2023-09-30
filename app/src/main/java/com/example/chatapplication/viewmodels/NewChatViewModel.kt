package com.example.chatapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domain.usecases.ChatListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewChatViewModel @Inject constructor(
    private val chatListUseCase: ChatListUseCase
) : ViewModel() {

    private val _chatExistObserver: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val chatExistObserver = _chatExistObserver

    private val _chatInsertedObserver: MutableSharedFlow<Unit> = MutableSharedFlow()
    val chatInsertedObserver = _chatInsertedObserver


    fun queryChatExistence(input: String) {
        CoroutineScope(Dispatchers.IO).launch {
            if (chatListUseCase.queryChatExistence(input) == null) {
                createNewChatItem(input)
            } else {
                _chatExistObserver.emit(true)
            }
        }
    }

    private fun createNewChatItem(input: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val retCode = chatListUseCase.insertChatItem(input)

            if(retCode != -1L){
                _chatInsertedObserver.emit(Unit)
            }
        }
    }

}