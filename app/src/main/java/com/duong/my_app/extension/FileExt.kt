package com.duong.my_app.extension

import java.io.File

fun File.move(file: File) {
    inputStream().use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    this.delete()
}