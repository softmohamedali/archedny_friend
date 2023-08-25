package com.example.archedny_app_friend.future_chat.presentation.screens.chat

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.asynctaskcoffee.audiorecorder.worker.AudioRecordListener
import com.example.archedny_app_friend.core.domain.utils.myextension.mylog
import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.databinding.FragmentChatBinding
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage
import com.example.archedny_app_friend.future_chat.presentation.screens.chat.adapters.MessagesAdapters
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class ChatFragment : Fragment(), AudioRecordListener {

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
        setViews()
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
                        mylog("my massages size${(it.data as MutableList).size}",this@ChatFragment)
                        messagesAdapter.setData(it.data)
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
            if (!binding.etMessageText.text.isNullOrEmpty()){
                sendMessage()
            }
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
    private var permissionToRecordAccepted = false
    private var permissionCode = 200
    private var permissionsRequired = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    private fun setViews() {
        binding.recordButton.audioRecordListener = this
        if (letsCheckPermissions()) {
            binding.recordButton.setRecordListener()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), permissionsRequired, permissionCode)
        }
    }

    override fun onAudioReady(audioUri: String?) {
        Toast.makeText(requireContext(), audioUri, Toast.LENGTH_SHORT).show()
    }

    override fun onReadyForRecord() {

    }

    override fun onRecordFailed(errorMessage: String?) {
        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun letsCheckPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode) {
            permissionToRecordAccepted =
                (grantResults[0] == PackageManager.PERMISSION_GRANTED) && ((grantResults[1] == PackageManager.PERMISSION_GRANTED))
            if (permissionToRecordAccepted) binding.recordButton.setRecordListener()
        }
        if (!permissionToRecordAccepted) Toast.makeText(
            requireContext(),
            "You have to accept permissions to send voice",
            Toast.LENGTH_SHORT
        ).show()
    }



}