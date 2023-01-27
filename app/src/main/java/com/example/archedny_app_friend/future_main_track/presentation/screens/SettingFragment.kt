package com.example.archedny_app_friend.future_main_track.presentation.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.archedny_app_friend.databinding.FragmentSettingBinding
import com.example.archedny_app_friend.future_main_track.presentation.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding?=null
    private val binding get() = _binding!!

    private val sharedViewModel by viewModels<SharedViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentSettingBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp() {
        setUpViewAction()
        setUpObservers()
    }

    private fun setUpObservers() {
        observeSetting()
        observeLang()
    }

    private fun observeSetting() {
        lifecycleScope.launchWhenStarted {
            sharedViewModel.isExtendMap.collect {
                binding.swExtendMap.isChecked=it
            }
        }
        lifecycleScope.launchWhenStarted {
            sharedViewModel.isDarkTheme.collect {
                binding.swDarkThem.isChecked=it
            }
        }
    }

    private fun observeLang(){
        lifecycleScope.launchWhenStarted {
            sharedViewModel.lang.collect {
                changeLang(it)
            }
        }
    }

    private fun setUpViewAction() {
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.swArabicLang.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
            {
                changeLang("ar")
                sharedViewModel.saveLang("ar")
            }else{
                changeLang("en")
                sharedViewModel.saveLang("en")
            }
        }
        binding.swDarkThem.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked)
            {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedViewModel.saveIsDarkTheme(true)
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedViewModel.saveIsDarkTheme(false)
            }
        }
        binding.swExtendMap.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                sharedViewModel.saveIsExtendMap(true)
            }else{
                sharedViewModel.saveIsExtendMap(false)
            }
        }
    }

    private fun changeLang(localcode:String)
    {
        val local= Locale(localcode)
        Locale.setDefault(local)
        val resourses=requireActivity().resources
        val config=resourses.configuration
        config.setLocale(local)
        resourses.updateConfiguration(config,resourses.displayMetrics)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}