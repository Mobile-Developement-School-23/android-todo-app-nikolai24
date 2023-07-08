package com.example.todoapp.data.repository

import android.content.Context
import com.example.todoapp.data.retrofit.TodoApiImpl
import com.example.todoapp.data.database.TodoItem
import com.example.todoapp.data.database.TodoDao
import com.example.todoapp.utils.AppConstants.KEY_DELETE
import com.example.todoapp.utils.AppConstants.KEY_INSERT
import com.example.todoapp.utils.AppConstants.KEY_UPDATE
import com.example.todoapp.utils.AppConstants.TODO_PREF
import com.example.todoapp.utils.NetworkCheck.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * The repository handles data operations.
 */
class Repository @Inject constructor(
    private val networkService: TodoApiImpl,
    private val todoDao: TodoDao,
    private val context: Context
) {

    val allItems: Flow<List<TodoItem>> = todoDao.getItems()

    suspend fun insert(item: TodoItem) {
        withContext(Dispatchers.IO) {
            todoDao.insert(item)
        }
        val prefs = context.getSharedPreferences(TODO_PREF, Context.MODE_PRIVATE)
        var insertStr = prefs.getString(KEY_INSERT, "")
        val id = item.id
        insertStr = insertStr + " $id"
        val editor = prefs.edit()
        editor.putString(KEY_INSERT, insertStr)
        editor.apply()
        if (isNetworkAvailable(context)) {
            networkService.post(item)
        }
    }

    suspend fun update(item: TodoItem) {
        withContext(Dispatchers.IO) {
            todoDao.update(item)
        }
        val prefs = context.getSharedPreferences(TODO_PREF, Context.MODE_PRIVATE)
        var insertStr = prefs.getString(KEY_UPDATE, "")
        val id = item.id
        insertStr += " $id"
        val editor = prefs.edit()
        editor.putString(KEY_UPDATE, insertStr)
        editor.apply()
        if (isNetworkAvailable(context)) {
            networkService.put(item)
        }
    }

    suspend fun delete(item: TodoItem) {
        withContext(Dispatchers.IO) {
            todoDao.delete(item)
        }
        val prefs = context.getSharedPreferences(TODO_PREF, Context.MODE_PRIVATE)
        var insertStr = prefs.getString(KEY_DELETE, "")
        val id = item.id
        insertStr += " $id"
        val editor = prefs.edit()
        editor.putString(KEY_DELETE, insertStr)
        editor.apply()
        if (isNetworkAvailable(context)) {
            networkService.delete(item)
        }
    }

    fun getItemByID(id: String): TodoItem {
        return todoDao.getItemByID(id)
    }

    suspend fun dataUpdate() {
        if (isNetworkAvailable(context)) {
            var listRes = networkService.getList().toMutableList()
            var listDB = withContext(Dispatchers.IO) {
                todoDao.getList().toMutableList()
            }
            val prefs = context.getSharedPreferences(TODO_PREF, Context.MODE_PRIVATE)
            val idInsert = prefs.getString(KEY_INSERT, "")!!.trim().split(" ")
            val idUpdate = prefs.getString(KEY_UPDATE, "")!!.trim().split(" ").toSet().toList()
            val idDelete = prefs.getString(KEY_DELETE, "")!!.trim().split(" ")
            prefs.edit().clear().commit()
            for (i in 0 until idInsert.size) {
                var id = idInsert[i]
                var item = listDB.firstOrNull { it.id == id }
                if (item != null) {
                    listRes.add(item)
                }
            }
            for (i in 0 until idUpdate.size) {
                var id = idUpdate[i]
                var item = listDB.firstOrNull { it.id == id }
                var oldItem = listRes.firstOrNull { it.id == id }
                if (oldItem != null) {
                    listRes.remove(oldItem)
                }
                if (item != null) {
                    listRes.add(item)
                }
            }
            for (i in 0 until idDelete.size) {
                var id = idDelete[i]
                var item = listRes.firstOrNull { it.id == id }
                if (item != null) {
                    listRes.remove(item)
                }
            }
            networkService.updateList(listRes)
            withContext(Dispatchers.IO) {
                todoDao.deleteAll()
                todoDao.insertAll(listRes)
            }
        }
    }
}
