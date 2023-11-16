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
        name.text = toDoItem.name
        completed.isChecked = toDoItem.isCompleted
        if (toDoItem.deadline != null) {
            date.visibility = View.VISIBLE
            date.text = toDoItem.deadline.toString()
        } else {
            date.visibility = View.GONE
        }
        infoButton.setOnClickListener {
            onItemClickCallback(toDoItem)
        }
        priorityIcon.visibility = View.VISIBLE
        when (toDoItem.importance){
            Importance.LOW -> priorityIcon.setImageResource(R.drawable.priority_low)
            Importance.NORMAL -> priorityIcon.visibility = View.GONE
            Importance.URGENT -> priorityIcon.setImageResource(R.drawable.priority_high)
        }
        completed.setOnCheckedChangeListener { _, value ->
            changeCompleted(name, value)
            changeCompleted(date, value)
            toDoItem.isCompleted = value
            onItemCompletedCallback(value, toDoItem.id!!)
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