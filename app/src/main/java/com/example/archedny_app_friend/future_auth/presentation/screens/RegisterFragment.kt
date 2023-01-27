package com.example.archedny_app_friend.future_auth.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.archedny_app_friend.databinding.FragmentRegisterBinding
import com.example.archedny_app_friend.future_auth.presentation.viewmodels.AuthViewModel
import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.core.domain.utils.myextension.out
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding?=null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private var mPhone:String?=null
    private var mCode:String?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentRegisterBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp() {
        setUpView()
        setUpViewAction()
        setUpObservers()
    }

    private fun setUpView() {
        binding.countrycodeSpinnerRegister.setCountryForNameCode("eg")
    }
    private fun setUpViewAction() {

        binding.registerButtonRegister.setOnClickListener {
            mPhone=binding.phoneEditTextRegister.text.toString()
            mCode=binding.countrycodeSpinnerRegister.selectedCountryCode
            out("phone${mPhone} code${mCode}")
            authViewModel.registerVerifiyWithPhone(
                phone ="+${mCode}${mPhone}",
                activity = requireActivity()
            )
        }
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenStarted {
            authViewModel.isVerifiying.collect{
                when(it){
                    is ResultState.IsLoading ->{
                        binding.progressRegister.isVisible=true

                    }
                    is ResultState.IsError ->{
                        binding.progressRegister.isVisible=false
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        out(it.message.toString())
                    }
                    is ResultState.IsSucsses ->{
                        binding.progressRegister.isVisible=false
                        val actionGoToVerification=RegisterFragmentDirections
                            .actionRegisterFragmentToVerificationPhoneFragment(
                                phone = "+"+mCode+mPhone!!,
                                verificationId = it.data!!
                            )
                        findNavController().navigate(actionGoToVerification)
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}