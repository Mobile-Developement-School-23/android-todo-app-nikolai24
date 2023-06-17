package com.example.todoapp

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
            if (!i.flag){
                newList.add(i)
            }
        }
        return newList
    }

    fun getItem(position: Int): TodoItem {
        val newItem = TodoItem("", "", Importance.COMMON, "", false, "", "")
        if (position == -1) {
            return newItem
        } else {
            return listTodoItems[position]
        }
    }

    fun deleteItem(position: Int){
        if (listTodoItems[position].flag){
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
        listTodoItems[position].flag = !flag
    }

    fun setFlagById(flag: Boolean, id: String ){
        for (i in 0 .. listTodoItems.size-1){
            if (listTodoItems[i].id == id) {
                listTodoItems[i].flag = !flag
            }
        }
    }
}

private var list = mutableListOf<TodoItem>(
    TodoItem("001", "Купить что-то", Importance.HIGH, "10 6 2023", false, "", ""),
    TodoItem("002", "Посмотреть лекцию", Importance.HIGH, "16 5 2023", true, "", ""),
    TodoItem("003", "Купить что-то", Importance.LOW, "14 8 2023", false, "", ""),
    TodoItem("004", "Приготовить завтрак", Importance.COMMON, "20 7 2023", true, "", ""),
    TodoItem("005",
        "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обрезается текст",
        Importance.HIGH, "", false, "", ""),
    TodoItem("006", "Купить что-то", Importance.LOW, "", true, "", ""),
    TodoItem("007", "Прочитать книгу", Importance.COMMON, "15 7 2023", false, "", ""),
    TodoItem("008", "Сделать приложение", Importance.HIGH, "", true, "", ""),
    TodoItem("009", "Купить что-то", Importance.COMMON, "3 7 2023", false, "", ""),
    TodoItem("010", "Купить что-то", Importance.LOW, "10 6 2023", false, "", "")
)