package com.example.chatapplication

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.dao.ChatDao
import com.example.data.dao.ChatItemDao
import com.example.data.db.ChatDatabase
import com.example.data.model.Chat
import com.example.data.model.ChatItem
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatTest {

    private lateinit var db: ChatDatabase
    private lateinit var chatDao: ChatDao
    private lateinit var chatItemDao: ChatItemDao
    private var chatItemId: Long = -1L

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ChatDatabase::class.java
        ).allowMainThreadQueries().build()

        chatDao = db.chatDao()
        chatItemDao = db.chatListDao()

        chatItemId = chatItemDao.insert(ChatItem(chatName = "ChatItem!"))
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertChat(): Unit = runBlocking {

        //Given
        Chat(message = "Hello World!", chatItemId = chatItemId).also {

            //When
            val chatId = chatDao.insert(it)
            val chatItem = chatDao.getChatById(chatId)

            //Then
            assert(chatItem != null)
            assert(chatItem?.message == "Hello World!")
        }
    }

    @Test
    fun getAllChatItem() = runBlocking {

        //Given
        repeat(3) {
            Chat(message = "Message $it", chatItemId = chatItemId).also { chat ->
                chatDao.insert(chat)
            }
        }

        //When
        val output = chatDao.getAllChatByChatItemId(chatItemId)

        //Then
        assert(output.size == 3)
        output.forEach {
            assert(it.chatItemId == chatItemId)
        }
    }

    @Test
    fun deleteSelectedChat() = runBlocking {

        val chatIds = mutableListOf<Long>()

        //Given
        repeat(5) {
            Chat(message = "Message $it", chatItemId = chatItemId).also { chat ->
                chatIds.add(chatDao.insert(chat))
            }
        }

        //When
        chatDao.deleteSelectedChat(chatIds)

        //Then
        val output = chatDao.getAllChatByChatItemId(chatItemId)

        output.forEach {
            assert(it.isDeleted)
        }
    }
}