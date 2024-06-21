package com.duong.my_app.canvaseditor.listeners

import android.view.MotionEvent
import com.duong.my_app.canvaseditor.models.DrawObject

internal interface PaintViewListener {
    fun onTouchUp(obj: DrawObject)
    fun onClick(x: Float, y: Float)
    fun onTouchEvent(event: MotionEvent)
}