package com.example.todoapp.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.app.App
import com.example.todoapp.databinding.FragmentSettingsBinding
import com.example.todoapp.di.components.SettingsFragmentComponent
import com.example.todoapp.presentation.viewmodel.MainViewModel
import com.example.todoapp.presentation.viewmodel.MainViewModelFactory
import com.example.todoapp.utils.AppConstants
import javax.inject.Inject

class SettingsFragment : Fragment(), MenuProvider {

    @Inject
    lateinit var vmFactory: MainViewModelFactory
    private lateinit var mainViewModel: MainViewModel
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var controller: NavController
    private lateinit var component: SettingsFragmentComponent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner)
        settingActionBar()
        controller = findNavController()
        component = (activity?.application as App).appComponent.settingsFragmentComponent()
        component.inject(this)
        mainViewModel = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioLightTheme -> mainViewModel.setTheme(AppConstants.LIGHT_THEME)
                R.id.radioDarkTheme -> mainViewModel.setTheme(AppConstants.DARK_THEME)
                R.id.radioSystemTheme -> mainViewModel.setTheme(AppConstants.SYSTEM_THEME)
            }
        }
        var theme = mainViewModel.getTheme()
        when (theme){
            AppConstants.LIGHT_THEME -> binding.radioGroup.check(R.id.radioLightTheme)
            AppConstants.DARK_THEME -> binding.radioGroup.check(R.id.radioDarkTheme)
            AppConstants.SYSTEM_THEME -> binding.radioGroup.check(R.id.radioSystemTheme )
        }
    }

    private fun settingActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title =
            context?.resources?.getString(R.string.settings_title)
        (activity as AppCompatActivity).supportActionBar?.subtitle = ""
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_close_24)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == android.R.id.home) {
            startMainActivity()
        }
        return false
    }

    private fun startMainActivity() {
        val action = SettingsFragmentDirections.actionSettingsFragmentToMainFragment()
        controller.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}