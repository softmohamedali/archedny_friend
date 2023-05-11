package com.example.archedny_app_friend.future_chat.presentation.screens.chat.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.archedny_app_friend.core.domain.utils.validation.MyDiff
import com.example.archedny_app_friend.databinding.LayoutFriendChatItemBinding
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage

class MessagesAdapters (): RecyclerView.Adapter<MessagesAdapters.VH>(){

    private var messages= mutableListOf<TextMassage>()

    class VH(var view: LayoutFriendChatItemBinding) : RecyclerView.ViewHolder(view.root){
        companion object {
            fun from(parent: ViewGroup): VH {
                val bindingView = LayoutFriendChatItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return VH(bindingView)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):VH {
        return VH.from(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {



    }


    override fun getItemCount(): Int {
        return messages.size
    }


    fun setData(newFriends: MutableList<TextMassage>) {
        val myDiff = MyDiff(messages, newFriends)
        val result = DiffUtil.calculateDiff(myDiff)
        messages = newFriends
        result.dispatchUpdatesTo(this)
    }



}