package com.example.todoapp

data class TodoItem(
    var id: String,
    var text: String,
    var importance: Importance,
    var deadline: Long? = null,
    var isCompleted: Boolean,
    var createdAt: Long? = null,
    var modifiedAt: Long? = null
)

enum class Importance{
    LOW,
    COMMON,
    HIGH
}