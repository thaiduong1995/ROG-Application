package com.duong.my_app.vm

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.duong.my_app.base.BaseViewModel
import com.duong.my_app.data.database.MyThemeRepository
import com.duong.my_app.data.model.Folder
import com.duong.my_app.data.model.Video
import com.duong.my_app.data.reponse.VideoState
import com.duong.my_app.extension.getStringValueOrNull
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MyThemeRepository
) : BaseViewModel() {
    
    private val _listFolderFlow: MutableStateFlow<List<Folder>> = MutableStateFlow(listOf())
    val listFolderFlow: StateFlow<List<Folder>> = _listFolderFlow

    fun providerAllHiddenFolder() {
        viewModelScope.launch(Dispatchers.IO) {
            val listFolder = mutableListOf<Folder>()
            context.filesDir.listFiles()?.forEach {
                if (it.name.first() == '.') {
                    val folder = Folder(
                        name = it.name.substring(1, it.name.length),
                        path = it.absolutePath,
                        size = it.listFiles()?.size ?: 0
                    )
                    listFolder.add(folder)
                }
            }
            _listFolderFlow.value = listFolder
        }
    }

    fun createFolder(folderName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val folder = File(context.filesDir, ".${folderName}")
            Log.d("FolderViewModel", "${folder.path}")
            if (!folder.exists()) {
                folder.mkdir()
                File(folder.name, ".nomedia").mkdir()
            }
        }
    }

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