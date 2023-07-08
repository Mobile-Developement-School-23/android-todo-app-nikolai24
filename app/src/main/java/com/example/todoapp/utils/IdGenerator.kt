package com.example.todoapp.utils

import java.util.UUID

/**
 * ID generation.
 */

object IdGenerator {

    fun getNewId(): String {
        return UUID.randomUUID().toString()
    }

}