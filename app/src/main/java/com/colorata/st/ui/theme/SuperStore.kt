package com.colorata.st.ui.theme

import android.content.Context
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


fun Context.getApps(): MutableList<App> = AppDb.getInstance(this)?.appDao()?.getAll() ?: mutableListOf()

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
