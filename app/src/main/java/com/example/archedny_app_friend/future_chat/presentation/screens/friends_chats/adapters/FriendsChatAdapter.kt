package com.example.archedny_app_friend.future_chat.presentation.screens.friends_chats.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.utils.validation.MyDiff
import com.example.archedny_app_friend.databinding.LayoutFriendChatItemBinding

class FriendsChatAdapter () :RecyclerView.Adapter<FriendsChatAdapter.VH>(){

    private var friends= mutableListOf<User>()

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
        if (friend.imagePath.isNotEmpty()){
            item.imgFriend.load(friend.imagePath)
        }
        item.tvFriendName.text = friend.phone
        item.tvLastActive.text = if(!friend.isActive) "last Active" else friend.lastActive
        item.tvLastMassage.text ="last massage" //friend.lastMassage
        item.pointIsActive.visibility = if (friend.isActive) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(friend.id!!)
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


    fun setData(newFriends: MutableList<User>) {
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