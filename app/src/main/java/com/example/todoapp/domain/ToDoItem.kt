package com.example.todoapp.domain

import android.os.Parcelable
import com.example.todoapp.core.Importance
import java.time.LocalDate
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToDoItem(
    var id: Long?,
    var name: String,
    var importance: Importance,
    var isCompleted: Boolean,
    var deadline: LocalDate? = null,
    var createdDate: LocalDate,
    var modifiedDate: LocalDate? = null
) : Parcelable
