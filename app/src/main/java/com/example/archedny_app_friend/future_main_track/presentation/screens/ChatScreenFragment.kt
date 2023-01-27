package com.example.archedny_app_friend.future_main_track.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.archedny_app_friend.databinding.FragmentChatScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatScreenFragment : Fragment() {
    private var _binding: FragmentChatScreenBinding?=null
    private val binding get() = _binding!!
//    private val pohoneItemAdapter by lazy { PhoneItemAdapter() }
//
//    private val searchViewModel by viewModels<SearshViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentChatScreenBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp() {
        setUpView()
        setUpViewAction()
        setUpObservers()
    }

    private fun setUpView() {

    }

    private fun setUpViewAction() {

    }

    private fun setUpObservers() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}