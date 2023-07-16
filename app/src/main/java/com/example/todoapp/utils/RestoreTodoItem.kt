package com.example.todoapp.utils

import com.example.todoapp.data.database.Importance
import com.example.todoapp.data.database.TodoItem

object RestoreTodoItem {
    private var item = TodoItem(
        id = "", text = "", importance = Importance.COMMON,
        deadline = null, isCompleted = true,
        createdAt = null, modifiedAt = null
    )

    private var deleteFlag = false

    private var name = ""

    fun getItem(): TodoItem {
        return item
    }

    fun setItem(deleteItem: TodoItem) {
        item = deleteItem
    }

    fun getDeleteFlag(): Boolean{
        return deleteFlag
    }

    fun setDeleteFlag(flag: Boolean){
        deleteFlag = flag
    }

    fun getName(): String {
        return name
    }

    fun setName(deleteName: String) {
        name = deleteName
    }
}