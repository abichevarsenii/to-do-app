package com.example.todoapp.data.items

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.todoapp.data.items.api.DatabaseApi

@Database(entities = [ItemEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemsApi(): DatabaseApi
}