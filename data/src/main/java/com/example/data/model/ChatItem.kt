package com.example.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "chat_list")
data class ChatItem(
    @PrimaryKey(autoGenerate = true)
    var chatItemId: Long = 0,
    var chatName: String = "",
    var isChatMuted: Boolean = false,
    @Ignore var isSelected: Boolean = false,
    var lastMessage: String? = "",
    var lastMessageTimeStamp: Long? = 0L
) {

}

