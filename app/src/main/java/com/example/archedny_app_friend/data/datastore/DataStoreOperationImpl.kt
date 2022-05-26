package com.example.archedny_app_friend.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.archedny_app_friend.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val android.content.Context.dataStore by preferencesDataStore(Constants.DATASTORE_ARCHEDNY_NAME)

@Singleton
class DataStoreOperationImpl @Inject constructor (
    @ApplicationContext val context: Context
){

    val dataStore=context.dataStore

    object PrefrencesKey{
        val extendMapToolKey= booleanPreferencesKey(Constants.EXTEND_MAP_KEY)
        val isDarkThemKey= booleanPreferencesKey(Constants.IS_DARK_THEME_KEY)
        val langKey= stringPreferencesKey(Constants.LANG_KEY)
    }



    suspend fun saveExtendMapToolKey(auto: Boolean) {
        dataStore.edit {
            it[PrefrencesKey.extendMapToolKey]=auto
        }
    }

    fun getExtendMapToolKey(): Flow<Boolean> {
        return dataStore.data.catch {ex->
            if (ex is IOException){
                emit(emptyPreferences())
            }else{
                throw ex
            }
        }.map { pref->
            val isExtendedMap=pref[PrefrencesKey.extendMapToolKey] ?: false
            isExtendedMap
        }
    }

    suspend fun saveIsDarkTheme(isDark: Boolean) {
        dataStore.edit {
            it[PrefrencesKey.isDarkThemKey]=isDark
        }
    }

    fun getIsDarkTheme(): Flow<Boolean> {
        return dataStore.data.catch {ex->
            if (ex is IOException){
                emit(emptyPreferences())
            }else{
                throw ex
            }
        }.map { pref->
            val isDarkTheme=pref[PrefrencesKey.isDarkThemKey] ?: false
            isDarkTheme
        }
    }

    suspend fun saveLang(lang: String) {
        dataStore.edit {
            it[PrefrencesKey.langKey]=lang
        }
    }

    fun getLang(): Flow<String> {
        return dataStore.data.catch {ex->
            if (ex is IOException){
                emit(emptyPreferences())
            }else{
                throw ex
            }
        }.map { pref->
            val lang=pref[PrefrencesKey.langKey] ?: "en"
            lang
        }
    }




}