package com.example.chatapplication.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.Constants
import com.example.chatapplication.R
import com.example.chatapplication.adapters.ChatAdapter
import com.example.chatapplication.databinding.ActivityChatBinding
import com.example.chatapplication.viewmodels.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private val chatAdapter = ChatAdapter()

    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.CHAT_ITEM_ID)) {
            viewModel.chatItemId = intent.getLongExtra(Constants.CHAT_ITEM_ID, -1)
            viewModel.getChatItemById()
            viewModel.getAllChatByChatItemId()
        } else {
            Toast.makeText(this, "Invalid chat", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.rvChat.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        binding.rvChat.adapter = chatAdapter

        chatAdapter.setChat(viewModel.chatList)

        viewModel.onChatItem.observe(this) {
            supportActionBar?.title = it.chatName
        }

        viewModel.onChatInserted.observe(this) {
            binding.etChat.text?.clear()
            chatAdapter.notifyItemInserted(0)
            binding.rvChat.scrollToPosition(0)
        }

        CoroutineScope(Dispatchers.Main).launch {

            viewModel.onSelectionMode.collect {
                invalidateOptionsMenu()
            }
        }

        setListeners()
    }

    private fun setListeners() {
        binding.btnSend.setOnClickListener {
            val text = binding.etChat.text.toString()

            if (text.isEmpty()) {
                return@setOnClickListener
            }

            viewModel.insertChat(text)
        }

        chatAdapter.setItemClickListener { pos, chat ->
            if (viewModel.onSelectionMode.value) {
                viewModel.toggleSelection(chat)
                chatAdapter.notifyItemChanged(pos)
            }
        }

        chatAdapter.setItemSelectedListener { pos, chat ->

            if (viewModel.onSelectionMode.value.not()) {
                viewModel.enterSelectionMode()
            }
            viewModel.toggleSelection(chat)
            chatAdapter.notifyItemChanged(pos)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_item_delete) {

            AlertDialog.Builder(this).also {
                it.setTitle("Delete messages?")
                    .setPositiveButton("Yes") { dialog, id ->
                        viewModel.deleteSelectedChat()
                        viewModel.exitSelectionMode()
                        chatAdapter.notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                it.create().show()
            }

            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)

        menu?.findItem(R.id.action_item_delete)?.let {
            it.isVisible = viewModel.onSelectionMode.value
        }
        return true
    }

}