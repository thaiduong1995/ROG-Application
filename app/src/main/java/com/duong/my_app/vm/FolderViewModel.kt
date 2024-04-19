package com.duong.my_app.vm

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.duong.my_app.base.BaseViewModel
import com.duong.my_app.data.database.MyThemeRepository
import com.duong.my_app.data.model.Folder
import com.duong.my_app.data.model.Image
import com.duong.my_app.data.reponse.ImageState
import com.duong.my_app.extension.move
import com.duong.my_app.ui.fragment.MainFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import javax.inject.Inject

@HiltViewModel
class FolderViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: MyThemeRepository
) : BaseViewModel() {

    private val _listFolderFlow: MutableStateFlow<List<Folder>> = MutableStateFlow(listOf())
    val listFolderFlow: StateFlow<List<Folder>> = _listFolderFlow

    private val _folderFlow: MutableStateFlow<Folder?> = MutableStateFlow(null)
    val folderFlow: StateFlow<Folder?> = _folderFlow

    private val _listImageFlow: MutableStateFlow<ImageState> = MutableStateFlow(ImageState.IDLE)
    val listImageFlow: StateFlow<ImageState> = _listImageFlow

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
                _folderFlow.value = Folder(name = folderName, path = folder.absolutePath)
            }
        }
    }

    fun goToImageFolder() {
        navigate(MainFragmentDirections.actionMainToImage())
    }

    fun moveImage(folder: Folder, listImage: List<Image>) {
        viewModelScope.launch(Dispatchers.IO) {
            _listImageFlow.value = ImageState.Loading
            val list = mutableListOf<Image>()
            listImage.forEach { image ->
                val temp = File(File(folder.path), image.name)
                if (temp.createNewFile()) {
                    File(image.path).move(temp)
                    list.add(image)
                }
            }
            _listImageFlow.value = ImageState.Success(list)
        }
    }
}