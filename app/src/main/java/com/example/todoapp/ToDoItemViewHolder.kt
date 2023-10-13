package com.example.todoapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ToDoItemBinding

class ToDoViewHolder(binding: ToDoItemBinding) : RecyclerView.ViewHolder(binding.root){
    val name = binding.toDoNameText
    val date = binding.toDoDateText
    val completed = binding.toDoCheckbox
    val infoButton = binding.toDoInfoButton

    fun onBind(toDoItem: ToDoItem, onItemClickCallback : (ToDoItem) -> Unit){
        name.text = toDoItem.name
        completed.isChecked = toDoItem.isCompleted
        if (toDoItem.deadline != null){
            date.visibility = View.VISIBLE
            date.text = toDoItem.deadline.toString()
        } else {
            date.visibility = View.GONE
        }
        infoButton.setOnClickListener {
            onItemClickCallback(toDoItem)
        }
    }

}