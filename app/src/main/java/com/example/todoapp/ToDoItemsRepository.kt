package com.example.todoapp

import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

class ToDoItemsRepository(val databaseAPI: DatabaseAPI) {

    private fun mappingEntityToClass(entity: ItemEntity): ToDoItem {
        return ToDoItem(
            id = entity.id,
            name = entity.name,
            importance = entity.importance,
            isCompleted = entity.isCompleted,
            deadline = entity.deadline?.let { LocalDate.parse(entity.deadline) },
            createdDate = LocalDate.parse(entity.createdDate),
            modifiedDate = entity.modifiedDate?.let { LocalDate.parse(entity.modifiedDate)}
        )
    }

    private fun mappingClassToEntity(item: ToDoItem): ItemEntity {
        return ItemEntity().apply {
            id = item.id
            name = item.name
            importance = item.importance
            isCompleted = item.isCompleted
            deadline = item.deadline?.toString()
            createdDate = item.createdDate.toString()
            modifiedDate = item.modifiedDate?.toString()
        }
    }

    public suspend fun getToDos(): List<ToDoItem> {
        return databaseAPI.getAllToDos()?.map { mappingEntityToClass(it) } ?: emptyList()
    }

    public suspend fun getToDo(id: Long): ToDoItem? {
        return databaseAPI.getById(id)?.let { mappingEntityToClass(it) }
    }

    public suspend fun addToDo(item: ToDoItem) {
        databaseAPI.insert(mappingClassToEntity(item))
    }

    public suspend fun getDefaultItem(): ToDoItem {
        val id = databaseAPI.insert(ItemEntity())
        return ToDoItem(
            id = id,
            name = "",
            importance = Importance.NORMAL,
            isCompleted = false,
            createdDate = LocalDate.now()
        )
    }
}