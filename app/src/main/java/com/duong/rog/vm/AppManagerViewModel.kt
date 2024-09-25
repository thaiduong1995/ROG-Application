package com.duong.rog.vm

import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageStats
import android.content.pm.ResolveInfo
import android.os.Build
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.duong.rog.IPackageStatsObserver
import com.duong.rog.base.BaseViewModel
import com.duong.rog.data.database.ROGRepository
import com.duong.rog.data.model.AppData
import com.duong.rog.data.reponse.AppState
import com.duong.rog.extension.readableFileSize
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import javax.inject.Inject


@HiltViewModel
class AppManagerViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: ROGRepository
) : BaseViewModel() {

    override val TAG = "AppManagerViewModel"

    private val _listAppFlow: MutableStateFlow<AppState> = MutableStateFlow(AppState.IDLE)
    val listAppFlow: StateFlow<AppState> = _listAppFlow

    fun providerListAllApp() {
        viewModelScope.launch(Dispatchers.IO) {
            _listAppFlow.value = AppState.Loading
            val resolveInfoList: List<ResolveInfo> = context.packageManager.queryIntentActivities(
                Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER),
                PackageManager.MATCH_ALL or PackageManager.MATCH_DISABLED_COMPONENTS or 0
            )
            val appDataList: ArrayList<AppData> = arrayListOf()
            resolveInfoList.forEach { resolveInfo ->
                with(resolveInfo) {
                    if (activityInfo.packageName != context.packageName &&
                        appDataList.find { it.packageName.lowercase() == activityInfo.packageName.lowercase() } == null
                    ) {
                        var size = 0L
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val storageStatsManager =
                                context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
                            try {
                                val ai = context.packageManager.getApplicationInfo(
                                    activityInfo.packageName, 0
                                )
                                val storageStats = storageStatsManager.queryStatsForUid(
                                    ai.storageUuid,
                                    activityInfo.applicationInfo.uid
                                )
                                val cacheSize = storageStats.cacheBytes
                                val dataSize = storageStats.dataBytes
                                val apkSize = storageStats.appBytes
                                size = cacheSize + dataSize + apkSize
                            } catch (e: Exception) {
                            }
                        } else {
                            try {
                                val packageManager: PackageManager = context.packageManager
                                val getPackageSizeInfo: Method = packageManager.javaClass.getMethod(
                                    "getPackageSizeInfo",
                                    String::class.java,
                                    IPackageStatsObserver::class.java
                                )
                                getPackageSizeInfo.invoke(
                                    packageManager,
                                    activityInfo.packageName,
                                    object : IPackageStatsObserver.Stub() {
                                        override fun onGetStatsCompleted(
                                            pStats: PackageStats?,
                                            succeeded: Boolean
                                        ) {
                                            Log.d("TAG", "pStats")
                                            pStats?.let { packageStats ->
                                                val totalCacheSize: Long =
                                                    packageStats.cacheSize + packageStats.externalCacheSize
                                                val totalDataSize: Long =
                                                    packageStats.dataSize + packageStats.externalDataSize
                                                val totalCodeSize: Long =
                                                    packageStats.codeSize + packageStats.externalCodeSize
                                                size =
                                                    totalCacheSize + totalDataSize + totalCodeSize
                                            }
                                        }
                                    }
                                )
                            } catch (ex: Exception) {
                            }
                        }

                        val appData = AppData(
                            packageName = activityInfo.packageName,
                            appName = context.packageManager.getApplicationLabel(
                                context.packageManager.getApplicationInfo(
                                    activityInfo.packageName, PackageManager.GET_META_DATA
                                )
                            ).toString(),
                            size = size.readableFileSize()
                        )
                        Log.d("AppManagerViewModel", "$appData")
                        if (resolveInfo.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                            appDataList.add(appData)
                        }
                    }
                }
            }
            _listAppFlow.value = AppState.Success(listApp = appDataList)
        }
    }
}