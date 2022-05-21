package com.example.archedny_app_friend.presentation.body.screens

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.archedny_app_friend.databinding.FragmentDialogSendTrackingRequestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogSendTrackingRequestFragment : DialogFragment() {
    private var _binding: FragmentDialogSendTrackingRequestBinding?=null
    private val binding get() = _binding!!
//    private val searchViewModel by viewModels<SearshViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentDialogSendTrackingRequestBinding.inflate(layoutInflater)
        setUp()
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
        binding.btnCancle.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpObservers() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}