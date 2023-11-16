package com.example.todoapp.ui.additem.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.core.Importance
import com.example.todoapp.MyApp
import com.example.todoapp.data.items.ToDoItemsRepository
import com.example.todoapp.domain.ToDoItem
import com.example.todoapp.databinding.FragmentAddItemBinding
import com.example.todoapp.ui.additem.stateholders.AddItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject


@AndroidEntryPoint
class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private val args: AddItemFragmentArgs by navArgs()
    private val viewModel: AddItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.id != -1L) {
            initialization()
        } else {
            defaultInitialization()
        }
        binding.closeButton.setOnClickListener {
            viewModel.onDeleteItem(args.id)
            viewModel.onNavigateToList(view)
        }
        binding.editTextDate.setOnClickListener { viewModel.onDatePicked(requireContext()) }
        binding.nameEditText.addTextChangedListener { viewModel.onNameTextChanged(it) }
        binding.saveButton.setOnClickListener {
            viewModel.onItemAdded()
            viewModel.onNavigateToList(view)
        }
        binding.importanceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    item?.importance = Importance.values()[i]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    item?.importance = Importance.NORMAL
                }

            }
        binding.deadlineSwitch.setOnCheckedChangeListener { _, value ->
            binding.editTextDate.visibility = if (value) View.VISIBLE else View.GONE
            if (!value) {
                item?.deadline = null
            }
        }
        binding.deleteButton.setOnClickListener {
            if (args.id != -1L) {
                scope.launch {
                    repository.deleteToDo(args.id)
                }
            }
            view.findNavController().navigate(action)
        }
    }

    private fun defaultInitialization() {
        scope.launch {
            item = repository.getDefaultItem()
            withContext(Dispatchers.Main) {
                binding.saveButton.isEnabled = true
            }
        }
    }

    private fun initialization() {
        scope.launch {
            item = repository.getToDo(args.id)
            if (item != null) {
                withContext(Dispatchers.Main) {
                    binding.nameEditText.setText(item!!.name)
                    binding.saveButton.isEnabled = true
                    if (item!!.deadline != null) {
                        binding.deadlineSwitch.isChecked = true
                        binding.editTextDate.visibility = View.VISIBLE
                        binding.editTextDate.setText(item!!.deadline.toString())
                    }
                    binding.importanceSpinner.setSelection(item!!.importance.ordinal)
                }
            }
        }
    }

}