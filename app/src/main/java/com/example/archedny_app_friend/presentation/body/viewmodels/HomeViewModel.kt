package com.example.archedny_app_friend.presentation.body.viewmodels

import androidx.lifecycle.ViewModel
import com.example.archedny_app_friend.data.repo.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

}