package com.example.todoapp.data.retrofit

import com.example.todoapp.data.database.Importance
import com.example.todoapp.data.database.TodoItem
import com.google.gson.annotations.SerializedName

/**
 * Model class TodoItemData for working with the server.
 */
data class TodoItemData(
    @SerializedName("id") var id: String,
    @SerializedName("text") var text: String,
    @SerializedName("importance") var importance: String,
    @SerializedName("deadline") var deadline: Long? = null,
    @SerializedName("done") var done: Boolean,
    @SerializedName("color") val color: String? = null,
    @SerializedName("created_at") var created_at: Long?,
    @SerializedName("changed_at") var changed_at: Long?,
    @SerializedName("last_updated_by") var last_updated_by: String
) {
    fun toTodoItem(): TodoItem {
        var i = when (importance) {
            "low" -> Importance.LOW
            "basic" -> Importance.COMMON
            else -> Importance.HIGH

        }
        return TodoItem(
            id = id,
            text = text,
            importance = i,
            deadline = deadline,
            isCompleted = done,
            createdAt = created_at,
            modifiedAt = changed_at
        )
    }
}

data class ListResponse(
    @SerializedName("status") var status: String,
    @SerializedName("revision") var revision: Int,
    @SerializedName("list") var list: List<TodoItemData>
)

data class ItemResponse(
    @SerializedName("status") var status: String,
    @SerializedName("revision") var revision: Int,
    @SerializedName("element") var element: TodoItemData
)

data class ListRequest(
    @SerializedName("list") var list: List<TodoItemData>
)

data class ItemRequest(
    @SerializedName("element") var element: TodoItemData
)