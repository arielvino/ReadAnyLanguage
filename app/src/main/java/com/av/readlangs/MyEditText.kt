package com.av.readlangs

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText

class MyEditText(context: Context, attributeSet: AttributeSet) : EditText(context, attributeSet) {

    private val selectionChangeListeners = mutableListOf<OnSelectionChangedListener>()

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (selectionChangeListeners != null) {
            for (listener in selectionChangeListeners) {
                listener.onSelectionChanged(selStart, selEnd)
            }
        }
    }

    fun addOnSelectionChangedListener(listener: OnSelectionChangedListener) {
        if (!selectionChangeListeners.contains(listener)) {
            selectionChangeListeners.add(listener)
        }
    }

    fun removeOnSelectionChangedListener(listener: OnSelectionChangedListener) {
        if (selectionChangeListeners.contains(listener)) {
            selectionChangeListeners.remove(listener)
        }
    }

    interface OnSelectionChangedListener {
        fun onSelectionChanged(selStart: Int, selEnd: Int)
    }
}