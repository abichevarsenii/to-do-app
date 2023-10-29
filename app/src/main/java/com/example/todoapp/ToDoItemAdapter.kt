package com.example.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.ToDoItemBinding


class ToDoItemAdapter(
    private val onItemClickCallback: (ToDoItem) -> Unit,
    private val onItemCheckedCallback: (Boolean, Long) -> Unit,
    ) : RecyclerView.Adapter<ToDoViewHolder>() {

    var itemsList = listOf<ToDoItem>()
        set(value) {
            val callback = CommonCallbackImpl(
                oldItems = field,
                newItems = value,
                { oldItem: ToDoItem, newItem -> oldItem == newItem })
            field = value
            val diffResult = DiffUtil.calculateDiff(callback)
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val holder = ToDoViewHolder(
            ToDoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
        return holder
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        holder.onBind(itemsList[position], onItemClickCallback, onItemCheckedCallback)
    }
}