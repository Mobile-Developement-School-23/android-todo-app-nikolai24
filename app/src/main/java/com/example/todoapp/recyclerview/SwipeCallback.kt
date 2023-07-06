package com.example.todoapp.recyclerview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.VectorDrawable
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R

class SwipeCallback(private var listener: SwipeToDelete) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    interface SwipeToDelete {
        fun onItemClick(position: Int)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        when (direction) {
            ItemTouchHelper.LEFT -> {
                listener.onItemClick(position)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView: View = viewHolder.itemView
        var p = Paint().also {
            it.color = ResourcesCompat.getColor(recyclerView.resources, R.color.vivid_red, null)
        }
        val icon = (ResourcesCompat.getDrawable(
            recyclerView.resources,
            R.drawable.baseline_delete_24,
            null
        ) as VectorDrawable).toBitmap()
        c.drawRect(
            itemView.right.toFloat() + dX,
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat(),
            p
        )
        val iconMarginRight = (dX * -0.1).coerceAtMost(70.0).coerceAtLeast(0.0)
        c.drawBitmap(
            icon,
            itemView.right.toFloat() - iconMarginRight.toFloat() - icon.width,
            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
            p
        )
        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }
}