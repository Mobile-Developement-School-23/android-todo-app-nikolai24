package com.example.todoapp.utils

import java.util.UUID

object IdGenerator {

    fun getNewId(): String {
        return UUID.randomUUID().toString()
    }

}