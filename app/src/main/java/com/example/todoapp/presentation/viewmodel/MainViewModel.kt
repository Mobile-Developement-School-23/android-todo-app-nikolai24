package com.example.todoapp.presentation.viewmodel

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.database.Importance
import com.example.todoapp.data.database.TodoItem
import com.example.todoapp.data.repository.TodoItemsRepository
import com.example.todoapp.utils.AppConstants
import com.example.todoapp.utils.IdGenerator.getNewId
import com.example.todoapp.utils.RestoreTodoItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: TodoItemsRepository) : ViewModel() {

    private val _allItems = repository.allItems.asLiveData()
    val allItems = _allItems

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

    fun restoreItem() {
        var item = RestoreTodoItem.getItem()
        insert(item)
    }

    fun deleteItem(item: TodoItem) {
        RestoreTodoItem.setItem(item)
        delete(item)
    }

    fun setTheme(theme: String){
        when (theme){
            AppConstants.LIGHT_THEME -> setLightTheme()
            AppConstants.DARK_THEME -> setDarkTheme()
            AppConstants.SYSTEM_THEME -> setSystemTheme()
        }
        repository.saveTheme(theme)
    }

    fun getTheme(): String? {
        return repository.getTheme()
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

    private fun getItemByID(id: String): TodoItem {
        return repository.getItemByID(id)
    }

    private fun setLightTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setDarkTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun setSystemTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}