package com.example.todoapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todoapp.R

/**
 * Single activity in which the fragments are located
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}