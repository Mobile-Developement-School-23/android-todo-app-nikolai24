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
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.todoapp.R
import com.example.todoapp.TodoItem
import com.example.todoapp.TodoItemsRepository
import com.example.todoapp.adapter.DataAdapter
import com.example.todoapp.databinding.FragmentMainBinding

class MainFragment : Fragment(), MenuProvider {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DataAdapter
    private lateinit var lManager: StaggeredGridLayoutManager
    private lateinit var listTodoItems: MutableList<TodoItem>
    private lateinit var controller: NavController
    private var completedItems = 0
    private var visibility = true

    private val listener: DataAdapter.OnItemClickListener = object: DataAdapter.OnItemClickListener{
        override fun onItemClick(item: TodoItem, position: Int) {
            startEditItemFragment(item.id, position)
        }
    }

    private val checkBoxListener: DataAdapter.OnCheckBoxClickListener = object: DataAdapter.OnCheckBoxClickListener{
        override fun onItemClick(item: TodoItem, position: Int) {
            clickOnCheckBox(item.flag, position, item.id)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =  FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = context?.resources?.getString(R.string.main_title)
        completedItems = TodoItemsRepository.getCounter()
        setSubtitle(completedItems.toString())
        controller = findNavController()
        listTodoItems = TodoItemsRepository.getListItems()
        adapter = DataAdapter(listener, checkBoxListener)
        adapter.setList(listTodoItems)
        binding.recyclerView.adapter = adapter
        lManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = lManager
        binding.fab.setOnClickListener{
            startEditItemFragment("", -1)
        }

        val swipeCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when(direction){
                    ItemTouchHelper.LEFT -> {
                        if (visibility) {
                            TodoItemsRepository.deleteItem(position)
                        } else {
                            TodoItemsRepository.deleteUnfulfilledItem(position)
                        }
                        if (visibility) {
                            adapter.setList(TodoItemsRepository.getListItems())
                            adapter.notifyDataSetChanged()
                        } else {
                            adapter.setList(TodoItemsRepository.getListUnfulfilledItems())
                            adapter.notifyDataSetChanged()
                        }
                        completedItems = TodoItemsRepository.getCounter()
                        setSubtitle(completedItems.toString())
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
                var p = Paint().also {it.color = ResourcesCompat.getColor(resources, R.color.vivid_red, null)}
                val icon = (ResourcesCompat.getDrawable(resources, R.drawable.baseline_delete_24, null) as VectorDrawable).toBitmap()
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
        if (menuItem.itemId == R.id.actionVisibility){
            val title = menuItem.title
            if (title == context?.resources?.getString(R.string.visible)) {
                menuItem.title = context?.resources?.getString(R.string.not_visible)
                visibility = false
                menuItem.setIcon(R.drawable.baseline_visibility_24)
                val newList = TodoItemsRepository.getListUnfulfilledItems()
                adapter.setList(newList)
            } else {
                menuItem.title = context?.resources?.getString(R.string.visible)
                visibility = true
                menuItem.setIcon(R.drawable.baseline_visibility_off_24)
                val newList = TodoItemsRepository.getListItems()
                adapter.setList(newList)
            }
            return true
        }
        return false
    }

    private fun startEditItemFragment(id: String, position: Int){
        val action = MainFragmentDirections.actionMainFragmentToEditItemFragment(id, position)
        controller.navigate(action)
    }

    private fun clickOnCheckBox(flag: Boolean, position: Int, id: String){
        TodoItemsRepository.setFlagById(flag, id)
        if (flag){
            TodoItemsRepository.reduceNumber()
        } else {
            TodoItemsRepository.addNumber()
        }
        completedItems = TodoItemsRepository.getCounter()
        setSubtitle(completedItems.toString())
        if (visibility) {
            adapter.notifyItemChanged(position)
        } else {
            val newList = TodoItemsRepository.getListUnfulfilledItems()
            adapter.setList(newList)
        }
    }

    private fun setSubtitle(s: String){
        (activity as AppCompatActivity).supportActionBar?.subtitle = context?.resources?.getString(R.string.sub_title) + s
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}