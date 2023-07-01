package com.example.todoapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todo_table")
    fun getItems(): Flow<List<TodoItem>>

    @Query("SELECT * FROM todo_table")
    fun getList(): List<TodoItem>

    @Query("SELECT * FROM todo_table WHERE id LIKE :id LIMIT 1")
    fun getItemByID(id: String): TodoItem

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: TodoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<TodoItem>)

    @Update
    suspend fun update(item: TodoItem)

    @Delete
    suspend fun delete(item: TodoItem)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()
}