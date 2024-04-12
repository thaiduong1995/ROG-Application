package com.duong.mytheme.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.duong.mytheme.service.VideoLiveWallpaperService

internal class BootBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val service = Intent(context, VideoLiveWallpaperService::class.java)
            context.startService(service)
        }
    }
}