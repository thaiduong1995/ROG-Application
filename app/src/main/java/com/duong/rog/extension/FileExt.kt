package com.duong.rog.extension

import java.io.File

fun File.move(file: File) {
    inputStream().use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    this.delete()
}