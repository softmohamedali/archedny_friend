package com.example.archedny_app_friend.core.domain.utils.validation

import androidx.recyclerview.widget.DiffUtil

open class MyDiff<T>(var old:List<T>, var new:List<T>):DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition]==new[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition]===new[newItemPosition]
    }
}