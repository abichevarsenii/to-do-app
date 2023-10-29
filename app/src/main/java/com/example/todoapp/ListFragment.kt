package com.example.todoapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.FragmentListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding
    lateinit var scope: CoroutineScope
    private var isVisibleCompletedItems = true
    private val myApp: MyApp by lazy {
        activity?.application as MyApp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scope = CoroutineScope(Dispatchers.Main)
        binding.addToDoButton.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToAddItemFragment()
            view.findNavController().navigate(action)
        }
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        val adapter = ToDoItemAdapter(
            {
                val action = ListFragmentDirections.actionListFragmentToAddItemFragment(it.id)
                view.findNavController().navigate(action)
            },
            { value, id ->
                scope.launch {
                    val item = myApp.repository.getToDo(id)
                    if (item != null) {
                        item.isCompleted = value
                        myApp.repository.addToDo(item)
                    }
                }
            })
        scope.launch {
            val items = myApp.repository.getToDos()
            adapter.itemsList = items
        }
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        binding.showCompletedButton.setOnClickListener {
            isVisibleCompletedItems = !isVisibleCompletedItems
            scope.launch {
                val items = myApp.repository.getToDos()
                if (isVisibleCompletedItems){
                    adapter.itemsList = items
                } else {
                    adapter.itemsList = items.filter { !it.isCompleted }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}