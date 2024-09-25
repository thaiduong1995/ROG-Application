package com.duong.rog.data.database

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.duong.rog.extension.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ROGRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun removePreview(firstInstallState: Int) {
        context.dataStore.edit {
            it[FIRST_INSTALL] = firstInstallState
        }
    }

    val firstInstall: Flow<Int> = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            emit(emptyPreferences())
            throw exception
        }
    }.map { preference ->
        preference[FIRST_INSTALL] ?: 0
    }

    companion object {
        val FIRST_INSTALL = intPreferencesKey("FIRST_INSTALL")
    }
}