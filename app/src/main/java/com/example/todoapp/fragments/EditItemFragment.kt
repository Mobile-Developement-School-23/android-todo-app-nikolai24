package com.example.todoapp.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.*
import com.example.todoapp.databinding.FragmentEditItemBinding
import com.example.todoapp.repository.TodoItemsRepository
import com.example.todoapp.utils.DateConverter

class EditItemFragment : Fragment(), MenuProvider {
    private var _binding: FragmentEditItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var controller: NavController
    private val args: EditItemFragmentArgs by navArgs()
    private lateinit var item: TodoItem
    private var importance = Importance.COMMON
    private var id = ""
    private var position = 0
    private var deadline: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditItemBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner)
        (activity as AppCompatActivity).supportActionBar?.title = ""
        (activity as AppCompatActivity).supportActionBar?.subtitle = ""
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_close_24)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        controller = findNavController()
        id = args.id
        position = args.position
        item = TodoItemsRepository.getItem(position)
        if (id == "") {
            binding.deleteButton.setOnClickListener {
                startMainActivity()
            }
        } else {
            binding.deleteButton.setOnClickListener {
                TodoItemsRepository.deleteItem(position)
                startMainActivity()
            }
            binding.description.setText(item.text)
            importance = item.importance
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.importance_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }

        when (importance) {
            Importance.COMMON -> binding.spinner.setSelection(0)
            Importance.LOW -> binding.spinner.setSelection(1)
            Importance.HIGH -> binding.spinner.setSelection(2)
        }

        binding.switchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (deadline == null) {
                    deadline = DateConverter.getLongDate()
                }
                binding.dateText.text = DateConverter.dateConvert(deadline!!)
                binding.cardViewDate.setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.white,
                        null
                    )
                )
                binding.cardViewDate.setOnClickListener {
                    datePickerDialog()
                }
            } else {
                binding.cardViewDate.setCardBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.min_grey,
                        null
                    )
                )
                deadline = null
                binding.dateText.text = ""
            }
        }

        if (item.deadline == null) {
            binding.cardViewDate.setCardBackgroundColor(
                ResourcesCompat.getColor(
                    resources,
                    R.color.min_grey,
                    null
                )
            )
            binding.switchCompat.isChecked = false
        } else {
            deadline = item.deadline
            binding.switchCompat.isChecked = true
        }
    }

    private fun startMainActivity() {
        controller.navigate(R.id.mainFragment)
    }

    private fun datePickerDialog() {
        var intDate = Triple(0, 0, 0)
        if (deadline == null) {
            deadline = DateConverter.getLongDate()
            intDate = DateConverter.getIntDate()
        } else {
            intDate = DateConverter.getIntDate(deadline!!)
        }
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, myear, mmonth, mday ->
                deadline = DateConverter.getLongDate(myear, mmonth + 1, mday)
                binding.dateText.text = DateConverter.dateConvert(deadline!!)
            },
            intDate.third,
            intDate.second,
            intDate.first
        ).show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.actionSave) {
            item.text = binding.description.text.toString()
            when (binding.spinner.selectedItemPosition) {
                0 -> importance = Importance.COMMON
                1 -> importance = Importance.LOW
                2 -> importance = Importance.HIGH
            }
            item.importance = importance
            item.modifiedAt = DateConverter.getLongDate()
            item.deadline = deadline
            if (position == -1) {
                item.createdAt = item.modifiedAt
            }
            TodoItemsRepository.saveItem(item, position)
            startMainActivity()
        } else {
            if (menuItem.itemId == android.R.id.home) {
                startMainActivity()
            }
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}