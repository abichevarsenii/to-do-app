package com.example.todoapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
class ItemEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    var name: String = ""
    var importance: Importance = Importance.NORMAL
    var isCompleted: Boolean = false
    var deadline: LocalDate? = null
    var createdDate: LocalDate = LocalDate.now()
    var modifiedDate: LocalDate? = null
}