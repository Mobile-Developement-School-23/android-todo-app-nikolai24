package com.example.todoapp.repository

import com.example.todoapp.Importance
import com.example.todoapp.TodoItem
import com.example.todoapp.utils.IdGenerator

object TodoItemsRepository {
    private var listTodoItems: MutableList<TodoItem> = list
    private var counter = 4

    private fun addItem(item: TodoItem){
        listTodoItems.add(item)
    }

    fun saveItem(item: TodoItem, position: Int){
        if (item.id == ""){
            item.id = IdGenerator.getNewId()
            addItem(item)
        } else {
            listTodoItems[position] = item
        }
    }

    fun getListItems(): MutableList<TodoItem>{
        return listTodoItems
    }

    fun getListUnfulfilledItems(): MutableList<TodoItem>{
        var newList: MutableList<TodoItem> = mutableListOf()
        for (i in listTodoItems) {
            if (!i.isCompleted){
                newList.add(i)
            }
        }
        return newList
    }

    fun getItem(position: Int): TodoItem {
        val newItem = TodoItem("", "", Importance.COMMON, null, false, null, null)
        if (position == -1) {
            return newItem
        } else {
            return listTodoItems[position]
        }
    }

    fun deleteItem(position: Int){
        if (listTodoItems[position].isCompleted){
            reduceNumber()
        }
        listTodoItems.removeAt(position)
    }

    fun deleteUnfulfilledItem(position: Int){
        val list = getListUnfulfilledItems()
        val id = list[position].id
        for (i in 0 .. listTodoItems.size - 1){
           if (listTodoItems[i].id == id){
               listTodoItems.removeAt(i)
               break
            }
        }
    }

    fun getCounter(): Int{
        return counter
    }

    fun addNumber(){
        counter += 1
    }

    fun reduceNumber(){
        counter -= 1
    }

    fun setFlag(flag: Boolean, position: Int ){
        listTodoItems[position].isCompleted = !flag
    }

    fun setFlagById(flag: Boolean, id: String ){
        for (i in 0 .. listTodoItems.size-1){
            if (listTodoItems[i].id == id) {
                listTodoItems[i].isCompleted = !flag
            }
        }
    }
}

private var list = mutableListOf<TodoItem>(
    TodoItem("001", "Купить что-то", Importance.HIGH, 1687963963, false, 1687963963, 1687963963),
    TodoItem("002", "Посмотреть лекцию", Importance.HIGH, 1687963963, true, 1687963963, 1687963963),
    TodoItem("003", "Купить что-то", Importance.LOW, 1687963963, false, 1687963963, 1687963963),
    TodoItem("004", "Приготовить завтрак", Importance.COMMON, 1687963963, true, 1687963963, 1687963963),
    TodoItem("005",
        "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезается текст",
        Importance.HIGH, 1687963963, false, 1687963963, 1687963963),
    TodoItem("006", "Купить что-то", Importance.LOW, 1687963963, true, 1687963963, 1687963963),
    TodoItem("007", "Прочитать книгу", Importance.COMMON, 1687963963, false, 1687963963, 1687963963),
    TodoItem("008", "Сделать приложение", Importance.HIGH, 1687963963, true, 1687963963, 1687963963),
    TodoItem("009", "Купить что-то", Importance.COMMON, 1687963963, false, 1687963963, 1687963963),
    TodoItem("010", "Купить что-то", Importance.LOW, 1687963963, false, 1687963963, 1687963963)
)