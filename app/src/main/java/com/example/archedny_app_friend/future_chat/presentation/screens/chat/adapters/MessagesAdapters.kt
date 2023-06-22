package com.example.archedny_app_friend.future_chat.presentation.screens.chat.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.core.domain.utils.validation.MyDiff
import com.example.archedny_app_friend.databinding.LayoutFriendChatItemBinding
import com.example.archedny_app_friend.databinding.LayoutSendChattextItemBinding
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage

class MessagesAdapters (): RecyclerView.Adapter<MessagesAdapters.VH>(){

    private var messages= mutableListOf<TextMassage>()
    var currentUserId:String?=""
    class VH(var view: LayoutSendChattextItemBinding) : RecyclerView.ViewHolder(view.root){
        companion object {
            fun from(parent: ViewGroup): VH {
                val bindingView = LayoutSendChattextItemBinding.inflate(
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
        val currentMassage=messages[position]
        holder.view.tvMessageContent.text=currentMassage.msg
        if (currentMassage.senderId!=currentUserId){
            holder.view.tvMessageContent.setBackgroundResource(R.drawable.chat_recive_bubble_arrow)
            holder.view.textMsgContainer.gravity=Gravity.START
        }


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