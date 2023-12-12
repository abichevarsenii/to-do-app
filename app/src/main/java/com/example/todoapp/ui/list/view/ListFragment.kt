package com.example.todoapp.ui.list.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentListBinding
import com.example.todoapp.ui.list.stateholders.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment() {

    private val viewModel: ListViewModel by viewModels()
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addToDoButton.setOnClickListener { viewModel.onToDoChangedClicked(-1, view) }
        binding.showCompletedButton.setOnClickListener { viewModel.onChangeVisibilityCompleted() }
        viewModel.completedTaskCountString.observe(viewLifecycleOwner) {
            binding.counterTextView.text = "${getString(R.string.completed_count)}: $it"
        }
        viewModel.isVisibleCompletedItems.observe(viewLifecycleOwner){
            binding.showCompletedButton.setImageDrawable(getIcon(it))
            binding.showCompletedButton.setColorFilter(ContextCompat.getColor(requireContext(), getColor(it)), android.graphics.PorterDuff.Mode.SRC_IN)
        }
        setUpRecyclerView(view)
    }

    private fun getIcon(it: Boolean?) =
        AppCompatResources.getDrawable(
            requireContext(),
            if (it == true) R.drawable.visibility else R.drawable.visibility_off
        )

    private fun getColor(it: Boolean?) = if (it == true) R.color.color_blue else R.color.label_disable

    private fun setUpRecyclerView(
        view: View
    ) {
        val adapter = ToDoItemAdapter(
        ItemDiffCalculator(),
        { viewModel.onToDoChangedClicked(it.id!!, view) },
        { value, id -> viewModel.onToDoCompletedChanged(id, value) }
        )
        val layoutManager = LinearLayoutManager( view.context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        viewModel.items.observe(viewLifecycleOwner) { adapter.submitList(it) }
        viewModel.updateItems()
    }
}