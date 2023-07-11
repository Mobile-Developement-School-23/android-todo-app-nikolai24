package com.example.todoapp.presentation.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.database.TodoItem

class TodoDataAdapter(
    private val listener: OnItemClickListener,
    private val checkBoxListener: OnCheckBoxClickListener
) :
    RecyclerView.Adapter<TodoViewHolder>() {

    var listItems: MutableList<TodoItem> = mutableListOf()

    interface OnItemClickListener {
        fun onItemClick(item: TodoItem, position: Int)
    }

    interface OnCheckBoxClickListener {
        fun onItemClick(item: TodoItem, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(listItems[position], listener, position, checkBoxListener)
    }

    fun setList(newList: List<TodoItem>) {
        val diffCallback = DiffUtilCallback(listItems, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listItems.clear()
        listItems.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}