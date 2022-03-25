package com.githubuserapps.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.githubuserapps.data.local.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
@TypeConverters(UserTypeConverter::class)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile // all of threads will have immediate access to this property
        private var instance: UserDatabase? = null
        private val lock = Any() // dummy object for thread monitoring

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            // If the instance var hasn't been initialized, call buildDatabase()
            // and assign it the returned object from the function call (it)
            instance ?: buildDatabase(context).also { instance = it }
        }

        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDatabase::class.java,
            "user_github_database"
        ).build()
    }

}

//        @Volatile
//        private var INSTANCE: UserDatabase? = null
//
//        fun getDatabase(context: Context): UserDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    UserDatabase::class.java,
//                    "user_github_database"
//                ).build()
//                INSTANCE = instance
//                // return instance
//                return instance
//            }
//        }
