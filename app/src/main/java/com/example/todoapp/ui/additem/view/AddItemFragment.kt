package com.example.todoapp.ui.additem.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.todoapp.core.Importance
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentAddItemBinding
import com.example.todoapp.ui.additem.stateholders.AddItemViewModel
import dagger.hilt.android.AndroidEntryPoint


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
        setUpFields()
        binding.closeButton.setOnClickListener { viewModel.onNavigateToList(view) }
        binding.editTextDate.setOnClickListener { viewModel.onDatePicked(requireContext()) }
        binding.nameEditText.addTextChangedListener { viewModel.onNameTextChanged(it) }
        binding.saveButton.setOnClickListener {
            viewModel.onItemAdded()
            viewModel.onNavigateToList(view)
        }
        binding.importanceSpinner.setSelection(Importance.NORMAL.ordinal)
        binding.importanceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapter: AdapterView<*>?,
                    view: View?,
                    i: Int,
                    l: Long
                ) {
                    viewModel.onImportanceChanged(Importance.values()[i])
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    viewModel.onImportanceChanged(Importance.NORMAL)
                }
            }
        binding.deadlineSwitch.setOnCheckedChangeListener { _, value ->
            binding.editTextDate.visibility = if (value) View.VISIBLE else View.GONE
            viewModel.onDeadlineEnablingChanged(value)
        }
        binding.deleteButton.setOnClickListener {
            viewModel.onDeleteItem(args.id)
            viewModel.onNavigateToList(view)
        }
        viewModel.date.observe(viewLifecycleOwner) {
            binding.editTextDate.setText(it ?: getString(R.string.select_date))
        }
    }

    private fun setUpFields() {
        viewModel.item.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.nameEditText.setText(it.name)
                binding.saveButton.isEnabled = true
                if (it.deadline != null) {
                    binding.deadlineSwitch.isChecked = true
                    binding.editTextDate.setText(it.deadline.toString())
                } else {
                    binding.deadlineSwitch.isChecked = false
                }
                binding.importanceSpinner.setSelection(it.importance.ordinal)
            }
        }
        viewModel.onItemLoaded(args.id)
    }

}