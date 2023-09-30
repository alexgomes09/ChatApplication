package com.example.chatapplication

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.dao.ChatItemDao
import com.example.data.db.ChatDatabase
import com.example.data.model.ChatItem
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ChatListTest {

    private lateinit var db: ChatDatabase
    private lateinit var dao: ChatItemDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ChatDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = db.chatListDao()

    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertChatItem(): Unit = runBlocking {

        //Given
        ChatItem(chatName = "Test123").also {

            //When
            val chatItemId = dao.insert(it)
            val chatItem = dao.getChatItemById(chatItemId)

            //Then
            assert(chatItemId != -1L)
            assert(chatItem.chatItemId == chatItemId)
        }
    }

    @Test
    fun getAllChatItem() = runBlocking {

        //Given
        val chatItem1 = ChatItem(chatName = "Test1!", lastMessage = "lastMessage1", lastMessageTimeStamp = 134112213)
        dao.insert(chatItem1)

        val chatItem2 = ChatItem(chatName = "Hello world!", lastMessage = "lastMessage2", lastMessageTimeStamp = 1246112463)
        dao.insert(chatItem2)

        //When
        val output = dao.getAllChatList().first()

        //Then
        assert(output.size == 2)
    }


    @Test
    fun isChatItemExist() = runBlocking {

        //Given
        val chatItem = ChatItem(chatName = "Test1")
        dao.insert(chatItem)

        //When
        val output = dao.queryChatExistence("Test1")


        //Then
        assert(output != null)
        assert(output?.chatName.equals("Test1"))
    }

    @Test
    fun deleteChatItemByIds() = runBlocking {

        //Given
        val chatItemId1 = dao.insert(ChatItem(chatName = "Test1"))
        val chatItemId2 = dao.insert(ChatItem(chatName = "Test2"))

        //When
        dao.deleteItemByIds(listOf(chatItemId1))


        //Then
        val output = dao.getAllChatList().first()

        assert(output.map { it.chatItemId }.contains(chatItemId1).not())
        assert(output.size == 1)
    }

    @Test
    fun updateChatItem() = runBlocking {

        //Given
        val chatItemId1 = dao.insert(ChatItem(chatName = "Test1", isChatMuted = false))

        //When
        dao.updateMuteStatus(listOf(chatItemId1))

        //Then
        val output = dao.getAllChatList().first()
        assert(output.first().isChatMuted)
    }
}