package com.example.todoapp.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todoapp.R
import com.example.todoapp.database.TodoItem
import com.example.todoapp.recyclerview.DataAdapter
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.recyclerview.SwipeCallback
import com.example.todoapp.utils.DateConverter
import com.example.todoapp.utils.NetworkCheck.isNetworkAvailable
import com.example.todoapp.viewmodel.MainViewModel

class MainFragment : Fragment(), MenuProvider {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DataAdapter
    private lateinit var lManager: StaggeredGridLayoutManager
    private lateinit var controller: NavController
    private var completedItems = 0
    private lateinit var mainViewModel: MainViewModel

    private val listener: DataAdapter.OnItemClickListener =
        object : DataAdapter.OnItemClickListener {
            override fun onItemClick(item: TodoItem, position: Int) {
                startEditItemFragment(item.id, position)
            }
        }

    private val checkBoxListener: DataAdapter.OnCheckBoxClickListener =
        object : DataAdapter.OnCheckBoxClickListener {
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
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title =
            context?.resources?.getString(R.string.main_title)
        controller = findNavController()
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        adapter = DataAdapter(listener, checkBoxListener)
        mainViewModel.allItems.observe(viewLifecycleOwner, Observer { items ->
            completedItems = items.filter { it.isCompleted }.size
            setSubtitle(completedItems.toString())
            adapter.setList(items)
        })
        binding.apply {
            recyclerView.adapter = adapter
            lManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            recyclerView.layoutManager = lManager
            fab.setOnClickListener {
                startEditItemFragment("", -1)
            }
        }
        val swipeCallback = SwipeCallback(swipeToDelete)
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.actionVisibility) {
            return true
        }
        if (menuItem.itemId == R.id.actionNetwork) {
            if (isNetworkAvailable(requireActivity().applicationContext)) {
                mainViewModel.dataUpdate()
                Toast.makeText(
                    requireActivity().applicationContext,
                    context?.resources?.getString(R.string.request_sent),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireActivity().applicationContext,
                    context?.resources?.getString(R.string.no_internet),
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

    private fun clickOnCheckBox(item: TodoItem, flag: Boolean, position: Int, id: String) {
        item.isCompleted = !flag
        item.modifiedAt = DateConverter.getLongDate()
        mainViewModel.saveItem(item)
        adapter.notifyItemChanged(position)
    }

    private fun swipeToDelete(position: Int) {
        var item = adapter.listItems[position]
        mainViewModel.deleteItem(item)
    }

    private fun setSubtitle(s: String) {
        (activity as AppCompatActivity).supportActionBar?.subtitle =
            context?.resources?.getString(R.string.sub_title) + s
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}