package com.example.todoapp.presentation.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.MenuProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.todoapp.*
import com.example.todoapp.app.App
import com.example.todoapp.data.database.Importance
import com.example.todoapp.data.database.TodoItem
import com.example.todoapp.databinding.FragmentEditItemBinding
import com.example.todoapp.di.components.EditItemFragmentComponent
import com.example.todoapp.utils.DateConverter
import com.example.todoapp.presentation.viewmodel.MainViewModel
import com.example.todoapp.presentation.viewmodel.MainViewModelFactory
import com.example.todoapp.utils.RestoreTodoItem
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class EditItemFragment : Fragment(), MenuProvider {

    @Inject
    lateinit var vmFactory: MainViewModelFactory
    private lateinit var mainViewModel: MainViewModel
    private lateinit var controller: NavController
    private val args: EditItemFragmentArgs by navArgs()
    private lateinit var item: TodoItem
    private var id = ""
    private var description = ""
    private var importance = Importance.COMMON
    private var deadline: Long? = null
    private var position = 0
    private lateinit var component: EditItemFragmentComponent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MyScreen()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner)
        settingActionBar()
        controller = findNavController()
        component = (activity?.application as App).appComponent.editItemFragmentComponent()
        component.inject(this)
        mainViewModel = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)
        id = args.id
        position = args.position
        item = mainViewModel.getItem(id)
        description = item.text
        deadline = item.deadline
        importance = item.importance
    }

    @Composable
    fun MyScreen() {
        Column(
            Modifier
                .background(colorResource(id = R.color.floral_white))
                .verticalScroll(rememberScrollState())
        ){
            CardTextField()
            ImportanceTitle()
            ImportanceCard()
            Divider()
            DeadLineSelection()
            Divider()
            DeleteButton()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CardTextField(){
        val editText = remember{ mutableStateOf(description) }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp, top = 15.dp)
                .height(IntrinsicSize.Min)
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            )
        ) {
            TextField(
                value = editText.value,
                onValueChange = { newText ->
                    editText.value = newText
                    description = newText
                },
                textStyle = TextStyle.Default.copy(fontSize = 20.sp, background = Color.White),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    disabledContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
        }
    }

    @Composable
    fun ImportanceTitle() {
        Text(
            text = stringResource(id = R.string.importance),
            color = colorResource(id = R.color.black),
            fontSize = 18.sp,
            modifier = Modifier
                .padding(top = 20.dp, start = 15.dp)
                .fillMaxWidth()
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ImportanceCard(){
        val expanded = remember { mutableStateOf(false) }
        val importanceText = remember { mutableStateOf(resources.getString(R.string.common)) }
        val colorId = remember { mutableIntStateOf(resources.getColor(R.color.black)) }
        var c = "black"
        when (importance){
            Importance.LOW -> {
                importanceText.value = resources.getString(R.string.low)
                colorId.value = resources.getColor(R.color.black)
            }
            Importance.HIGH -> {
                importanceText.value = resources.getString(R.string.high)
                colorId.value = resources.getColor(R.color.vivid_red)
            }
            else -> {
                importanceText.value = resources.getString(R.string.common)
                colorId.value = resources.getColor(R.color.black)
            }
        }
        Card(
            modifier = Modifier
                .width(160.dp)
                .padding(start = 15.dp, end = 15.dp, top = 20.dp)
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            onClick = { expanded.value = true }
        ) {
            Text(
                text = importanceText.value,
                fontSize = 17.sp,
                modifier = Modifier
                    .padding(start = 15.dp, end = 15.dp, top = 8.dp, bottom = 8.dp)
            )
        }
        Box{
            DropdownMenu(
                modifier = Modifier.background(Color.White),
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
                offset = DpOffset(x = 15.dp, y = 5.dp)
            ){
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.common_menu),
                        fontSize = 17.sp)},
                    onClick = {
                        importance = Importance.COMMON
                        importanceText.value = resources.getString(R.string.common)
                        expanded.value = false
                    })
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.low_menu),
                        fontSize = 17.sp) },
                    onClick = {
                        importance = Importance.LOW
                        importanceText.value = resources.getString(R.string.low)
                        expanded.value = false
                    })
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.high_menu),
                        color = colorResource(id = R.color.vivid_red),
                        fontSize = 17.sp) },
                    onClick = {
                        importance = Importance.HIGH
                        importanceText.value = resources.getString(R.string.high)
                        expanded.value = false
                    })
            }
        }
    }

    @Composable
    fun Divider(){
        Divider(color = colorResource(id = R.color.grey),
            thickness = 1.dp,
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, top = 24.dp)
        )
    }

    @Composable
    fun DeadLineSelection(){
        val checkedState = remember { mutableStateOf(false) }
        if (deadline != null) {
            checkedState.value = true
        }
        val date = remember { mutableStateOf(DateConverter.dateConvert(deadline))}

        fun datePickerDialog() {
            var intDate = Triple(0, 0, 0)
            if (deadline == null) {
                deadline = DateConverter.getLongDate()
                intDate = DateConverter.getIntDate()
            } else {
                intDate = DateConverter.getIntDate(deadline!!)
            }
            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, myear, mmonth, mday ->
                    deadline = DateConverter.getLongDate(myear, mmonth + 1, mday)
                    date.value = DateConverter.dateConvert(deadline)
                },
                intDate.third,
                intDate.second,
                intDate.first
            ).show()
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 15.dp, end = 20.dp)) {
            Column() {
                Text(
                    text = stringResource(id = R.string.deadline_title),
                    color = colorResource(id = R.color.black),
                    fontSize = 18.sp
                )
                Card(
                    modifier = Modifier
                        .width(160.dp)
                        .padding(top = 20.dp)
                        .wrapContentHeight()
                        .clickable {
                            if (deadline != null) {
                                datePickerDialog()
                            }
                        },
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 10.dp
                    ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                    )
                ) {
                    Text(
                        text = date.value,
                        color = colorResource(id = R.color.blue),
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(start = 15.dp, end = 15.dp, top = 8.dp, bottom = 8.dp)
                    )
                }
            }
            Switch(
                checked = checkedState.value,
                onCheckedChange = {
                    checkedState.value = it
                    if(checkedState.value){
                        deadline = DateConverter.getLongDate()
                        date.value = DateConverter.dateConvert(deadline)
                    } else {
                        deadline = null
                        date.value = ""
                    }
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(id = R.color.blue),
                    checkedTrackColor = colorResource(id = R.color.low_blue),
                    checkedBorderColor = colorResource(id = R.color.low_blue),
                    uncheckedThumbColor = colorResource(id = R.color.white),
                    uncheckedTrackColor = colorResource(id = R.color.grey),
                    uncheckedBorderColor = colorResource(id = R.color.grey)
                )
            )
        }
    }

    @Composable
    fun DeleteButton(){
        Button(
            onClick = {
                if (id == "") {
                    startMainActivity()
                } else {
                    mainViewModel.deleteItem(item)
                    RestoreTodoItem.setDeleteFlag(flag = true)
                    RestoreTodoItem.setName(item.text)
                    startMainActivity()
                }
            },
            modifier = Modifier
                .padding(start = 15.dp, top = 40.dp, bottom = 40.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.white)
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_red_24),
                    contentDescription = stringResource(R.string.delete),
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.delete),
                    color = Color.Red,
                    fontSize = 18.sp
                )
            }
        }
    }

    private fun startMainActivity() {
        controller.navigate(R.id.mainFragment)
    }

    private fun settingActionBar(){
        (activity as AppCompatActivity).supportActionBar?.title = ""
        (activity as AppCompatActivity).supportActionBar?.subtitle = ""
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_close_24)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.edit_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId == R.id.actionSave) {
            item.text = description
            item.importance = importance
            item.modifiedAt = DateConverter.getLongDate()
            item.deadline = deadline
            if (position == -1) {
                item.createdAt = item.modifiedAt
            }
            mainViewModel.saveItem(item)
            startMainActivity()
        } else {
            if (menuItem.itemId == android.R.id.home) {
                startMainActivity()
            }
        }
        return false
    }
}