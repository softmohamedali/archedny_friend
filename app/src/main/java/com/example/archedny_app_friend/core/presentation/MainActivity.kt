package com.example.archedny_app_friend.core.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.databinding.ActivityMainBinding
import com.example.archedny_app_friend.future_auth.presentation.viewmodels.AuthViewModel
import com.example.archedny_app_friend.future_main_track.presentation.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

//cheak Permission utility

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),NavController.OnDestinationChangedListener {

    companion object{
        private var _binding:ActivityMainBinding?=null
        private val binding get() = _binding!!

        fun openNavDraw()
        {
            binding.drwer.open()
        }

        fun closeNavDraw()
        {
            binding.drwer.close()
        }
    }

    private lateinit var navController:NavController
    private val  authViewModel: AuthViewModel by viewModels()
    private val  sharedViewModel: SharedViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
    }
    private fun setUp() {
        navController=findNavController(R.id.fragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)
        setUpNavDrawer()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launchWhenStarted {
            sharedViewModel.isDarkTheme.collect {
                if (it){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            sharedViewModel.lang.collect {
                changeLang(it)
            }
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        when(destination.id){
            R.id.splashFragment, R.id.homeMapFragment2,
            R.id.registerFragment, R.id.verificationPhoneFragment ->{
                    binding.bottomNavigationView.visibility=View.GONE
                }
            else ->{
                binding.bottomNavigationView.visibility=View.VISIBLE
            }
        }
    }

    private fun setUpNavDrawer() {
        val actionToogle= ActionBarDrawerToggle(
            this,
            binding.drwer,
            R.string.open,
            R.string.close
        )
        binding.drwer.addDrawerListener(actionToogle)
        binding.navViewDrawer.bringToFront()
        binding.navViewDrawer.requestLayout()
        binding.navViewDrawer.setNavigationItemSelectedListener{
            when(it.itemId)
            {
                R.id. drwer_item_logout-> {
                    authViewModel.logOut()
                    navController.navigate(R.id.global_go_to_login)
                    closeNavDraw()
                }
                R.id.drwer_item_settingui->{
                    navController.navigate(R.id.action_homeMapFragment2_to_settingFragment)
                    closeNavDraw()
                }
                else ->{

                }
            }
            true
        }
    }


    private fun changeLang(localcode:String)
    {
        val local= Locale(localcode)
        Locale.setDefault(local)
        val resourses=this.resources
        val config=resourses.configuration
        config.setLocale(local)
        resourses.updateConfiguration(config,resourses.displayMetrics)
    }




    override fun onBackPressed() {
        if (binding.drwer.isOpen)
        {
            closeNavDraw()
        }else{
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding =null
    }

}