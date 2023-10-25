package com.example.todoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.databinding.FragmentAddItemBinding
import kotlinx.coroutines.*
import java.time.LocalDate
import java.util.*


class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private var item: ToDoItem? = null
    private val args: AddItemFragmentArgs by navArgs()
    private val myApp: MyApp by lazy {
        activity?.application as MyApp
    }
    private val scope: CoroutineScope by lazy {
        CoroutineScope(Dispatchers.Default)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val action = AddItemFragmentDirections.actionAddItemFragmentToListFragment()
        if (args.id != -1L) {
            initialization()
        } else {
            defaultInitialization()
        }
        binding.closeButton.setOnClickListener {
            if (item != null) {
                scope.launch {
                    myApp.repository.deleteToDo(item!!.id)
                }
            }
            view.findNavController().navigate(action)
        }
        binding.editTextDate.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance(TimeZone.getDefault())
            val dialog = DatePickerDialog(
                context!!, { view, year, month, day ->
                    binding.editTextDate.setText("${year}.${month}.${day}")
                    if (binding.deadlineSwitch.isChecked) {
                        item?.deadline = LocalDate.of(year, month, day)
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
        binding.nameEditText.addTextChangedListener({ _, _, _, _ -> }, { _, _, _, _ -> },
            {
                item?.name = it.toString()
            }
        )
        binding.saveButton.setOnClickListener {
            scope.launch {
                if (item != null) {
                    myApp.repository.addToDo(item!!)
                }
            }
            view.findNavController().navigate(action)
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
                    myApp.repository.deleteToDo(args.id)
                }
            }
            view.findNavController().navigate(action)
        }
    }

    private fun defaultInitialization() {
        scope.launch {
            item = myApp.repository.getDefaultItem()
            withContext(Dispatchers.Main) {
                binding.saveButton.isEnabled = true
            }
        }
    }

    private fun initialization() {
        scope.launch {
            item = myApp.repository.getToDo(args.id)
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

    override fun onDestroyView() {
        super.onDestroyView()
        scope.cancel()
    }

    private fun isCorrectData(): Boolean {
        return binding.nameEditText.text.toString() != ""
    }


}