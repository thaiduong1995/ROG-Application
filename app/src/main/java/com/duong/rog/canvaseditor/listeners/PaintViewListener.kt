package com.duong.rog.canvaseditor.listeners

import android.view.MotionEvent
import com.duong.rog.canvaseditor.models.DrawObject

internal interface PaintViewListener {
    fun onTouchUp(obj: DrawObject)
    fun onClick(x: Float, y: Float)
    fun onTouchEvent(event: MotionEvent)
}