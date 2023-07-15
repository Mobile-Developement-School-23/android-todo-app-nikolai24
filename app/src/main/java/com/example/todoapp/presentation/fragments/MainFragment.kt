package com.example.todoapp.presentation.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todoapp.R
import com.example.todoapp.app.App
import com.example.todoapp.data.database.TodoItem
import com.example.todoapp.presentation.recyclerview.TodoDataAdapter
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.di.components.MainFragmentComponent
import com.example.todoapp.presentation.recyclerview.SwipeCallback
import com.example.todoapp.utils.DateConverter
import com.example.todoapp.utils.NetworkCheck.isNetworkAvailable
import com.example.todoapp.presentation.viewmodel.MainViewModel
import com.example.todoapp.presentation.viewmodel.MainViewModelFactory
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class MainFragment : Fragment(), MenuProvider {

    @Inject
    lateinit var vmFactory: MainViewModelFactory
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: TodoDataAdapter
    private lateinit var lManager: StaggeredGridLayoutManager
    private lateinit var controller: NavController
    private var completedItems = 0
    private lateinit var component: MainFragmentComponent

    private val listener: TodoDataAdapter.OnItemClickListener =
        object : TodoDataAdapter.OnItemClickListener {
            override fun onItemClick(item: TodoItem, position: Int) {
                startEditItemFragment(item.id, position)
            }
        }

    private val checkBoxListener: TodoDataAdapter.OnCheckBoxClickListener =
        object : TodoDataAdapter.OnCheckBoxClickListener {
            override fun onItemClick(item: TodoItem, position: Int) {
                clickOnCheckBox(item, item.isCompleted, position, item.id)
            }
        }

    private val swipeToDelete: SwipeCallback.SwipeToDelete =
        object : SwipeCallback.SwipeToDelete {
            override fun onItemClick(position: Int) {
                swipeToDelete(position)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner)
        settingActionBar()
        controller = findNavController()
        component = (activity?.application as App).appComponent.mainFragmentComponent()
        component.inject(this)
        mainViewModel = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)
        adapter = TodoDataAdapter(listener, checkBoxListener)
        mainViewModel.allItems.observe(viewLifecycleOwner) { items ->
            completedItems = items.filter { it.isCompleted }.size
            setSubtitle(completedItems.toString())
            adapter.setList(items)
        }
        binding.apply {
            recyclerView.adapter = adapter
            lManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            recyclerView.layoutManager = lManager
            fab.setOnClickListener {
                startEditItemFragment("", -1)
            }
        }
        swipeInit()
    }

    private fun settingActionBar(){
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = context?.resources?.getString(R.string.main_title)
    }

    private fun swipeInit(){
        val swipeCallback = SwipeCallback(swipeToDelete)
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.actionSettings) {
            startSettingsFragment()
            return true
        }
        if (menuItem.itemId == R.id.actionNetwork) {
            if (isNetworkAvailable(requireActivity().applicationContext)) {
                mainViewModel.dataUpdate()
                Toast.makeText(
                    requireActivity().applicationContext,
                    resources.getString(R.string.request_sent),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireActivity().applicationContext,
                    resources.getString(R.string.no_internet),
                    Toast.LENGTH_SHORT
                ).show()
            }
            return true
        }
        return false
    }

    private fun startEditItemFragment(id: String, position: Int) {
        val action = MainFragmentDirections.actionMainFragmentToEditItemFragment(id, position)
        controller.navigate(action)
    }
    private fun startSettingsFragment() {
        val action = MainFragmentDirections.actionMainFragmentToSettingsFragment()
        controller.navigate(action)
    }

    private fun clickOnCheckBox(item: TodoItem, flag: Boolean, position: Int, id: String) {
        item.isCompleted = !flag
        item.modifiedAt = DateConverter.getLongDate()
        mainViewModel.saveItem(item)
        adapter.notifyItemChanged(position)
    }

    private fun swipeToDelete(position: Int) {
        val item = adapter.listItems[position]
        mainViewModel.deleteItem(item)
        runSnackbar(item.text)
    }

    private fun runSnackbar(name: String){
        Snackbar.make(binding.root, resources.getString(R.string.delete_title) + name, 5000).apply {
            setAction(resources.getString(R.string.cancel)){
                mainViewModel.restoreItem()
            }.show()
        }
    }

    private fun setSubtitle(s: String) {
        (activity as AppCompatActivity).supportActionBar?.subtitle =
            resources.getString(R.string.sub_title) + s
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}