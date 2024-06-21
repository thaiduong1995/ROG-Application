package com.duong.my_app.vm

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.duong.my_app.base.BaseViewModel
import com.duong.my_app.data.database.MyThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MyThemeRepository
) : BaseViewModel() {

    override val TAG = "MainViewModel"

    fun removePreview(firstInstall: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removePreview(firstInstall)
        }
    }

    override fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            super.clearData()
        }
    }
}