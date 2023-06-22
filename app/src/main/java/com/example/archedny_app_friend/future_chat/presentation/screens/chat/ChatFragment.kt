package com.example.archedny_app_friend.future_chat.presentation.screens.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.databinding.FragmentChatBinding
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage
import com.example.archedny_app_friend.future_chat.presentation.screens.chat.adapters.MessagesAdapters
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding?=null
    private val binding get() = _binding!!
    private val messagesAdapter by lazy { MessagesAdapters() }
    private val chatViewModel by viewModels<ChatViewModel>()
    private lateinit var friendId:String
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
        setUpObservables()
        setUpViewAction()
    }

    private fun setUpObservables() {
        lifecycleScope.launchWhenStarted {
            chatViewModel.chatContent.collect{
                when{
                    it is ResultState.IsLoading ->{
//                        binding.pbFriendsChats.visibility=View.VISIBLE
//                        binding.tvError.visibility=View.GONE
                    }
                    it is ResultState.IsSucsses ->{
//                        binding.pbFriendsChats.visibility=View.GONE
//                        binding.tvError.visibility=View.GONE
                        messagesAdapter.setData(it.data as MutableList)
                    }
                    it is ResultState.IsError ->{
//                        binding.pbFriendsChats.visibility=View.GONE
//                        binding.tvError.visibility=View.VISIBLE
//                        binding.tvError.text=it.message
                        messagesAdapter.setData(mutableListOf())
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            chatViewModel.chatChannelId.collect{
                when{
                    it is ResultState.IsLoading ->{
//                        binding.pbFriendsChats.visibility=View.VISIBLE
//                        binding.tvError.visibility=View.GONE
                    }
                    it is ResultState.IsSucsses ->{
//                        binding.pbFriendsChats.visibility=View.GONE
//                        binding.tvError.visibility=View.GONE
//                        chatViewModel.getChatContent(chatChannelId = it.data!!)
                        chatViewModel.getChatContent(it.data!!)
                        chatChannelId=it.data
                    }
                    it is ResultState.IsError ->{
//                        binding.pbFriendsChats.visibility=View.GONE
//                        binding.tvError.visibility=View.VISIBLE
//                        binding.tvError.text=it.message
                        messagesAdapter.setData(mutableListOf())
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            chatViewModel.isSendTheMessage.collect{
                when{
                    it is ResultState.IsLoading ->{
                    }
                    it is ResultState.IsSucsses ->{
                        binding.etMessageText.setText("")
                    }
                    it is ResultState.IsError ->{
                        binding.etMessageText.setText("")
                        Toast.makeText(requireContext(),"error : ${it.message}",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun screenVariable() {
        friendId=navArgs.friendId
        Log.d("moali in chatfrag","friend id${friendId}")
        messagesAdapter.currentUserId=friendId
        chatViewModel.getChatChannelId(friendId = friendId)
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
            receiverId="",
            chatChanneId = chatChannelId
        )
        chatViewModel.sendMessage(textMessage = message)
    }

    private fun setupChatRecyclerView() {
        binding.recyMessages.apply {
            adapter=messagesAdapter
        }

    }


}