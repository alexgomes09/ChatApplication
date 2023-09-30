package com.example.chatapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityNewChatBinding
import com.example.chatapplication.viewmodels.NewChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewChatActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[NewChatViewModel::class.java]
    }
    private lateinit var binding: ActivityNewChatBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewChatBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setListeners()

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.chatExistObserver.collect {
                binding.textField.error = "Chat name already exist. Try another one"
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            viewModel.chatInsertedObserver.collect {
                Toast.makeText(this@NewChatActivity, "New chat created", Toast.LENGTH_SHORT).show()
                binding.textField.editText?.text?.clear()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.title = getString(R.string.create_new_chat)
    }


    private fun setListeners() {
        binding.btnCreateChat.setOnClickListener {

            val input = binding.textInput.text.toString()

            if (input.isEmpty()) {
                binding.textField.error = "Please enter a chat name"
            } else {
                viewModel.queryChatExistence(input)
            }
        }

        binding.textInput.addTextChangedListener {
            binding.textField.error = null
        }
    }

}