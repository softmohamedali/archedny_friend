package com.example.archedny_app_friend.presentation.body.viewmodels

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(

):ViewModel() {

    val bundleFromFragmentBToFragmentA = MutableLiveData<Bundle>()
}