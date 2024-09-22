package com.duong.rog.vm

import android.content.Context
import com.duong.rog.base.BaseViewModel
import com.duong.rog.data.database.MyThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MyThemeRepository
) : BaseViewModel() {

    override val TAG = "ThemeViewModel"

}