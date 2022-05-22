package com.example.archedny_app_friend.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
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
import com.example.archedny_app_friend.presentation.auth.AuthViewModel
import com.example.archedny_app_friend.utils.myextention.toast
import com.example.archedny_app_friend.utils.out
import com.google.android.gms.auth.api.Auth
import dagger.hilt.android.AndroidEntryPoint

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
    private val  authViewModel:AuthViewModel by viewModels<AuthViewModel>()

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
        setUpNavDrawer()
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
                else ->{

                }
            }
            true
        }
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