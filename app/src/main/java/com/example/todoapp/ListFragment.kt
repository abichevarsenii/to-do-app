package com.example.todoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.FragmentListBinding


class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val action = ListFragmentDirections.actionListFragmentToAddItemFragment()
        binding.addToDoButton.setOnClickListener {
            view.findNavController().navigate(action)
        }
        val layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        val items = ToDoItemsRepository().getToDos()
        val adapter = ToDoItemAdapter {
            val action = ListFragmentDirections.actionListFragmentToAddItemFragment(it.id)
            view.findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        adapter.itemsList = items
    }
}