package com.example.todoapp.ui.list.stateholders

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.todoapp.data.items.ToDoItemsRepository
import com.example.todoapp.ui.list.view.ListFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val itemsRepository: ToDoItemsRepository) : ViewModel() {
    val items = itemsRepository.items
    val completedTaskCountString = (items.map { list -> list.count { it.isCompleted }.toString() } )
    val isVisibleCompletedItems = itemsRepository.isVisibleCompletedItems

    fun updateItems(){
        viewModelScope.launch {
            itemsRepository.updateItems()
        }
    }

    fun onToDoCompletedChanged(id : Long, value: Boolean){
        viewModelScope.launch {
            itemsRepository.changeItemCompletedState(id, value)
        }
    }

    fun onToDoChangedClicked(id : Long, view: View){
        val action = ListFragmentDirections.actionListFragmentToAddItemFragment(id)
        view.findNavController().navigate(action)
    }

    fun onChangeVisibilityCompleted(){
        viewModelScope.launch {
            itemsRepository.changeVisibilityCompleted()
        }
    }
}