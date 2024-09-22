package com.duong.rog.canvaseditor.listeners

import android.view.MotionEvent
import com.duong.rog.canvaseditor.models.DrawObject

internal interface StickerViewListener {
    fun onRemove()
    fun onDone(obj: DrawObject)
    fun onZoomAndRotate()
    fun onFlip()
    fun onClickStickerOutside(x: Float, y: Float)
    fun onTouchEvent(event: MotionEvent)
}