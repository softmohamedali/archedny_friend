package com.example.archedny_app_friend.utils.validation

import android.util.Patterns
import android.widget.EditText

object ValidationUtilsView {
    fun isNotValidEmail(et: EditText):Boolean
    {
        val value=et.text.toString()
        if (value.isNullOrEmpty())
        {
            et.error="required Email"
            et.requestFocus()
            return true
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches())
        {
            et.error="Invalid Email"
            et.requestFocus()
            return true
        }
        return false
    }

    fun isFeildIsEmpty(et: EditText):Boolean
    {
        val value=et.text.toString()
        if (value.isEmpty())
        {
            et.error="required"
            et.requestFocus()
            return true
        }
        return false
    }

    fun isValidPassword(et: EditText):Boolean
    {
        val value=et.text.toString()
        if (value.isEmpty())
        {
            et.error="required"
            et.requestFocus()
            return false
        }
        if (value.length<6)
        {
            et.error="password so weak"
            et.requestFocus()
            return false
        }
        return true
    }

    fun validateMobile(et: EditText): Boolean {
        when (et.text.toString().length) {
            11 -> {
                val emailSplit = et.text.toString().split("")

                val mobileFirstThreeNumber: String = emailSplit[1] + emailSplit[2] + emailSplit[3]
                if (mobileFirstThreeNumber == "010" || mobileFirstThreeNumber == "011" || mobileFirstThreeNumber == "012" || mobileFirstThreeNumber == "015")
                {
                    return true
                }else{
                    et.error="inavlid number"
                    et.requestFocus()
                    return false
                }
            }
            else -> {
                et.error="inavlid number"
                et.requestFocus()
                return false
            }
        }


    }
}