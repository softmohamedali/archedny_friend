package com.example.archedny_app_friend.presentation.body.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.databinding.FragmentDialogSendTrackingRequestBinding
import com.example.archedny_app_friend.databinding.FragmentSearshBinding
import com.example.archedny_app_friend.presentation.body.adapters.PhoneItemAdapter
import com.example.archedny_app_friend.presentation.body.viewmodels.SearshViewModel
import com.example.archedny_app_friend.utils.ResultState
import com.example.archedny_app_friend.utils.myextention.ToastType
import com.example.archedny_app_friend.utils.myextention.toastSuccessBooking
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DialogSendTrackingRequestFragment : Fragment() {
    private var _binding: FragmentDialogSendTrackingRequestBinding?=null
    private val binding get() = _binding!!
//    private val searchViewModel by viewModels<SearshViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentDialogSendTrackingRequestBinding.inflate(layoutInflater)
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