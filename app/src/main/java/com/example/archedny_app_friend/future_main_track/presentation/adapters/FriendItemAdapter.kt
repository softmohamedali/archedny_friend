package com.example.archedny_app_friend.future_main_track.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.databinding.LayoutFrindItemBinding
import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.utils.validation.MyDiff
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class FriendItemAdapter @Inject constructor(
    @ApplicationContext val context:Context
        ) : RecyclerView.Adapter<FriendItemAdapter.Vh>() {
    private var phoneList = mutableListOf<User>()
    private var holders= mutableListOf<Vh>()
    private var holdersPos= mutableListOf<Int>()
    var cheackedFriend: User?=null
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh.from(parent)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holders.add(holder)
        holdersPos.add(position)
        val user = phoneList[position]
        holder.view.tvPhone.text=user.phone
        holder.itemView.setOnClickListener {
            for (i in holdersPos)
            {
                if (i!=position)
                {
                    holders[i].view.itemContainer.strokeColor =
                        ContextCompat.getColor(context, R.color.item_phone_border)
                    holders[i].view.tvPhone.setTextColor(
                        ContextCompat.getColor(context, R.color.item_phone_text)
                    )
                }else{
                    holders[i].view.itemContainer.strokeColor =
                        ContextCompat.getColor(context, R.color.positive)
                    holders[i].view.tvPhone.setTextColor(
                        ContextCompat.getColor(context, R.color.positive)
                    )
                }
            }
            cheackedFriend=user
            onItemClickListener?.let {
                it(user)
            }
        }

    }


    override fun getItemCount(): Int {
        return phoneList.size
    }


    fun setData(newFAvs: MutableList<User>) {
        holders= mutableListOf()
        holdersPos= mutableListOf()
        val mydiff = MyDiff(phoneList, newFAvs)
        val result = DiffUtil.calculateDiff(mydiff)
        phoneList = newFAvs
        result.dispatchUpdatesTo(this)
    }

    private var onItemClickListener: ((User) -> Unit)? = null

    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClickListener = listener
    }



}