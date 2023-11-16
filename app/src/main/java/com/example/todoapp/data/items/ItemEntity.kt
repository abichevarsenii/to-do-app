package com.example.todoapp.data.items

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.todoapp.core.Importance
import java.time.LocalDate

@Entity(tableName = "items")
data class ItemEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String = "",
    var importance: Importance = Importance.NORMAL,
    var isCompleted: Boolean = false,
    var deadline: String? = null,
    var createdDate: String = LocalDate.now().toString(),
    var modifiedDate: String? = null,
)