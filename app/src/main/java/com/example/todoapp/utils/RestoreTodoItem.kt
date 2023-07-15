package com.example.todoapp.utils

import com.example.todoapp.data.database.Importance
import com.example.todoapp.data.database.TodoItem

object RestoreTodoItem {
    private var item = TodoItem("", "", Importance.COMMON, null, true, null, null)

    fun getItem(): TodoItem{
        return item
    }

    fun setItem(deleteItem: TodoItem){
        item = deleteItem
    }
}