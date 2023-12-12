package com.example.todoapp.ui.list.view

import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.core.Importance
import com.example.todoapp.R
import com.example.todoapp.domain.ToDoItem
import com.example.todoapp.databinding.ToDoItemBinding


class ToDoViewHolder(binding: ToDoItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val name = binding.toDoNameText
    private val date = binding.toDoDateText
    private val completed = binding.toDoCheckbox
    private val infoButton = binding.toDoInfoButton
    private val priorityIcon = binding.priorityIcon

    fun onBind(
        toDoItem: ToDoItem,
        onItemClickCallback: (ToDoItem) -> Unit,
        onItemCompletedCallback: (Boolean, Long) -> Unit
    ) {
        completed.setOnCheckedChangeListener { _, value ->
            onItemCompletedCallback(value, toDoItem.id!!)
        }
        infoButton.setOnClickListener {
            onItemClickCallback(toDoItem)
        }

        setUpDate(toDoItem)
        setUpPriority(toDoItem)
        setUpCompleted(toDoItem)
        setUpName(toDoItem)
    }

    private fun setUpName(toDoItem: ToDoItem) {
        name.text = toDoItem.name
    }

    private fun setUpCompleted(toDoItem: ToDoItem) {
        changeCompleted(name, toDoItem.isCompleted)
        changeCompleted(date, toDoItem.isCompleted)
        completed.isChecked = toDoItem.isCompleted
    }

    private fun setUpPriority(toDoItem: ToDoItem) {
        priorityIcon.visibility = View.VISIBLE
        when (toDoItem.importance) {
            Importance.LOW -> priorityIcon.setImageResource(R.drawable.priority_low)
            Importance.NORMAL -> priorityIcon.visibility = View.GONE
            Importance.URGENT -> priorityIcon.setImageResource(R.drawable.priority_high)
        }
    }

    private fun setUpDate(toDoItem: ToDoItem) {
        if (toDoItem.deadline != null) {
            date.visibility = View.VISIBLE
            date.text = toDoItem.deadline.toString()
        } else {
            date.visibility = View.GONE
        }
    }

    private fun changeCompleted(textView: TextView, value: Boolean) {
        textView.isEnabled = !value
        if (value){
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

}