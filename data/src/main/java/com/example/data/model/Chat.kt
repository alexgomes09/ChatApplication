package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(
    tableName = "chat",
    foreignKeys = [ForeignKey(
        entity = ChatItem::class,
        parentColumns = ["chatItemId"],
        childColumns = ["chatItemId"],
        onDelete = CASCADE)])
data class Chat(
    @PrimaryKey(autoGenerate = true) var chatId: Long = 0,
    var message: String = "",
    var isDeleted: Boolean = false,
    @Ignore var isSelected: Boolean = false,
    var sentTimeStamp: Long = System.currentTimeMillis(),
    var chatItemId: Long = 0,
)