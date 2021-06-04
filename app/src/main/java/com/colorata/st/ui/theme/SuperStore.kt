package com.colorata.st.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.room.*

/*AppDb.getInstance(this)?.appDao()*/

fun Context.updateApp(id: String, name: String) {
    val list = AppDb.getInstance(this)?.appDao()?.getAll()
    if (list != null) {
        for (i in list) {
            if (i.id == id) {
                AppDb.getInstance(this)?.appDao()?.update(App(id, name))
                return
            }
        }
    }
    AppDb.getInstance(this)?.appDao()?.insert(App(id, name))
}

fun Context.deleteApp(id: String, name: String) {
    val list = AppDb.getInstance(this)?.appDao()?.getAll()
    if (list != null) {
        for (i in list) {
            if (i.id == id) {
                AppDb.getInstance(this)?.appDao()?.delete(App(id, name))
                return
            }
        }
    }
}


fun Context.getApps(): MutableList<App> =
    AppDb.getInstance(this)?.appDao()?.getAll() ?: mutableListOf()

@Entity
data class App(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String
)

@Dao
interface AppDao {
    @Query("SELECT * FROM app")
    fun getAll(): MutableList<App>

    @Query("DELETE FROM app")
    fun nukeTable()

    @Insert
    fun insert(app: App)

    @Insert
    fun insert(appList: List<App>)

    @Update
    fun update(app: App)

    @Delete
    fun delete(app: App)
}

@Database(entities = [App::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        private var INSTANCE: AppDb? = null

        fun getInstance(context: Context): AppDb? {
            if (INSTANCE == null) {
                synchronized(AppDb::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDb::class.java, "SKeys.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

fun Context.put(key: String, value: Int) {
    val shared = getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    shared.edit().putInt(key, value).apply()
}

fun Context.put(key: String, value: String) {
    val shared = getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    shared.edit().putString(key, value).apply()
}

fun Context.put(key: String, value: Float) {
    val shared = getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    shared.edit().putFloat(key, value).apply()
}

fun Context.put(key: String, value: Boolean) {
    val shared = getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
    shared.edit().putBoolean(key, value).apply()
}

class SuperStore(forThis: Context) {

    val context = forThis

    fun drop(key: String, value: Int) {
        val shared: SharedPreferences =
            context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
        shared.edit().putInt(key, value).apply()
    }

    fun drop(key: String, value: String) {
        val shared: SharedPreferences =
            context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
        shared.edit().putString(key, value).apply()
    }

    fun drop(key: String, value: Float) {
        val shared: SharedPreferences =
            context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
        shared.edit().putFloat(key, value).apply()
    }

    fun drop(key: String, value: Boolean) {
        val shared: SharedPreferences =
            context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
        shared.edit().putBoolean(key, value).apply()
    }

    fun drop(pair: MutableList<Pair<String, Any>>): Boolean {
        pair.forEach {
            when (val buffer = it.second) {
                is Boolean -> {
                    drop(it.first, buffer)
                }
                is String -> {
                    drop(it.first, buffer)
                }
                is Float -> {
                    drop(it.first, buffer)
                }
                is Int -> {
                    drop(it.first, buffer)
                }
                else -> return false
            }
        }
        return true
    }

    fun catchInt(key: String): Int {
        val shared: SharedPreferences =
            context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
        return shared.getInt(key, 0)
    }

    fun catchString(key: String): String {
        val shared: SharedPreferences =
            context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
        return shared.getString(key, "") ?: ""
    }

    fun catchFloat(key: String): Float {
        val shared: SharedPreferences =
            context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
        return shared.getFloat(key, 0f)
    }

    fun catchBoolean(key: String): Boolean {
        val shared: SharedPreferences =
            context.getSharedPreferences(Strings.shared, Context.MODE_PRIVATE)
        return shared.getBoolean(key, false)
    }
}