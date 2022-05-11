package com.example.archedny_app_friend.utils.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.widget.EditText
import com.example.archedny_app_friend.utils.out

class OTPReceiver : BroadcastReceiver() {
    fun setListener(listener: SMSListener?) {
        Companion.listener = listener
    }
    override fun onReceive(context: Context, intent: Intent) {
        val messages: Array<SmsMessage> = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (sms in messages) {
            val msg: String = sms.getMessageBody()
            val otp = msg.split(" is").toTypedArray()[0]
            if (otp.length < 6){
                listener?.onFalieur("some error accured")
                return
            }
            listener?.onSucess(otp)

        }
    }

    companion object {
        private var listener: SMSListener? = null
    }

    interface SMSListener{
        fun onSucess(otp:String)
        fun onFalieur(msg:String)
    }
}
