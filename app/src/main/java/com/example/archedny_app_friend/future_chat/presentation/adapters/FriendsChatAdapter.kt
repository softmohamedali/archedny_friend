package com.example.archedny_app_friend.future_chat.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.archedny_app_friend.core.domain.utils.validation.MyDiff
import com.example.archedny_app_friend.databinding.LayoutFriendChatItemBinding
import com.example.archedny_app_friend.future_chat.domain.models.Friend

class FriendsChatAdapter () :RecyclerView.Adapter<FriendsChatAdapter.VH>(){

    private var friends= mutableListOf<Friend>()

    class VH(var view:LayoutFriendChatItemBinding) :ViewHolder(view.root){
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



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH.from(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val friend = friends[position]
        val item = holder.view
        item.imgFriend.load(friend.imagePath)
        item.tvFriendName.text = friend.name
        item.tvLastActive.text = friend.lastActive
        item.tvLastMassage.text = friend.lastMassage
        item.pointIsActive.visibility = if (friend.isActive) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(friend.chatChannelId)
            }
        }
        item.imgFriendContainer.setOnClickListener {
            onFriendImageClickListener?.let {
                it(friend.imagePath)
            }
        }

    }


    override fun getItemCount(): Int {
        return friends.size
    }


    fun setData(newFriends: MutableList<Friend>) {
        val mydiff = MyDiff(friends, newFriends)
        val result = DiffUtil.calculateDiff(mydiff)
        friends = newFriends
        result.dispatchUpdatesTo(this)
    }

    private var onItemClickListener: ((chatChannelId:String) -> Unit)? = null

    fun setOnItemClickListener(listener: (chatChannelId:String) -> Unit) {
        onItemClickListener = listener
    }


    private var onFriendImageClickListener: ((imagePath:String) -> Unit)? = null

    fun setOnFriendImageClickListener(listener: (imagePath:String) -> Unit) {
        onFriendImageClickListener = listener
    }

}