package com.example.todoapp.recyclerview

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.utils.DateConverter
import com.example.todoapp.Importance
import com.example.todoapp.R
import com.example.todoapp.TodoItem

class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val description: TextView = itemView.findViewById(R.id.description)
    private val checkbox: ImageView = itemView.findViewById(R.id.checkbox)
    private val importanceIcon: ImageView = itemView.findViewById(R.id.importanceIcon)
    private val infoIcon: ImageView = itemView.findViewById(R.id.infoIcon)
    private val deadline : TextView = itemView.findViewById(R.id.deadline)

    fun bind(item: TodoItem, listener: DataAdapter.OnItemClickListener, position: Int, checkBoxListener: DataAdapter.OnCheckBoxClickListener){
        description.text = item.text
        if (item.deadline == null){
            deadline.isVisible = false
        } else {
            deadline.isVisible = true
            deadline.text = DateConverter.dateConvert(item.deadline!!)
        }
        if (item.isCompleted) {
            checkbox.setImageResource(R.drawable.baseline_check_box_24)
            description.setTextColor(Color.GRAY)
            description.paintFlags = description.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            importanceIcon.isVisible = false
            deadline.isVisible = false
        } else {
            if (item.importance == Importance.HIGH) {
                checkbox.setImageResource(R.drawable.baseline_check_box_outline_red_24)
            } else {
                checkbox.setImageResource(R.drawable.baseline_check_box_outline_blank_24)
            }
            description.setTextColor(Color.BLACK)
            description.paintFlags = description.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            importanceIcon.isVisible = true
            if (item.deadline != null){
                deadline.isVisible = true
            }
        }
        infoIcon.setImageResource(R.drawable.baseline_info_24)
        itemView.setOnClickListener{listener.onItemClick(item, position)}
        checkbox.setOnClickListener{
            checkBoxListener.onItemClick(item, position)
        }
        if (item.importance == Importance.HIGH) {
            importanceIcon.setImageResource(R.drawable.baseline_priority_high_24)
        }
        if (item.importance == Importance.LOW) {
            importanceIcon.setImageResource(R.drawable.baseline_south_24)
        }
        if (item.importance == Importance.COMMON) {
            importanceIcon.setImageResource(R.drawable.baseline_north_24)
        }
    }
}