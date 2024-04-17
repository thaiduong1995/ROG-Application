package com.duong.my_app.vm

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log

import androidx.lifecycle.viewModelScope
import com.duong.my_app.base.BaseViewModel
import com.duong.my_app.data.database.MyThemeRepository
import com.duong.my_app.data.model.Image
import com.duong.my_app.data.reponse.ImageState
import com.duong.my_app.extension.getRealPathFromURI

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MyThemeRepository
) : BaseViewModel() {

    private val _listImageFlow: MutableStateFlow<ImageState> = MutableStateFlow(ImageState.IDLE)
    val listImageFlow: StateFlow<ImageState> = _listImageFlow

    fun providerListAllImage() {
        viewModelScope.launch(Dispatchers.IO) {
            _listImageFlow.value = ImageState.Loading
            val listOfAllImages = mutableListOf<Image>()
            val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

            try {
                context.contentResolver.query(uri, projection, null, null, null)?.let { cursor ->
                    val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
                    while (cursor.moveToNext()) {
                        cursor.getString(columnIndexData)?.let { absolutePathOfImage ->
                            listOfAllImages.add(Image(name = File(absolutePathOfImage).name, path = absolutePathOfImage))
                        }
                    }
                    cursor.close()
                    _listImageFlow.value = ImageState.Success(listImage = listOfAllImages)
                }

            } catch (ex: Exception) {
                _listImageFlow.value = ImageState.Error(message = ex.message.toString())
            }
        }
    }

    fun getAllShownImagesPath() {
        viewModelScope.launch(Dispatchers.IO) {
            _listImageFlow.value = ImageState.Loading
            val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor: Cursor?
            val columnIndexID: Int
            val listOfAllImages: MutableList<Image> = mutableListOf()
            val projection = arrayOf(MediaStore.Images.Media._ID)
            var imageId: Long
            cursor = context.contentResolver.query(uriExternal, projection, null, null, null)
            if (cursor != null) {
                columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    imageId = cursor.getLong(columnIndexID)
                    val uriImage = Uri.withAppendedPath(uriExternal, "" + imageId)
                    Log.d("uriImage", "$uriImage")
                    getRealPathFromURI(context, uriImage)?.let {
                        listOfAllImages.add(
                            Image(name = File(it).name, path = it)
                        )
                    }
                }
                cursor.close()
            }
            _listImageFlow.value = ImageState.Success(listOfAllImages)
        }
    }
}