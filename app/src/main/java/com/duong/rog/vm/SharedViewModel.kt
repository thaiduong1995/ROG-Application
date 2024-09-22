package com.duong.rog.vm

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import com.duong.rog.base.BaseViewModel
import com.duong.rog.data.database.MyThemeRepository
import com.duong.rog.data.model.Image
import com.duong.rog.data.model.Video
import com.duong.rog.data.reponse.ImageState
import com.duong.rog.data.reponse.ProgressBarState
import com.duong.rog.data.reponse.VideoState
import com.duong.rog.extension.getRealPathFromURI
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MyThemeRepository
) : BaseViewModel() {

    override val TAG = "ShareViewModel"

    private val imageList = mutableListOf<Image>()

    private var _progressBarStateFlow: MutableStateFlow<ProgressBarState> =
        MutableStateFlow(ProgressBarState.Idle)
    var progressBarState: StateFlow<ProgressBarState> = _progressBarStateFlow

    private val _videoListFlow: MutableStateFlow<VideoState> = MutableStateFlow(VideoState.IDLE)
    val videoListFlow: StateFlow<VideoState> = _videoListFlow

    private val _imageListFlow: MutableStateFlow<ImageState> = MutableStateFlow(ImageState.Idle)
    val imageListFlow: StateFlow<ImageState> = _imageListFlow

    private val _isPreviewFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPreviewFlow: StateFlow<Boolean> = _isPreviewFlow

    private val _bundleSaveFlow: MutableStateFlow<List<Bundle>> = MutableStateFlow(listOf())
    val bundleSaveFlow: StateFlow<List<Bundle>> = _bundleSaveFlow

    fun setProgressBarState(progressBarState: ProgressBarState) {
        viewModelScope.launch(Dispatchers.IO) {
            _progressBarStateFlow.value = progressBarState
        }
    }

    fun providerListAllVideo() {
        viewModelScope.launch(Dispatchers.IO) {
            _videoListFlow.value = VideoState.Loading
            val uriExternal: Uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val listOfAllVideo: MutableList<Video> = mutableListOf()
            val projection = arrayOf(MediaStore.Video.Media._ID)
            context.contentResolver.query(uriExternal, projection, null, null, null)
                ?.let { cursor ->
                    val columnIndexID: Int =
                        cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                    while (cursor.moveToNext()) {
                        val videoId: Long = cursor.getLong(columnIndexID)
                        val uriVideo = Uri.withAppendedPath(uriExternal, "" + videoId)
                        getRealPathFromURI(context, uriVideo)?.let {
                            listOfAllVideo.add(Video(path = it))
                        }
                    }
                    cursor.close()
                    _videoListFlow.value = VideoState.Success(listOfAllVideo)
                }
        }
    }

    fun goToImage() {
        viewModelScope.launch(Dispatchers.IO) {
            _imageListFlow.value = ImageState.Prepare
        }
    }

    fun providerListAllImage() {
        viewModelScope.launch(Dispatchers.IO) {
            _imageListFlow.value = ImageState.Loading
            imageList.clear()
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
                    getRealPathFromURI(context, uriImage)?.let {
                        listOfAllImages.add(
                            Image(name = File(it).name, path = it)
                        )
                    }
                }
                cursor.close()
            }
            imageList.addAll(listOfAllImages)
            _imageListFlow.value = ImageState.Success(imageList)
        }
    }

    fun deleteImage(position: Int) {
        imageList.removeAt(position)
    }

    fun setPreview(isPreview: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _isPreviewFlow.value = isPreview
        }
    }

    fun savedInstanceState(bundle: Bundle?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (bundle != null) {
                val bundleList = _bundleSaveFlow.value.toMutableList()
                bundle.keySet().forEach { key ->
                    if (bundleList.any { it.containsKey(key) }) {
                        bundleList.removeIf { it.containsKey(key) }
                    }
                    bundleList.add(bundle)
                }
                _bundleSaveFlow.value = bundleList
            } else {
                _bundleSaveFlow.value = listOf()
            }
        }
    }
}