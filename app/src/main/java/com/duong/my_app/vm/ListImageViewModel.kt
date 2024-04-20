package com.duong.my_app.vm

import android.content.Context

import androidx.lifecycle.viewModelScope
import com.duong.my_app.base.BaseViewModel
import com.duong.my_app.data.database.MyThemeRepository
import com.duong.my_app.data.model.Image
import com.duong.my_app.data.reponse.ImageState
import com.duong.my_app.extension.move
import com.duong.my_app.ui.fragment.ListImageFragmentDirections

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

import javax.inject.Inject

@HiltViewModel
class ListImageViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MyThemeRepository
) : BaseViewModel() {

    private val _imageStateFlow: MutableStateFlow<ImageState> = MutableStateFlow(ImageState.IDLE)
    val imageStateFlow: StateFlow<ImageState> = _imageStateFlow

    fun gotoImagePreview(image: Image, listImage: Array<Image>) {
        navigate(ListImageFragmentDirections.actionListImageToImagePreview(image, listImage))
    }

    fun moveImage(folderPath: String, listImage: List<Image>) {
        viewModelScope.launch(Dispatchers.IO) {
            _imageStateFlow.value = ImageState.Loading
            val list = mutableListOf<Image>()
            listImage.forEach { image ->
                val temp = File(File(folderPath), image.name)
                if (temp.createNewFile()) {
                    File(image.path).move(temp)
                    list.add(image)
                }
            }
            _imageStateFlow.value = ImageState.Success(list)
        }
    }

    fun deleteFile(listImage: MutableList<Image>) {
        viewModelScope.launch(Dispatchers.IO) {
            _imageStateFlow.value = ImageState.Loading
            val list = mutableListOf<Image>()
            listImage.forEach { image ->
                File(image.path).delete()
                list.add(image)
            }
            _imageStateFlow.value = ImageState.Success(list)
        }
    }
}