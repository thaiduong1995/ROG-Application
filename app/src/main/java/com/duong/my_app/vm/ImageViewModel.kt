package com.duong.my_app.vm

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.duong.my_app.base.BaseViewModel
import com.duong.my_app.data.database.MyThemeRepository
import com.duong.my_app.data.model.Image
import com.duong.my_app.ui.fragment.MainFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MyThemeRepository
) : BaseViewModel() {

    override val TAG = "ListImageViewModel"

    fun goToImagePreview(imageList: Array<Image>, position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            navigate(MainFragmentDirections.actionMainToImagePreview(imageList, position))
        }
    }

    override fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            super.clearData()
        }
    }
}