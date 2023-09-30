package com.example.chatapplication.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ItemChatListBinding
import com.example.chatapplication.formatDate
import com.example.data.model.ChatItem


class ChatListAdapters() : RecyclerView.Adapter<ChatListAdapters.ChatListViewHolder>() {

    private lateinit var itemSelectedListener: ((position: Int, chatItem: ChatItem) -> Unit)
    private lateinit var itemClickListener: ((position: Int, chatItem: ChatItem) -> Unit)

    private var chatItem: List<ChatItem> = listOf()
    private lateinit var context: Context

    fun setChatList(list: List<ChatItem>) {
        this.chatItem = list
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: ((position: Int, chatItem: ChatItem) -> Unit)) {
        itemClickListener = listener
    }

    fun setItemSelectedListener(listener: ((position: Int, chatItem: ChatItem) -> Unit)) {
        itemSelectedListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        context = parent.context
        val binding = ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {

        if (position == RecyclerView.NO_POSITION) {
            return
        }

        val chatItem = getItem(position)
        holder.bind(chatItem)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(ItemSpaceDecoration(10))
    }

    private fun getItem(position: Int) = chatItem[position]

    override fun getItemCount(): Int = chatItem.size

    inner class ChatListViewHolder(private val binding: ItemChatListBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                itemClickListener.invoke(adapterPosition, getItem(adapterPosition))
            }

            binding.root.setOnLongClickListener {
                itemSelectedListener.invoke(adapterPosition, getItem(adapterPosition))
                true
            }
        }

        fun bind(chatItem: ChatItem) {

            binding.tvChatListName.text = chatItem.chatName
            binding.tvLatestMsg.text = chatItem.lastMessage

            if (chatItem.lastMessageTimeStamp == null) {
                binding.tvMsgTimestamp.visibility = View.INVISIBLE
            } else {
                binding.tvMsgTimestamp.visibility = View.VISIBLE
                binding.tvMsgTimestamp.text = formatDate(chatItem.lastMessageTimeStamp ?: 0L)
            }

            if (chatItem.isSelected) {
                binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.selected))
            } else {
                binding.root.setCardBackgroundColor(Color.WHITE)
            }

            if (chatItem.isChatMuted) {
                binding.ivMute.visibility = View.VISIBLE
            } else {
                binding.ivMute.visibility = View.GONE
            }
        }
    }

    inner class ItemSpaceDecoration(private val itemSpace: Int) : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.top = itemSpace / 2
            outRect.bottom = itemSpace / 2
        }
    }
}