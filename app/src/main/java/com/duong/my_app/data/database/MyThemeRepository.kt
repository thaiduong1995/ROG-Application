package com.duong.my_app.data.database

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.duong.my_app.extension.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyThemeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun removePreview() {
        context.dataStore.edit {
            it[IS_FIRST_TIME] = false
        }
    }

    val isFirstTime: Flow<Boolean> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            emit(emptyPreferences())
            throw exception
        }
    }.map { preference ->
        preference[IS_FIRST_TIME] ?: true
    }

    companion object {
        val IS_FIRST_TIME = booleanPreferencesKey("IS_FIRST_TIME_INSTALL")
    }
}