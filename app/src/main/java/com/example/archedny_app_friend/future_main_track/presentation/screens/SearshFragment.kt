package com.example.archedny_app_friend.future_main_track.presentation.screens

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
import com.example.archedny_app_friend.databinding.FragmentSearshBinding
import com.example.archedny_app_friend.future_main_track.presentation.adapters.PhoneItemAdapter
import com.example.archedny_app_friend.future_main_track.presentation.viewmodels.SearshViewModel
import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.core.domain.utils.myextension.ToastType
import com.example.archedny_app_friend.core.domain.utils.myextension.coustomToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearshFragment : Fragment() {
    private var _binding: FragmentSearshBinding?=null
    private val binding get() = _binding!!
    private val pohoneItemAdapter by lazy { PhoneItemAdapter() }

    private val searchViewModel by viewModels<SearshViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSearshBinding.inflate(layoutInflater)
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
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        pohoneItemAdapter.setOnItemClickListener {

        }
        pohoneItemAdapter.setOnAddFriendClickListener { frienIs->
            searchViewModel.addFreind(frienIs)
        }
        binding.etSearch.doOnTextChanged { text, start, before, count ->
            searchViewModel.searchPhone(text.toString())

        }
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenStarted {
            searchViewModel.phones.collect{
                when{
                    it is ResultState.IsLoading ->{
                        binding.tvHintMessage.isVisible=false
                    }
                    it is ResultState.IsSucsses ->{
                            binding.tvHintMessage.isVisible=false
                            val phones=it.data
                            pohoneItemAdapter.setData(phones as MutableList)
                    }
                    it is ResultState.IsError ->{
                        binding.tvHintMessage.isVisible=true
                        binding.tvHintMessage.text=it.message
                        pohoneItemAdapter.setData(mutableListOf())
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            searchViewModel.isAddFriend.collect{
                when{
                    it is ResultState.IsLoading ->{}
                    it is ResultState.IsSucsses ->{
                        coustomToast(
                            requireContext(),
                            "Operation Succes",
                            ToastType.SUCCESS
                        )
                    }

                    it is ResultState.IsError ->{
                        coustomToast(
                            requireContext(),
                            "Error try Again",
                            ToastType.ERROR
                        )
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyPhones.apply {
            layoutManager=LinearLayoutManager(requireActivity(),RecyclerView.VERTICAL,false)
            adapter=pohoneItemAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}