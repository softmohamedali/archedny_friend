package com.example.archedny_app_friend.future_chat.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.databinding.FragmentFriendsChatsScreenBinding
import com.example.archedny_app_friend.future_chat.domain.models.Friend
import com.example.archedny_app_friend.future_chat.presentation.adapters.FriendsChatAdapter
import com.example.archedny_app_friend.future_chat.presentation.viewmodels.FriendChatsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendsChatsScreenFragment : Fragment() {
    private var _binding: FragmentFriendsChatsScreenBinding?=null
    private val binding get() = _binding!!
    private val friendsItemAdapter by lazy { FriendsChatAdapter() }

    private val chatsFriendsViewModel by viewModels<FriendChatsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentFriendsChatsScreenBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp() {
        setUpView()
        setUpViewAction()
        setUpObservers()
    }

    private fun setUpView() {
        setUpRecyclerView()
    }

    private fun setUpViewAction() {
        friendsItemAdapter.setOnItemClickListener {
            findNavController().navigate(
                FriendsChatsScreenFragmentDirections
                    .actionFriendsChatsScreenFragmentToChatFragment(it)
            )
        }
        friendsItemAdapter.setOnFriendImageClickListener {

        }
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.fabChatsSearch.setOnClickListener {
            findNavController().navigate(R.id.action_friendsChatsScreenFragment_to_searshFragment)
        }
    }

    private fun setUpObservers() {

    }

    fun setUpRecyclerView(){
        var data= mutableListOf<Friend>()
        data.add(Friend())
        data.add(Friend())
        data.add(Friend())
        data.add(Friend())
        data.add(Friend())
        data.add(Friend())
        friendsItemAdapter.setData(data)
        binding.recyFriendsChats.apply {
            layoutManager=LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
            adapter=friendsItemAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}