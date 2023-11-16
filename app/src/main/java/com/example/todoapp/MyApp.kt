package com.example.todoapp

import android.app.Application
import androidx.room.Room
import com.example.todoapp.data.items.ItemEntity
import com.example.todoapp.data.items.ToDoItemsRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

}