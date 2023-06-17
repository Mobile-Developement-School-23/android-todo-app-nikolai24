package com.example.todoapp

data class TodoItem(
    var id: String,
    var description: String,
    var importance: Importance,
    var deadline: String,
    var flag: Boolean,
    var creationDate: String,
    var changeDate: String
)

enum class Importance{
    LOW,
    COMMON,
    HIGH
}