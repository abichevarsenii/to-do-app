package com.example.todoapp

import java.time.LocalDate
import java.util.*
import kotlin.random.Random

class ToDoItemsRepository {

    private val todos = listOf(
        ToDoItem(
            0,
            "Дело 1",
            Importance.NORMAL,
            isCompleted = false,
            createdDate = LocalDate.now()
        )
    )

    public fun getToDos(): List<ToDoItem> {
        return todos
    }

    public fun getToDo(id : Int) : ToDoItem{
        return todos[id]
    }

    public fun getDefaultItem() : ToDoItem {
        return ToDoItem(
            id = Random(42).nextInt(10_000),
            name = "",
            importance = Importance.NORMAL,
            isCompleted = false,
            createdDate = LocalDate.now()
        )
    }
}