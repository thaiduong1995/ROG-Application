package com.duong.rog.vm

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.duong.rog.base.BaseViewModel
import com.duong.rog.data.database.ROGRepository
import com.duong.rog.data.model.Image
import com.duong.rog.ui.fragment.MainFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: ROGRepository
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