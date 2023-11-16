package com.example.todoapp.ui.list.view

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.domain.ToDoItem

class ItemDiffCalculator :  DiffUtil.ItemCallback<ToDoItem>()  {
    override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
        return oldItem == newItem
    }
}