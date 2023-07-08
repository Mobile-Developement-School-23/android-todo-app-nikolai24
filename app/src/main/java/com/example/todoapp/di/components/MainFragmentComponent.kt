package com.example.todoapp.di.components

import com.example.todoapp.di.modules.ViewModelFactoryModule
import com.example.todoapp.presentation.fragments.MainFragment
import com.example.todoapp.presentation.viewmodel.ViewModelFactoryScope
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class MainFragmentScope

@Subcomponent(modules = [ViewModelFactoryModule::class])
@MainFragmentScope
@ViewModelFactoryScope
interface MainFragmentComponent {

    fun inject(mainFragment: MainFragment)

}

