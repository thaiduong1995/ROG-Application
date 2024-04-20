package com.duong.my_app.vm

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import com.duong.my_app.base.BaseViewModel
import com.duong.my_app.data.database.MyThemeRepository
import com.duong.my_app.data.model.Folder
import com.duong.my_app.data.model.Image
import com.duong.my_app.data.reponse.ImageState
import com.duong.my_app.extension.getRealPathFromURI
import com.duong.my_app.extension.move
import com.duong.my_app.ui.fragment.MainFragmentDirections
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

    private val _folderFlow: MutableStateFlow<Folder?> = MutableStateFlow(null)
    val folderFlow: StateFlow<Folder?> = _folderFlow

    private val _listImageFlow: MutableStateFlow<ImageState> = MutableStateFlow(ImageState.IDLE)
    val listImageFlow: StateFlow<ImageState> = _listImageFlow

    private val _imageStateFlow: MutableStateFlow<ImageState> = MutableStateFlow(ImageState.IDLE)
    val imageStateFlow: StateFlow<ImageState> = _imageStateFlow

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
            if (!folder.exists()) {
                folder.mkdir()
                File(folder.name, ".nomedia").mkdir()
                _folderFlow.value = Folder(name = folderName, path = folder.absolutePath)
            }
        }
    }

    fun goToFolderDetail(title: String?, folderPath: String, listImage: Array<Image>) {
        navigate(
            MainFragmentDirections.actionMainToListImage(
                title = title,
                folderPath = folderPath,
                listImage = listImage
            )
        )
    }

    fun getAllImage() {
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

    override fun clearData() {
        super.clearData()
        _listImageFlow.value = ImageState.IDLE
    }
}