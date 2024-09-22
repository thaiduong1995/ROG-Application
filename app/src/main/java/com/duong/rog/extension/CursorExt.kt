package com.duong.rog.extension

import android.annotation.SuppressLint
import android.database.Cursor

/**
 * Created by Thinhvh on 21/11/2022.
 * Phone: 0398477967
 * Email: thinhvh.fpt@gmail.com
 */
@SuppressLint("Range")
fun Cursor.getStringValue(key: String): String = getString(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getStringValueOrNull(key: String) =
    if (isNull(getColumnIndex(key))) null else getString(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getIntValue(key: String) = getInt(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getIntValueOrNull(key: String) =
    if (isNull(getColumnIndex(key))) null else getInt(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getLongValue(key: String) = getLong(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getLongValueOrNull(key: String) =
    if (isNull(getColumnIndex(key))) null else getLong(getColumnIndex(key))

@SuppressLint("Range")
fun Cursor.getBlobValue(key: String): ByteArray = getBlob(getColumnIndex(key))