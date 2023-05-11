package com.example.archedny_app_friend.future_chat.presentation.screens.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.archedny_app_friend.databinding.FragmentChatBinding
import com.example.archedny_app_friend.future_auth.presentation.screens.VerificationPhoneFragmentArgs
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage
import com.example.archedny_app_friend.future_chat.presentation.screens.chat.adapters.MessagesAdapters
import java.util.Date

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding?=null
    private val binding get() = _binding!!
    private val messagesAdapter by lazy { MessagesAdapters() }
    private val searchViewModel by viewModels<ChatViewModel>()
    private lateinit var chatChannelId:String
    private val navArgs by navArgs<ChatFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentChatBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp(){
        screenVariable()
        setupChatRecyclerView()
        setUpViewAction()
    }

    private fun screenVariable() {
        chatChannelId=navArgs.chatChanelId
    }

    private fun setUpViewAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imgRecordVoice.setOnClickListener {

        }
        binding.imgSendText.setOnClickListener {
            sendMessage()
        }

    }

    private fun sendMessage() {
        val messageText=binding.etMessageText.text.toString()
        val message = TextMassage(
            msg=messageText,
            date=Date().time.toString(),
            senderId="",
            receiverId="",
            chatChanneId = chatChannelId
        )
        searchViewModel.sendMessage(textMessage = message)
    }

    private fun setupChatRecyclerView() {

    }


}