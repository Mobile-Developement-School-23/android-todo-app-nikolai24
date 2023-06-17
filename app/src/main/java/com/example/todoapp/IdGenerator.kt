package com.example.todoapp

object IdGenerator {

    fun getNewId(): String {
        return (1000..9000).random().toString() + (100..900).random().toString()
    }

}