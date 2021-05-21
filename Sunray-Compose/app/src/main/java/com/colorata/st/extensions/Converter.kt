package com.colorata.st.extensions

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


fun pxToDp(px: Int) = (px / Resources.getSystem().displayMetrics.density).toInt()

fun Bitmap.toSharingString(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, baos)

    val array = baos.toByteArray()
    return Base64.encodeToString(array, Base64.DEFAULT);
}

fun String.toBitmap(): Bitmap? {
    val imageAsBytes: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
}

fun Int.toHex(): String {
    return String.format("#%06X", (0xFFFFFF and  this))
}

fun String.toHSV(): Triple<Float, Float, Float> {
    val r: Float = Integer.parseInt("${this[1]}${this[2]}", 16).toFloat()/255
    val g: Float = Integer.parseInt("${this[3]}${this[4]}", 16).toFloat()/255
    val b: Float = Integer.parseInt("${this[5]}${this[6]}", 16).toFloat()/255

    val min = r.coerceAtMost(g).coerceAtMost(b)
    val max = r.coerceAtLeast(g).coerceAtLeast(b)

    val dif = max - min
    val v = max * 100

    val h: Float = when {
        min == max -> {
            0f
        }
        max == r && g >= b -> {
            (60 * ((g - b) / dif)) % 360
        }
        max == r && g < b -> {
            (60 * ((g - b) / dif) + 360) % 360
        }
        max == g -> {
            (60 * ((b - r) / dif) + 120) % 360
        }
        max == b -> {
            (60 * ((r - g) / dif) + 240) % 360
        }
        else -> 0f
    }
    val s = if (max == 0f) {
        0f
    } else (dif / max) * 100

    return Triple(h, s, v)
}