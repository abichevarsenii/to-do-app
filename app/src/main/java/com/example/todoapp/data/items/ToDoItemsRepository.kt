package com.example.todoapp.data.items

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.core.Importance
import com.example.todoapp.data.items.api.DatabaseApi
import com.example.todoapp.domain.ToDoItem
import java.time.LocalDate
import javax.inject.Inject

class ToDoItemsRepository @Inject constructor(private val dataSource: ToDoItemsDataSource) {

    private val _items = MutableLiveData<List<ToDoItem>>(emptyList())
    public val items: LiveData<List<ToDoItem>> = _items
    private var isVisibleCompletedItems = true

    private suspend fun getItems(): List<ToDoItem> {
        val items = dataSource.getAllToDos()?.map { mappingEntityToClass(it) } ?: emptyList()
        return if (isVisibleCompletedItems) {
            items
        } else {
            items.filter { !it.isCompleted }
        }
    }

    suspend fun updateItems() {
        _items.value = getItems()
    }

    suspend fun changeItemCompletedState(id: Long, completed: Boolean) {
        val item = dataSource.getById(id)
        if (item != null) {
            item.isCompleted = completed
            dataSource.insert(item)
            updateItems()
        }
    }

    suspend fun changeVisibilityCompleted(isVisible: Boolean? = null) {
        isVisibleCompletedItems = isVisible ?: !isVisibleCompletedItems
        updateItems()
    }

    suspend fun addToDo(item: ToDoItem) {
        dataSource.insert(mappingClassToEntity(item))
        updateItems()
    }

    suspend fun deleteToDo(id: Long) {
        dataSource.deleteById(id)
        updateItems()
    }

    private fun mappingEntityToClass(entity: ItemEntity): ToDoItem {
        return ToDoItem(
            id = entity.id,
            name = entity.name,
            importance = entity.importance,
            isCompleted = entity.isCompleted,
            deadline = entity.deadline?.let { LocalDate.parse(entity.deadline) },
            createdDate = LocalDate.parse(entity.createdDate),
            modifiedDate = entity.modifiedDate?.let { LocalDate.parse(entity.modifiedDate) }
        )
    }

    private fun mappingClassToEntity(item: ToDoItem): ItemEntity {
        return ItemEntity().apply {
            id = item.id!!
            name = item.name
            importance = item.importance
            isCompleted = item.isCompleted
            deadline = item.deadline?.toString()
            createdDate = item.createdDate.toString()
            modifiedDate = item.modifiedDate?.toString()
        }
    }
}