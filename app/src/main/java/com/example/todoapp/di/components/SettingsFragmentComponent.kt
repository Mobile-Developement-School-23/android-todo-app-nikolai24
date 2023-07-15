package com.example.todoapp.di.components

import com.example.todoapp.di.modules.ViewModelFactoryModule
import com.example.todoapp.presentation.fragments.SettingsFragment
import com.example.todoapp.presentation.viewmodel.ViewModelFactoryScope
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class SettingsFragmentScope

@Subcomponent(modules = [ViewModelFactoryModule::class])
@SettingsFragmentScope
@ViewModelFactoryScope
interface SettingsFragmentComponent {

    fun inject(settingsFragment: SettingsFragment)

}