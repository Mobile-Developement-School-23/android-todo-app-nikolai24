package com.example.todoapp.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.annotation.WorkerThread
import com.example.todoapp.database.TodoItem
import com.example.todoapp.database.TodoDao
import com.example.todoapp.retrofit.TodoApiImpl
import com.example.todoapp.utils.AppConstants.KEY_DELETE
import com.example.todoapp.utils.AppConstants.KEY_INSERT
import com.example.todoapp.utils.AppConstants.KEY_UPDATE
import com.example.todoapp.utils.AppConstants.TODO_PREF
import com.example.todoapp.utils.NetworkCheck.isNetworkAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class Repository(private val wordDao: TodoDao, private val context: Context) {

    val allItems: Flow<List<TodoItem>> = wordDao.getItems()
    
    suspend fun insert(item: TodoItem) {
        withContext(Dispatchers.IO) {
            wordDao.insert(item)
        }
        var prefs = context.getSharedPreferences(TODO_PREF, Context.MODE_PRIVATE )
        var insertStr = prefs.getString(KEY_INSERT, "")
        var id = item.id
        insertStr = insertStr + " $id"
        val editor = prefs.edit()
        editor.putString(KEY_INSERT, insertStr)
        editor.apply()
        if (isNetworkAvailable(context)){
            TodoApiImpl.post(item)
        }
    }

    suspend fun update(item: TodoItem) {
        withContext(Dispatchers.IO) {
            wordDao.update(item)
        }
        var prefs = context.getSharedPreferences(TODO_PREF, Context.MODE_PRIVATE )
        var insertStr = prefs.getString(KEY_UPDATE, "")
        var id = item.id
        insertStr += " $id"
        val editor = prefs.edit()
        editor.putString(KEY_UPDATE, insertStr)
        editor.apply()
        if (isNetworkAvailable(context)){
            TodoApiImpl.put(item)
        }
    }

    suspend fun delete(item: TodoItem) {
        withContext(Dispatchers.IO) {
            wordDao.delete(item)
        }
        var prefs = context.getSharedPreferences(TODO_PREF, Context.MODE_PRIVATE )
        var insertStr = prefs.getString(KEY_DELETE, "")
        var id = item.id
        insertStr += " $id"
        val editor = prefs.edit()
        editor.putString(KEY_DELETE, insertStr)
        editor.apply()
        if (isNetworkAvailable(context)){
            TodoApiImpl.delete(item)
        }
    }

    fun getItemByID(id: String): TodoItem {
        return wordDao.getItemByID(id)
    }

    suspend fun dataUpdate(){
        if (isNetworkAvailable(context)){
            var listRes = TodoApiImpl.getList().toMutableList()
            var listDB = wordDao.getList().toMutableList()
            val prefs = context.getSharedPreferences(TODO_PREF, Context.MODE_PRIVATE )
            val idInsert = prefs.getString(KEY_INSERT, "")!!.trim().split(" ")
            val idUpdate = prefs.getString(KEY_UPDATE, "")!!.trim().split(" ").toSet().toList()
            val idDelete = prefs.getString(KEY_DELETE, "")!!.trim().split(" ")
            prefs.edit().clear().commit()
            for (i in 0 until idInsert.size){
                var id = idInsert[i]
                var item = listDB.firstOrNull { it.id == id }
                if (item != null){
                    listRes.add(item)
                }
            }
            for (i in 0 until idUpdate.size){
                var id = idUpdate[i]
                var item = listDB.firstOrNull { it.id == id }
                var oldItem = listRes.firstOrNull { it.id == id }
                if (oldItem != null){
                    listRes.remove(oldItem)
                }
                if (item != null){
                    listRes.add(item)
                }
            }
            for (i in 0 until idDelete.size){
                var id = idDelete[i]
                var item = listRes.firstOrNull { it.id == id }
                if (item != null){
                    listRes.remove(item)
                }
            }
            TodoApiImpl.updateList(listRes)
            wordDao.deleteAll()
            wordDao.insertAll(listRes)
        }
    }
}
