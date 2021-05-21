package com.colorata.st.ui.theme

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getStringOrNull

val Context.database: SQLiteDatabase
    get() = openOrCreateDatabase("SKeys.db", MODE_PRIVATE, null)

fun Context.getApp(packageName: String): Pair<String, String> {
    val base = openOrCreateDatabase("SKeys.db", MODE_PRIVATE, null)
    base.execSQL("CREATE TABLE IF NOT EXISTS appKeys (keys TEXT, return INTEGER)")
    val query = base.rawQuery("SELECT * FROM appKeys WHERE keys = '%$packageName%'", null)
    val output = Pair(query.getStringOrNull(1) ?: "com.colorata.st", query.getStringOrNull(2) ?: "STasks")
    query.close()
    base.close()
    return output
}

/*


fun Context.getAllApps(packageName: String): MutableList<Pair<String, String>> {
    val base = openOrCreateDatabase("SKeys.db", MODE_PRIVATE, null)
    base.execSQL("CREATE TABLE IF NOT EXISTS appKeys (keys TEXT, return INTEGER)")
    val query = base.rawQuery("SELECT * FROM appKeys WHERE xColumn LIKE '%$packageName%'", null)
    val output = Pair(query.getStringOrNull(1) ?: "com.colorata.st", query.getStringOrNull(2) ?: "STasks")
    query.moveToFirst()
    query.close()
    base.close()
    return output
}
*/
