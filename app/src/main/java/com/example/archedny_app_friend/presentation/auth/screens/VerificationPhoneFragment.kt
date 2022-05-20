package com.example.archedny_app_friend.presentation.auth.screens

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.databinding.FragmentVerificationPhoneBinding
import com.example.archedny_app_friend.domain.models.User
import com.example.archedny_app_friend.presentation.auth.AuthViewModel
import com.example.archedny_app_friend.utils.ResultState
import com.example.archedny_app_friend.utils.common.OTPReceiver
import com.example.archedny_app_friend.utils.common.PermissionManger
import com.example.archedny_app_friend.utils.out
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class VerificationPhoneFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentVerificationPhoneBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel>()
    private val navArgs by navArgs<VerificationPhoneFragmentArgs>()
    private lateinit var mPhone: String
    private val REQ_USER_CNSENT = 200
    private lateinit var mVerificationId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVerificationPhoneBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp() {
        mPhone = navArgs.phone
        mVerificationId = navArgs.verificationId
        if (!PermissionManger.hasSmsRecivePermission(requireContext())) {
            PermissionManger.requestSmsRecivePermission(this)
        }
        registerSmsReciver()
        setUpView()
        counter()
        setUpViewAction()
        setUpObservers()
    }


    private fun setUpView() {
        binding.numberMustVerifiyTextVerification.text = mPhone
        configOtpEditText(
            binding.phoneCode1Verfification,
            binding.phoneCode2Verfification,
            binding.phoneCode3Verfification,
            binding.phoneCode4Verfification,
            binding.phoneCode5Verfification,
            binding.phoneCode6Verfification,
        )
    }

    private fun setUpViewAction() {
        binding.verifiyButtonVerification.setOnClickListener {
            val code1 = binding.phoneCode1Verfification.text.toString()
            val code2 = binding.phoneCode2Verfification.text.toString()
            val code3 = binding.phoneCode3Verfification.text.toString()
            val code4 = binding.phoneCode4Verfification.text.toString()
            val code5 = binding.phoneCode5Verfification.text.toString()
            val code6 = binding.phoneCode6Verfification.text.toString()
            if (code1.trim().isEmpty() ||
                code2.trim().isEmpty() ||
                code3.trim().isEmpty() ||
                code4.trim().isEmpty() ||
                code5.trim().isEmpty() ||
                code6.trim().isEmpty()
            ) {
                Toast.makeText(requireContext(), "please enter code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val completeCode = code1 + code2 + code3 + code4 + code5 + code6
            val credinetial = authViewModel.getCredintial(completeCode, mVerificationId)
            authViewModel.singInWithCredential(credinetial)
        }
        binding.singupTextLogin.setOnClickListener {
            authViewModel.registerVerifiyWithPhone(
                phone = mPhone,
                activity = requireActivity()
            )
        }
    }

    private fun setUpObservers() {
        lifecycleScope.launchWhenStarted {
            authViewModel.isSingIn.collect {
                when (it) {
                    is ResultState.IsLoading -> {
                        binding.progressVerification.isVisible = true
                    }
                    is ResultState.IsError -> {
                        binding.progressVerification.isVisible = false
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.IsSucsses -> {
                        authViewModel.saveUser(User(
                            phone = mPhone
                        ))
                        binding.progressVerification.isVisible = false
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            authViewModel.isSaveUser.collect {
                when (it) {
                    is ResultState.IsLoading -> {
                        binding.progressVerification.isVisible = true
                    }
                    is ResultState.IsError -> {
                        binding.progressVerification.isVisible = false
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.IsSucsses -> {
                        binding.progressVerification.isVisible = false
                        authViewModel.saveUser(User(
                            phone = mPhone
                        ))
                        Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_verificationPhoneFragment_to_homeMapFragment2)
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            authViewModel.isVerifiying.collect {
                when (it) {
                    is ResultState.IsLoading -> {
                        binding.progressVerification.isVisible = true
                    }
                    is ResultState.IsError -> {
                        binding.progressVerification.isVisible = false
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.IsSucsses -> {
                        mVerificationId = it.data!!
                        counter()
                        binding.progressVerification.isVisible = false

                    }
                }
            }
        }

    }

    fun counter() {
        lifecycleScope.launch {
            for (i in 1..60) {
                delay(1000)
                binding.timeResendTextVerification.text = (60-i).toString()
            }
        }
    }

    fun configOtpEditText(vararg etList: EditText) {
        val afterTextChanged = { index: Int, e: Editable? ->
            val view = etList[index]
            val text = e.toString()

            when (view.id) {
                // first text changed
                etList[0].id -> {
                    if (text.isNotEmpty()) etList[index + 1].requestFocus()
                }

                // las text changed
                etList[etList.size - 1].id -> {
                    if (text.isEmpty()) etList[index - 1].requestFocus()
                }

                // middle text changes
                else -> {
                    if (text.isNotEmpty()) etList[index + 1].requestFocus()
                    else etList[index - 1].requestFocus()
                }
            }
            false
        }
        etList.forEachIndexed { index, editText ->
            editText.doAfterTextChanged { afterTextChanged(index, it) }
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Toast.makeText(requireContext(), "permission granted", Toast.LENGTH_SHORT).show()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.permissionPermanentlyDenied(this, perms.first())) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        } else {
            PermissionManger.requestSmsRecivePermission(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            PermissionManger.REQUEST_CODE_SMS_RECIVE,
            permissions,
            grantResults,
            this
        )
    }


    private fun registerSmsReciver() {
        OTPReceiver().setListener(object : OTPReceiver.SMSListener {
            override fun onSucess(otp: String) {
                binding.phoneCode1Verfification.setText(otp[0].toString())
                binding.phoneCode2Verfification.setText(otp[1].toString())
                binding.phoneCode3Verfification.setText(otp[2].toString())
                binding.phoneCode4Verfification.setText(otp[3].toString())
                binding.phoneCode5Verfification.setText(otp[4].toString())
                binding.phoneCode6Verfification.setText(otp[5].toString())
            }
            override fun onFalieur(msg: String) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}

