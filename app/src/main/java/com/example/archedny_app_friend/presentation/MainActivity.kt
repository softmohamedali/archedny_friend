package com.example.archedny_app_friend.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.MotionEventCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.databinding.ActivityMainBinding
import com.example.archedny_app_friend.utils.myextention.toast
import com.example.archedny_app_friend.utils.out
import dagger.hilt.android.AndroidEntryPoint

//cheak Permission utility

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),NavController.OnDestinationChangedListener {
    private var _binding:ActivityMainBinding?=null
    private val binding get() = _binding!!
    private lateinit var navController:NavController
    private lateinit var drwarLayout:DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUp()
    }
    private fun setUp() {
        navController=findNavController(R.id.fragmentContainerView)
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)
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
//                    authViewModel.logOut()
                    navController.navigate(R.id.glopal_to_logInFragment)
                    closeNavDraw()
                }
                else ->{

                }
            }
            true
        }
    }

    fun openNavDraw()
    {
        binding.drwer.open()
    }

    fun closeNavDraw()
    {
        binding.drwer.close()
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
        _binding=null
    }

}