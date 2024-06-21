package com.duong.my_app.canvaseditor.models

import com.duong.my_app.canvaseditor.enums.DrawType
import com.duong.my_app.canvaseditor.stickers.Sticker

internal data class DrawObject(
    val pathAndPaint: PathAndPaint?,
    val sticker: Sticker?,
    val drawType: DrawType
)