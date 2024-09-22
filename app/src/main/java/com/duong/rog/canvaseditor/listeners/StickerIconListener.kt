package com.duong.rog.canvaseditor.listeners

import android.view.MotionEvent
import com.duong.rog.canvaseditor.stickers.StickerView

internal interface StickerIconListener {
    fun onActionDown(stickerView: StickerView?, event: MotionEvent?)
    fun onActionMove(stickerView: StickerView, event: MotionEvent)
    fun onActionUp(stickerView: StickerView, event: MotionEvent?)
}