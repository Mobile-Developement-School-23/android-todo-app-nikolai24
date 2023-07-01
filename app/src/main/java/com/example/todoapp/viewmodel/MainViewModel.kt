package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.database.Importance
import com.example.todoapp.database.TodoItem
import com.example.todoapp.database.TodoDatabase
import com.example.todoapp.repository.Repository
import com.example.todoapp.utils.IdGenerator.getNewId
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val allItems: LiveData<List<TodoItem>>

    init {
        val todoDao = TodoDatabase.getDatabase(application).todoDao()
        repository = Repository(todoDao, application.applicationContext)
        allItems = repository.allItems.asLiveData()
    }

    fun dataUpdate() = viewModelScope.launch {
        repository.dataUpdate()
    }

    fun saveItem(item: TodoItem) {
        if (item.id == "") {
            item.id = getNewId()
            insert(item)
        } else {
            update(item)
        }
    }

    fun deleteItem(item: TodoItem) {
        delete(item)
    }

    private fun insert(item: TodoItem) = viewModelScope.launch {
        repository.insert(item)
    }

    private fun update(item: TodoItem) = viewModelScope.launch {
        repository.update(item)
    }

    private fun delete(item: TodoItem) = viewModelScope.launch {
        repository.delete(item)
    }

    fun getItem(id: String): TodoItem {
        val newItem = TodoItem("", "", Importance.COMMON, null, false, null, null)
        if (id == "") {
            return newItem
        } else {
            return getItemByID(id)
        }
    }

    fun getItemByID(id: String): TodoItem {
        return repository.getItemByID(id)
    }
}