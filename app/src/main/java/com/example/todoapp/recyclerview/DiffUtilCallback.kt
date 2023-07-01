package com.example.todoapp.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.example.todoapp.database.TodoItem

class DiffUtilCallback(
    private val oldList: List<TodoItem>,
    private val newList: List<TodoItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldId = oldList[oldItemPosition].id
        val newId = newList[newItemPosition].id
        return oldId == newId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}