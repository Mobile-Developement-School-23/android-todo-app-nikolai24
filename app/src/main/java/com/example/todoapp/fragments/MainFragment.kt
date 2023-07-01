package com.example.todoapp.fragments

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todoapp.R
import com.example.todoapp.database.TodoItem
import com.example.todoapp.recyclerview.DataAdapter
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.retrofit.TodoApiImpl
import com.example.todoapp.utils.NetworkCheck
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

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                var item = adapter.listItems[position]
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        mainViewModel.deleteItem(item)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView: View = viewHolder.itemView
                var p = Paint().also {
                    it.color = ResourcesCompat.getColor(resources, R.color.vivid_red, null)
                }
                val icon = (ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.baseline_delete_24,
                    null
                ) as VectorDrawable).toBitmap()
                c.drawRect(
                    itemView.right.toFloat() + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat(),
                    p
                )
                val iconMarginRight = (dX * -0.1).coerceAtMost(70.0).coerceAtLeast(0.0)
                c.drawBitmap(
                    icon,
                    itemView.right.toFloat() - iconMarginRight.toFloat() - icon.width,
                    itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
                    p
                )
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }
        }
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
        mainViewModel.saveItem(item)
        adapter.notifyItemChanged(position)
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