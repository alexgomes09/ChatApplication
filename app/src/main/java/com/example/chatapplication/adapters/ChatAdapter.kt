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
import com.example.chatapplication.databinding.ItemChatBinding
import com.example.chatapplication.formatDate
import com.example.data.model.Chat


class ChatAdapter() : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private lateinit var itemSelectedListener: ((position: Int, chat: Chat) -> Unit)
    private lateinit var itemClickListener: ((position: Int, chat: Chat) -> Unit)

    private var chats: List<Chat> = emptyList()
    private lateinit var context: Context

    fun setChat(list: List<Chat>) {
        this.chats = list
        notifyDataSetChanged()
    }

    fun setItemClickListener(listener: ((position: Int, chat: Chat) -> Unit)) {
        itemClickListener = listener
    }

    fun setItemSelectedListener(listener: ((position: Int, chat: Chat) -> Unit)) {
        itemSelectedListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        context = parent.context
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        if (position == RecyclerView.NO_POSITION) {
            return
        }

        val chat = getItem(position)
        holder.bind(chat)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addItemDecoration(ItemSpaceDecoration(10))
    }

    private fun getItem(position: Int) = chats[position]

    override fun getItemCount(): Int = chats.size

    inner class ChatViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (getItem(adapterPosition).isSelected) {
                    return@setOnClickListener
                }

                itemClickListener.invoke(adapterPosition, getItem(adapterPosition))
            }

            binding.root.setOnLongClickListener {
                if (getItem(adapterPosition).isDeleted) {
                    return@setOnLongClickListener false
                }

                itemSelectedListener.invoke(adapterPosition, getItem(adapterPosition))
                true
            }
        }

        fun bind(chat: Chat) {

            binding.tvChatTimestamp.text = formatDate(chat.sentTimeStamp)

            if (chat.isDeleted) {
                binding.tvChatTimestamp.visibility = View.GONE
                binding.tvChat.text = context.getString(R.string.message_deleted)
                binding.tvChat.setTextColor(ContextCompat.getColor(context,R.color.deleted))
            } else {
                binding.tvChatTimestamp.visibility = View.VISIBLE
                binding.tvChat.text = chat.message
                binding.tvChat.setTextColor(Color.BLACK)
            }

            if (chat.isSelected) {
                binding.root.setCardBackgroundColor(ContextCompat.getColor(context, R.color.selected))
            } else {
                binding.root.setCardBackgroundColor(Color.WHITE)
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