package com.example.todoapp.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.database.TodoItem

class DataAdapter(
    private val listener: OnItemClickListener,
    private val checkBoxListener: OnCheckBoxClickListener
) :
    RecyclerView.Adapter<ViewHolder>() {

    var listItems: MutableList<TodoItem> = mutableListOf()

    interface OnItemClickListener {
        fun onItemClick(item: TodoItem, position: Int)
    }

    interface OnCheckBoxClickListener {
        fun onItemClick(item: TodoItem, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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