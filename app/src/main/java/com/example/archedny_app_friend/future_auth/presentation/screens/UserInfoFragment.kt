package com.example.archedny_app_friend.future_auth.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.archedny_app_friend.databinding.FragmentUserInfoBinding
import com.example.archedny_app_friend.future_auth.presentation.viewmodels.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment : Fragment() {
    private var _binding: FragmentUserInfoBinding?=null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentUserInfoBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp() {
        setUpView()
        setUpViewAction()
        setUpObservers()
    }

    private fun setUpView() {
        lifecycleScope.launchWhenStarted {

        }
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