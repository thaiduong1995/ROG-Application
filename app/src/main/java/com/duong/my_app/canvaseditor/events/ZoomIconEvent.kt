package com.duong.my_app.canvaseditor.events

import android.view.MotionEvent
import com.duong.my_app.canvaseditor.listeners.StickerIconListener
import com.duong.my_app.canvaseditor.stickers.StickerView

internal class ZoomIconEvent: StickerIconListener {
    override fun onActionDown(stickerView: StickerView?, event: MotionEvent?) {}
    override fun onActionMove(stickerView: StickerView, event: MotionEvent) {
        stickerView.zoomAndRotate(event)
    }
    override fun onActionUp(stickerView: StickerView, event: MotionEvent?) {}
}