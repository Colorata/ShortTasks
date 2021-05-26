package com.colorata.st.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.colorata.st.R

fun Context.getAppsLabel(): MutableList<String> {
    val manager = packageManager
    val userLabel = mutableListOf<String>()
    val i = Intent(Intent.ACTION_MAIN, null)
    i.addCategory(Intent.CATEGORY_LAUNCHER)
    val availableActivities: List<ResolveInfo> = manager.queryIntentActivities(i, 0)
    for (ri in availableActivities) {
        userLabel.add(ri.loadLabel(manager).toString())
    }
    return userLabel
}

fun Context.getAppsPackage(): MutableList<String> {
    val manager = packageManager
    val userPackage = mutableListOf<String>()
    val i = Intent(Intent.ACTION_MAIN, null)
    i.addCategory(Intent.CATEGORY_LAUNCHER)
    val availableActivities: List<ResolveInfo> = manager.queryIntentActivities(i, 0)
    for (ri in availableActivities) {
        userPackage.add(ri.activityInfo.packageName)
    }

    return userPackage
}

fun Context.getAppsIcon(): MutableList<Drawable?> {
    val manager = packageManager
    val userIcon = mutableListOf<Drawable?>()
    val i = Intent(Intent.ACTION_MAIN, null)
    i.addCategory(Intent.CATEGORY_LAUNCHER)
    val availableActivities: List<ResolveInfo> = manager.queryIntentActivities(i, 0)
    for (ri in availableActivities) {
        userIcon.add(ri.activityInfo.loadIcon(manager))
    }

    return userIcon
}

