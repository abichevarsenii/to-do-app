package com.example.todoapp.data.items
import javax.inject.Inject

class ToDoItemsDataSource @Inject constructor(database: AppDatabase) {

    private val api = database.itemsApi()

    suspend fun insert(item: ItemEntity) : Long {
        return api.insert(item)
    }

    suspend fun delete(item: ItemEntity) {
        api.delete(item)
    }

    suspend fun deleteById(id: Long) {
        api.deleteById(id)
    }

    suspend fun getAllToDos(): List<ItemEntity>? {
        return api.getAllToDos()
    }

    suspend fun getById(id: Long): ItemEntity? {
        return api.getById(id)
    }
}