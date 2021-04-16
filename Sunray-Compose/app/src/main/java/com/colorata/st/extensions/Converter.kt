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