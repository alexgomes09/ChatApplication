package com.example.chatapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.Constants
import com.example.chatapplication.R
import com.example.chatapplication.adapters.ChatListAdapters
import com.example.chatapplication.databinding.ActivityChatListBinding
import com.example.chatapplication.viewmodels.ChatListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ChatListActivity : AppCompatActivity() {

    private enum class UiState {
        EMPTY, SUCCESS
    }

    private lateinit var binding: ActivityChatListBinding
    private val chatListAdapters = ChatListAdapters()

    private val viewModel: ChatListViewModel by lazy {
        ViewModelProvider(this)[ChatListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvChatList.adapter = chatListAdapters

        setListeners()

        CoroutineScope(Dispatchers.Main).launch {

            viewModel.chatList.collect {
                if (it.isEmpty()) {
                    setUiState(UiState.EMPTY)
                } else {
                    setUiState(UiState.SUCCESS)
                    chatListAdapters.setChatList(it)
                }
            }
        }

        CoroutineScope(Dispatchers.Main).launch {

            viewModel.onSelectionMode.collect {
                invalidateOptionsMenu()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.title = getString(R.string.lbl_chat_list)

        viewModel.getAllChatList()
    }

    private fun setListeners() {

        chatListAdapters.setItemClickListener { pos, chatItem ->
            if (viewModel.onSelectionMode.value) {
                viewModel.toggleSelection(chatItem)
                chatListAdapters.notifyItemChanged(pos)
                return@setItemClickListener
            }
            Intent(this, ChatActivity::class.java).also {
                it.putExtra(Constants.CHAT_ITEM_ID, chatItem.chatItemId)
                startActivity(it)
            }
        }

        chatListAdapters.setItemSelectedListener { pos, chatItem ->
            if (viewModel.onSelectionMode.value.not()) {
                viewModel.enterSelectionMode()
            }
            viewModel.toggleSelection(chatItem)
            chatListAdapters.notifyItemChanged(pos)
        }


        chatListAdapters.setItemSelectedListener { pos, chatItem ->
            if (viewModel.onSelectionMode.value.not()) {
                viewModel.enterSelectionMode()
            }
            viewModel.toggleSelection(chatItem)
            chatListAdapters.notifyItemChanged(pos)
        }

        binding.btnCreateChat.setOnClickListener {
            gotoNewChatScreen()
        }

        binding.fabCreateChat.setOnClickListener {
            gotoNewChatScreen()
        }
    }

    private fun setUiState(uiState: UiState) {
        when (uiState) {
            UiState.EMPTY -> {
                binding.lblCreateChatNow.visibility = View.VISIBLE
                binding.btnCreateChat.visibility = View.VISIBLE
                binding.rvChatList.visibility = View.GONE
            }

            UiState.SUCCESS -> {
                binding.lblCreateChatNow.visibility = View.GONE
                binding.btnCreateChat.visibility = View.GONE
                binding.rvChatList.visibility = View.VISIBLE
            }
        }
    }

    private fun gotoNewChatScreen() {
        Intent(this, NewChatActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_item_delete) {

            AlertDialog.Builder(this).also {
                it.setTitle("Delete ChatItem?")
                    .setPositiveButton("Yes") { dialog, id ->
                        viewModel.deleteSelectedChatList()
                        viewModel.exitSelectionMode()
                        chatListAdapters.notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                it.create().show()
            }

            true
        } else if (id == R.id.action_item_mute_unmute) {
            AlertDialog.Builder(this).also {
                it.setTitle("Mute/Unmute Chat Item?")
                    .setPositiveButton("Yes") { dialog, id ->
                        viewModel.toggleMuteChatItem()
                        chatListAdapters.notifyDataSetChanged()
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

        viewModel.onSelectionMode.value.also { selected ->
            menu?.findItem(R.id.action_item_delete)?.let {
                it.isVisible = selected
            }

            menu?.findItem(R.id.action_item_mute_unmute)?.let {
                it.isVisible = selected
            }
        }

        return true
    }
}
