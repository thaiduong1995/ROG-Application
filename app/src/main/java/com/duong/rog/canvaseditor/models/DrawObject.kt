package com.duong.rog.canvaseditor.models

import com.duong.rog.canvaseditor.enums.DrawType
import com.duong.rog.canvaseditor.stickers.Sticker

internal data class DrawObject(
    val pathAndPaint: PathAndPaint?,
    val sticker: Sticker?,
    val drawType: DrawType
)