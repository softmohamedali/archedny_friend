package com.example.archedny_app_friend.presentation.auth.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.databinding.FragmentSplashBinding
import com.example.archedny_app_friend.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding?=null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSplashBinding.inflate(layoutInflater)
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
            delay(2000)
            if (authViewModel.getUser()!=null){
                findNavController().navigate(R.id.action_splashFragment_to_homeMapFragment2)
            }else{
                findNavController().navigate(R.id.action_splashFragment_to_registerFragment)
            }
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