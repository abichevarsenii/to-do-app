package com.example.todoapp

import android.os.Parcelable
import java.time.LocalDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToDoItem(
    var id: Long,
    var name: String,
    var importance: Importance,
    var isCompleted: Boolean,
    var deadline: LocalDate? = null,
    var createdDate: LocalDate,
    var modifiedDate: LocalDate? = null
) : Parcelable
