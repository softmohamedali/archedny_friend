package com.example.archedny_app_friend.core.domain.utils.myextension

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.databinding.LayoutToastSucessBinding

fun toast(
    context: Context,
    msg:String?="toast here"
){
    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
}

fun coustomToast(
    context: Context,
    msg: String,
    type: ToastType = ToastType.SUCCESS
) {
    var toast = Toast(context)
    toast.duration = Toast.LENGTH_SHORT
    val view = LayoutToastSucessBinding.inflate(LayoutInflater.from(context))
    view.tvSuccess.text = msg
    val resourceId:Int=when(type){
        ToastType.SUCCESS ->{
            R.drawable.ic_done
        }
        ToastType.ERROR ->{
            R.drawable.ic_error
        }
        ToastType.WARNING ->{
            R.drawable.ic_warning
        }
    }
    val iconColor:Int=when(type){
        ToastType.SUCCESS ->{
            R.color.purple_500
        }
        ToastType.ERROR ->{
            R.color.error
        }
        ToastType.WARNING ->{
            R.color.warning
        }
    }

    view.imageView4.setBackgroundResource(resourceId)
    view.imageView4.setColorFilter(ContextCompat.getColor(context, iconColor))

    toast.view = view.root
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}
