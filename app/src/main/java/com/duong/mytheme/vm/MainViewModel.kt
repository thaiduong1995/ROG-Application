package com.duong.mytheme.vm

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import com.duong.mytheme.base.BaseViewModel
import com.duong.mytheme.data.database.MyThemeRepository
import com.duong.mytheme.data.model.Video
import com.duong.mytheme.data.reponse.VideoState
import com.duong.mytheme.extension.getStringValueOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MyThemeRepository
) : BaseViewModel() {

    private val _listVideoFlow: MutableStateFlow<VideoState> = MutableStateFlow(VideoState.IDLE)
    val listVideoFlow: StateFlow<VideoState> = _listVideoFlow

    fun removePreview() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.removePreview()
        }
    }

    fun providerListAllVideo() {
        viewModelScope.launch(Dispatchers.IO) {
            val listVideo = mutableListOf<Video>()
            _listVideoFlow.value = VideoState.Loading
            val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            try {
                val sel =
                    "${MediaStore.Video.Media.DURATION}> '0' AND ${MediaStore.Video.Media.SIZE}> '0'"
                context.contentResolver.query(uri, null, sel, null, null)?.let { cursor ->
                    //looping through all rows and adding to list
                    if (cursor.count > 0) {
                        if (cursor.moveToFirst()) {
                            do {
                                val data =
                                    cursor.getStringValueOrNull(MediaStore.Video.Media.DATA) ?: ""
                                if (File(data).exists()) {
                                    listVideo.add(Video(path = data))
                                }
                            } while (cursor.moveToNext())
                            cursor.close()
                        }
                    }
                    _listVideoFlow.value = VideoState.Success(listVideo = listVideo)
                }
            } catch (e: Exception) {
                _listVideoFlow.value = VideoState.Error(message = e.message.toString())
            }
        }
    }

}