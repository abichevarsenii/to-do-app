package com.example.todoapp

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

class MyApp : Application() {

    private lateinit var database: AppDatabase
    public lateinit var repository: ToDoItemsRepository

    @Database(entities = [ItemEntity::class], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun ItemEntity(): DatabaseAPI
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "ToDoItems").build()
        repository = ToDoItemsRepository(database.ItemEntity())
    }
}