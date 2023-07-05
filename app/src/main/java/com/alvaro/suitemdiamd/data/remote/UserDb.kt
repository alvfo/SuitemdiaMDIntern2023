package com.alvaro.suitemdiamd.data.remote

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alvaro.suitemdiamd.data.api.UserResponseItem

@Database(
    entities = [UserResponseItem::class, RemoteKey::class],
    version = 1,
    exportSchema = false
)
abstract class UserDb : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun remoteKeysDao(): RemoteDao

    companion object {
        @Volatile
        private var INSTANCE: UserDb? = null

        @JvmStatic
        fun getDatabase(context: Context): UserDb {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDb::class.java, "user_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}