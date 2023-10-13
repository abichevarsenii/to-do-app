package com.example.todoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.databinding.FragmentAddItemBinding
import com.google.android.material.internal.TextWatcherAdapter
import java.time.LocalDate
import java.util.*
import kotlin.random.Random


class AddItemFragment : Fragment() {

    lateinit var binding: FragmentAddItemBinding
    private var item = ToDoItemsRepository().getDefaultItem()
    val args: AddItemFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val action = AddItemFragmentDirections.actionAddItemFragmentToListFragment()
        if (args.id != -1) {
            val item = ToDoItemsRepository().getToDo(args.id)
            binding.nameEditText.setText(item.name)
        }
        binding.closeButton.setOnClickListener {
            view.findNavController().navigate(action)
        }
        binding.editTextDate.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())
            val dialog = DatePickerDialog(
                context!!, { view, year, month, day ->
                    binding.editTextDate.setText("${year}.${month}.${day}")
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
        binding.nameEditText.addTextChangedListener({ _, _, _, _ -> }, { _, _, _, _ -> },
            {
                item.name = it.toString()
            }
        )
    }

    private fun isCorrectData(): Boolean {
        return binding.nameEditText.text.toString() != ""
    }


}