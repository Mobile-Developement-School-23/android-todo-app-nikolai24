package com.example.todoapp.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.todoapp.data.retrofit.TodoItemData

/**
 * Model class TodoItem to store in the database.
 */

@Entity(tableName = "todo_table")
data class TodoItem(
    @PrimaryKey @ColumnInfo var id: String,
    @ColumnInfo var text: String,
    @TypeConverters(ImportanceConverter::class) @ColumnInfo var importance: Importance,
    @ColumnInfo var deadline: Long? = null,
    @ColumnInfo var isCompleted: Boolean,
    @ColumnInfo var createdAt: Long? = null,
    @ColumnInfo var modifiedAt: Long? = null
){
    fun toTodoItemData(): TodoItemData {
        var i = when (importance) {
            Importance.LOW -> "low"
            Importance.COMMON -> "basic"
            else -> "important"
        }
        return TodoItemData(
            id = id,
            text = text,
            importance = i,
            deadline = deadline,
            done = isCompleted,
            color = "#FFFFFF",
            created_at = createdAt,
            changed_at = modifiedAt,
            last_updated_by = "user"
        )
    }
}

class ImportanceConverter {
    @TypeConverter
    fun fromImportance(importance: Importance): String {
        return when (importance) {
            Importance.LOW -> "low"
            Importance.COMMON -> "common"
            Importance.HIGH -> "high"
        }
    }

    @TypeConverter
    fun toImportance(str: String): Importance {
        return when (str) {
            "low" -> Importance.LOW
            "high" -> Importance.HIGH
            else -> Importance.COMMON
        }
    }

}

enum class Importance {
    LOW,
    COMMON,
    HIGH
}