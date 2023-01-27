package com.example.archedny_app_friend.future_main_track.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.archedny_app_friend.databinding.LayoutPhoneItemBinding
import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.utils.validation.MyDiff

class PhoneItemAdapter() : RecyclerView.Adapter<PhoneItemAdapter.Vh>() {
    private var phoneList = mutableListOf<User>()

    class Vh(var view: LayoutPhoneItemBinding) : RecyclerView.ViewHolder(view.root) {
        companion object {
            fun from(parent: ViewGroup): Vh {
                val bindingview = LayoutPhoneItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return Vh(bindingview)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh.from(parent)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        val user = phoneList[position]
        holder.view.tvPhone.text=user.phone
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it()
            }
        }

        holder.view.imgAddFriend.setOnClickListener {
            onAddFriendClickListener?.let {
                it(user.id!!)
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


    private var onAddFriendClickListener: ((userId:String) -> Unit)? = null

    fun setOnAddFriendClickListener(listener: (userId:String) -> Unit) {
        onAddFriendClickListener = listener
    }

}