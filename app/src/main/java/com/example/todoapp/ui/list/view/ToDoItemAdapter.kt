package com.example.todoapp.ui.list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todoapp.domain.ToDoItem
import com.example.todoapp.databinding.ToDoItemBinding


class ToDoItemAdapter(
    private val diffCalculator : ItemDiffCalculator,
    private val onItemClickCallback: (ToDoItem) -> Unit,
    private val onItemCheckedCallback: (Boolean, Long) -> Unit,
    ) : ListAdapter<ToDoItem,ToDoViewHolder>(diffCalculator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        return ToDoViewHolder(
            ToDoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.onBind(getItem(position), onItemClickCallback, onItemCheckedCallback)
    }
}