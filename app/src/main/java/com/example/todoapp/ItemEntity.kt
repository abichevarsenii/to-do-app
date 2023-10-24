package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "items")
data class ItemEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = "",
    var importance: Importance = Importance.NORMAL,
    var isCompleted: Boolean = false,
    var deadline: String? = null,
    var createdDate: String = LocalDate.now().toString(),
    var modifiedDate: String? = null,
)