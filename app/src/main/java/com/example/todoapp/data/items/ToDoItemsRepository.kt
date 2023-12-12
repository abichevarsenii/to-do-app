package com.example.todoapp.data.items

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.domain.ToDoItem
import java.time.LocalDate
import javax.inject.Inject

class ToDoItemsRepository @Inject constructor(private val dataSource: ToDoItemsDataSource) {

    private val _items = MutableLiveData<List<ToDoItem>>(emptyList())
    private var _isVisibleCompletedItems = MutableLiveData<Boolean>(true)
    val items: LiveData<List<ToDoItem>> = _items
    val isVisibleCompletedItems: LiveData<Boolean> = _isVisibleCompletedItems

    private suspend fun getItems(): List<ToDoItem> {
        val items = dataSource.getAllToDos()?.map { mappingEntityToClass(it) } ?: emptyList()
        return if (isVisibleCompletedItems.value == true) {
            items
        } else {
            items.filter { !it.isCompleted }
        }
    }

    suspend fun updateItems() {
        _items.postValue(getItems())
    }

    suspend fun getItemById(id: Long) : ToDoItem? {
        if (id == -1L) return mappingEntityToClass(ItemEntity())
        return dataSource.getById(id)?.let { mappingEntityToClass(it) }
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
        _isVisibleCompletedItems.value = isVisible ?: (isVisibleCompletedItems.value != true)
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
            id = item.id
            name = item.name
            importance = item.importance
            isCompleted = item.isCompleted
            deadline = item.deadline?.toString()
            createdDate = item.createdDate.toString()
            modifiedDate = item.modifiedDate?.toString()
        }
    }
}