package com.example.todoapp.app

import android.app.Application
import com.example.todoapp.di.components.AppComponent
import com.example.todoapp.di.components.DaggerAppComponent

/**
 * Creating an AppComponent
 */

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .factory()
            .create(context = this)
    }

}