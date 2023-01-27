package com.example.archedny_app_friend.future_main_track.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archedny_app_friend.core.data.datastore.DataStoreOperationImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val dataStore: DataStoreOperationImpl
):ViewModel() {

    val isExtendMap=dataStore.getExtendMapToolKey()

    fun saveIsExtendMap(auto:Boolean){
        viewModelScope.launch(Dispatchers.IO){ dataStore.saveExtendMapToolKey(auto) }
    }

    val isDarkTheme=dataStore.getIsDarkTheme()

    fun saveIsDarkTheme(isDark:Boolean){
        viewModelScope.launch(Dispatchers.IO){ dataStore.saveIsDarkTheme(isDark) }
    }

    val lang=dataStore.getLang()

    fun saveLang(lang:String){
        viewModelScope.launch(Dispatchers.IO){ dataStore.saveLang(lang) }
    }
}