package com.example.todoapp.ui.additem.stateholders

import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.example.todoapp.data.items.ToDoItemsRepository
import com.example.todoapp.domain.ToDoItem
import com.example.todoapp.ui.additem.view.AddItemFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(private val itemsRepository: ToDoItemsRepository) :
    ViewModel() {

    private var item: ToDoItem? = null
    private val _date = MutableLiveData<String?>(null)
    public val date : LiveData<String?> = _date

    fun onDeleteItem(id: Long) {
        if (item != null && id == -1L) {
            viewModelScope.launch {
                itemsRepository.deleteToDo(item!!.id)
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
//                if (binding.deadlineSwitch.isChecked) {
//                    item?.deadline = LocalDate.of(year, month, day)
//                }
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    fun onNameTextChanged(name: Editable?) {
        item?.name = name.toString()
    }

    fun onItemAdded() {
        viewModelScope.launch {
            if (item != null) {
                itemsRepository.addToDo(item!!)
            }
        }
    }

    fun onItemLoaded(id: Long) {

    }

    fun onDeadlineEnablingChanged(isEnable: Boolean) {

    }

    fun setUpItem() {

    }


}