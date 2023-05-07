package com.example.archedny_app_friend.future_chat.presentation.screens.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.databinding.FragmentChatBinding
import com.example.archedny_app_friend.databinding.FragmentFriendsChatsScreenBinding

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding?=null
    private val binding get() = _binding!!
    //    private val pohoneItemAdapter by lazy { PhoneItemAdapter() }
//
//    private val searchViewModel by viewModels<SearshViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentChatBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp(){
        setupChatRecyclerView()
    }

    private fun setupChatRecyclerView() {

    }


}