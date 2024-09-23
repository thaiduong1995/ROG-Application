package com.duong.rog.canvaseditor.events

import android.view.MotionEvent
import com.duong.rog.canvaseditor.listeners.StickerIconListener
import com.duong.rog.canvaseditor.stickers.StickerView

internal class DeleteIconEvent: StickerIconListener {
    override fun onActionDown(stickerView: StickerView?, event: MotionEvent?) {}
    override fun onActionMove(stickerView: StickerView, event: MotionEvent) {}
    override fun onActionUp(stickerView: StickerView, event: MotionEvent?) {
        stickerView.remove()
    }
}