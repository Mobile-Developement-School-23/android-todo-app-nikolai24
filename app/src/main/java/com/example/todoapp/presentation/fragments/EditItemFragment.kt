package com.example.todoapp.presentation.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.*
import com.example.todoapp.app.App
import com.example.todoapp.data.database.Importance
import com.example.todoapp.data.database.TodoItem
import com.example.todoapp.databinding.FragmentEditItemBinding
import com.example.todoapp.di.components.EditItemFragmentComponent
import com.example.todoapp.utils.DateConverter
import com.example.todoapp.presentation.viewmodel.MainViewModel
import com.example.todoapp.presentation.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class EditItemFragment : Fragment(), MenuProvider {

    @Inject
    lateinit var vmFactory: MainViewModelFactory
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentEditItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var controller: NavController
    private val args: EditItemFragmentArgs by navArgs()
    private lateinit var item: TodoItem
    private var importance = Importance.COMMON
    private var id = ""
    private var position = 0
    private var deadline: Long? = null
    private lateinit var component: EditItemFragmentComponent

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
        settingActionBar()
        controller = findNavController()
        component = (activity?.application as App).appComponent.editItemFragmentComponent()
        component.inject(this)
        mainViewModel = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)
        id = args.id
        position = args.position
        item = mainViewModel.getItem(id)
        deleteButtonInit()
        spinnerCreated()
        switchCreated()
        setDeadline()
    }

    private fun deleteButtonInit(){
        if (id == "") {
            binding.deleteButton.setOnClickListener {
                startMainActivity()
            }
        } else {
            binding.deleteButton.setOnClickListener {
                mainViewModel.deleteItem(item)
                showSnackbar(item.text)
                startMainActivity()
            }
            binding.description.setText(item.text)
            importance = item.importance
        }
    }

    private fun showSnackbar(name: String){
        Snackbar.make(binding.root, resources.getString(R.string.delete_title) + name, 5000).apply {
            setAction(resources.getString(R.string.cancel)){
                mainViewModel.restoreItem()
            }.show()
        }
    }

    private fun startMainActivity() {
        controller.navigate(R.id.mainFragment)
    }

    private fun settingActionBar(){
        (activity as AppCompatActivity).supportActionBar?.title = ""
        (activity as AppCompatActivity).supportActionBar?.subtitle = ""
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_close_24)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
            mainViewModel.saveItem(item)
            startMainActivity()
        } else {
            if (menuItem.itemId == android.R.id.home) {
                startMainActivity()
            }
        }
        return false
    }

    private fun spinnerCreated(){
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
    }

    private fun switchCreated(){
        binding.switchCompat.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (deadline == null) {
                    deadline = DateConverter.getLongDate()
                }
                binding.dateText.text = DateConverter.dateConvert(deadline!!)
                binding.cardViewDate.setOnClickListener {
                    datePickerDialog()
                }
            } else {
                deadline = null
                binding.dateText.text = ""
                binding.cardViewDate.setOnClickListener {
                }
            }
        }
    }

    private fun setDeadline(){
        if (item.deadline == null) {
            binding.switchCompat.isChecked = false
        } else {
            deadline = item.deadline
            binding.switchCompat.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}