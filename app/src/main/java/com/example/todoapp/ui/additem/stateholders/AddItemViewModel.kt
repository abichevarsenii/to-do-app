package com.example.todoapp.ui.additem.stateholders

import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.todoapp.core.Importance
import com.example.todoapp.data.items.ToDoItemsRepository
import com.example.todoapp.domain.ToDoItem
import com.example.todoapp.ui.additem.view.AddItemFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar
import java.util.TimeZone
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(private val itemsRepository: ToDoItemsRepository) :
    ViewModel() {

    private var _item = MutableLiveData<ToDoItem?>(null)
    private val _date = MutableLiveData<String?>(null)
    val date : LiveData<String?> = _date
    val item : LiveData<ToDoItem?> = _item

    fun onDeleteItem(id: Long) {
        if (_item.value != null && id != -1L) {
            viewModelScope.launch(Dispatchers.Default) {
                itemsRepository.deleteToDo(id)
            }
        }
    }

    fun onNavigateToList(view : View) {
        val action = AddItemFragmentDirections.actionAddItemFragmentToListFragment()
        view.findNavController().navigate(action)
    }

    fun onDatePicked(context : Context) {
        val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())
        val dialog = DatePickerDialog(
            context, { view, year, month, day ->
                _date.value = "${year}.${month}.${day}"
                _item.value?.deadline = LocalDate.of(year, month, day)
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    fun onNameTextChanged(name: Editable?) {
        _item.value?.name = name.toString()
    }

    fun onImportanceChanged(value: Importance) {
        _item.value?.importance = value
    }

    fun onItemAdded() {
        viewModelScope.launch(Dispatchers.Default) {
            if (_item.value != null) {
                itemsRepository.addToDo(_item.value!!)
            }
        }
    }

    fun onItemLoaded(id: Long) {
        viewModelScope.launch(Dispatchers.Default) {
            _item.postValue(itemsRepository.getItemById(id))
        }
    }

    fun onDeadlineEnablingChanged(isEnable: Boolean) {
        if (!isEnable) {
            _item.value?.deadline = null
            _date.value = null
        }
    }
}