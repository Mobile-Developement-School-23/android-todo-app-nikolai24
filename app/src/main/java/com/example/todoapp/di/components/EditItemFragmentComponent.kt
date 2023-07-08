package com.example.todoapp.di.components

import com.example.todoapp.di.modules.ViewModelFactoryModule
import com.example.todoapp.presentation.fragments.EditItemFragment
import com.example.todoapp.presentation.viewmodel.ViewModelFactoryScope
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class EditItemFragmentScope

@Subcomponent(modules = [ViewModelFactoryModule::class])
@EditItemFragmentScope
@ViewModelFactoryScope
interface EditItemFragmentComponent {

    fun inject(editItemFragment: EditItemFragment)

}