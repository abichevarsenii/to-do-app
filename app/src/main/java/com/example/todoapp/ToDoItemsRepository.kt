package com.example.todoapp

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
            deadline = entity.deadline,
            createdDate = entity.createdDate,
            modifiedDate = entity.modifiedDate
        )
    }

    private fun mappingClassToEntity(item: ToDoItem): ItemEntity {
        return ItemEntity().apply {
            name = item.name
            importance = item.importance
            isCompleted = item.isCompleted
            deadline = item.deadline
            createdDate = item.createdDate
            modifiedDate = item.modifiedDate
        }
    }

    public suspend fun getToDos(): List<ToDoItem> {
        return databaseAPI.getAllToDos()?.map { mappingEntityToClass(it) } ?: emptyList()
    }

    public suspend fun getToDo(id: Int): ToDoItem? {
        return databaseAPI.getById(id)?.map { mappingEntityToClass(it) }?.last()
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