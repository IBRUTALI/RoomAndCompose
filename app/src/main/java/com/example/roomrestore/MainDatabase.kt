package com.example.roomrestore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1)
abstract class MainDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {

        fun getDB(context: Context): MainDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDatabase::class.java, "main-db"
            ).build()
        }

    }
}