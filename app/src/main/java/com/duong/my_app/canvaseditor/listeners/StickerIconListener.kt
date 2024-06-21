package com.duong.my_app.canvaseditor.listeners

import android.view.MotionEvent
import com.duong.my_app.canvaseditor.stickers.StickerView

internal interface StickerIconListener {
    fun onActionDown(stickerView: StickerView?, event: MotionEvent?)
    fun onActionMove(stickerView: StickerView, event: MotionEvent)
    fun onActionUp(stickerView: StickerView, event: MotionEvent?)
}