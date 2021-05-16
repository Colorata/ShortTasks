package com.colorata.st.ui.theme

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.database.sqlite.SQLiteDatabase
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

val Context.database: SQLiteDatabase
    get() = openOrCreateDatabase("SKeys.db", MODE_PRIVATE, null)

fun Context.getBoolean() {
    val base = openOrCreateDatabase("SKeys.db", MODE_PRIVATE, null)
    base.execSQL("CREATE TABLE IF NOT EXISTS booleanKeys (keys TEXT, return INTEGER)")
}

@Entity
data class BoolRoom(
    @PrimaryKey val key: String,
    @ColumnInfo(name = "Bool") val bool: Boolean?
)

