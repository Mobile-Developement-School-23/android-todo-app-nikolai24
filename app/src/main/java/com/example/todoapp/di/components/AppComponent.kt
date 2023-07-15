package com.example.todoapp.di.components

import android.content.Context
import com.example.todoapp.di.modules.DataBaseModule
import com.example.todoapp.di.modules.NetworkModule
import com.example.todoapp.di.modules.RepositoryModule
import com.example.todoapp.presentation.fragments.SettingsFragmentDirections
import dagger.BindsInstance
import dagger.Component
import javax.inject.Scope

@Scope
annotation class AppScope

@Component(modules = [DataBaseModule::class,
    NetworkModule::class,
    RepositoryModule::class])
@AppScope
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }

    fun mainFragmentComponent(): MainFragmentComponent

    fun editItemFragmentComponent(): EditItemFragmentComponent

    fun settingsFragmentComponent(): SettingsFragmentComponent

}