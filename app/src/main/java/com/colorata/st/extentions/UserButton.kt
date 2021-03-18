package com.colorata.st.extentions

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable

class UserButton(private val context: Context) {

    val iconer = getAppsIcon()
    val packager = getAppsPackage()
    val labeler = getAppsLabel()

    //Fun for get LABELS
    private fun getAppsLabel(): MutableList<String> {
        val manager = context.packageManager
        val userLabel = mutableListOf<String>()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val availableActivities: List<ResolveInfo> = manager.queryIntentActivities(i, 0)
        for (ri in availableActivities) {
            userLabel.add(ri.loadLabel(manager).toString())
        }

        return userLabel
    }

    //Fun for get PACKAGES
    private fun getAppsPackage(): MutableList<String> {
        val manager = context.packageManager
        val userPackage = mutableListOf<String>()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val availableActivities: List<ResolveInfo> = manager.queryIntentActivities(i, 0)
        for (ri in availableActivities) {
            userPackage.add(ri.activityInfo.packageName)
        }

        return userPackage
    }

    //Fun for get ICONS
    private fun getAppsIcon(): MutableList<Drawable?> {
        val manager = context.packageManager
        val userIcon = mutableListOf<Drawable?>()
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val availableActivities: List<ResolveInfo> = manager.queryIntentActivities(i, 0)
        for (ri in availableActivities) {
            userIcon.add(ri.activityInfo.loadIcon(manager))
        }

        return userIcon
    }
}