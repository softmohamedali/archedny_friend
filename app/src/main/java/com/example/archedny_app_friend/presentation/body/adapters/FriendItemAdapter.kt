package com.example.archedny_app_friend.presentation.body.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.archedny_app_friend.databinding.LayoutFrindItemBinding
import com.example.archedny_app_friend.databinding.LayoutPhoneItemBinding
import com.example.archedny_app_friend.domain.models.User
import com.example.orignal_ecommerce_manger.util.MyDiff

class FriendItemAdapter () : RecyclerView.Adapter<FriendItemAdapter.Vh>() {
    private var phoneList = mutableListOf<User>()

    class Vh(var view: LayoutFrindItemBinding) : RecyclerView.ViewHolder(view.root) {
        companion object {
            fun from(parent: ViewGroup): Vh {
                val bindingview = LayoutFrindItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return Vh(bindingview)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendItemAdapter.Vh {
        return Vh.from(parent)
    }

    override fun onBindViewHolder(holder: FriendItemAdapter.Vh, position: Int) {
        val user = phoneList[position]
        holder.view.tvPhone.text=user.phone
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it()
            }
        }
    }


    override fun getItemCount(): Int {
        return phoneList.size
    }


    fun setData(newFAvs: MutableList<User>) {
        val mydiff = MyDiff(phoneList, newFAvs)
        val result = DiffUtil.calculateDiff(mydiff)
        phoneList = newFAvs
        result.dispatchUpdatesTo(this)
    }

    private var onItemClickListener: (() -> Unit)? = null

    fun setOnItemClickListener(listener: () -> Unit) {
        onItemClickListener = listener
    }



}