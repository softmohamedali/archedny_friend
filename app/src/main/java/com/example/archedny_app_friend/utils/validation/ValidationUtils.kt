package com.example.util

import com.example.archedny_app_friend.utils.validation.ValidateState
import java.util.regex.Pattern

object ValidationUtils {

    /**
     * should be valid email if..
     * if pattern is like email
     * if not empty or null
     */
    fun isValidEmail(email: String?): ValidateState
    {
        if (email.isNullOrEmpty())
        {
            return ValidateState(valid = false,messge = "email is require")
        }
        if (!isEmail(email.trim()))
        {
            return ValidateState(valid = false,messge = "invalid email")
        }
        return ValidateState(valid = true)
    }

    /**
     * should be email if..
     * if start with string then @ thin string then . then string
     */
    private fun isEmail(email: CharSequence): Boolean {
        var isValid = true
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            isValid = false
        }
        return isValid
    }

    /**
     * should be valid password if..
     * if not empty or null
     * if password length great than 6 or equal
     */
    fun isValidPassword(password:String?):ValidateState
    {
        if (password.isNullOrEmpty())
        {
            return ValidateState(valid = false,messge = "password is require")
        }
        if (password.length<6)
        {
            return ValidateState(valid = false,messge = "password length most be more than 6")
        }
        return ValidateState(valid = true)
    }

    /**
     * should be valid egyption number if..
     * if not empty or null
     * if mobile.length equal 11 and start with 010 or 011 or 012
     */
    fun isValidMobile(mobile:String?):ValidateState{
        if (mobile.isNullOrEmpty())
        {
            return ValidateState(valid = false,messge = "mobile is require")
        }
        when (mobile.length) {
            11 -> {
                val emailSplit = mobile.split("")

                val mobileFirstThreeNumber: String = emailSplit[1] + emailSplit[2] + emailSplit[3]
                if (mobileFirstThreeNumber == "010" || mobileFirstThreeNumber == "011" || mobileFirstThreeNumber == "012" || mobileFirstThreeNumber == "015")
                {
                    return ValidateState(valid = true)
                }else{
                    return ValidateState(valid = false,messge = "invalid number")
                }
            }
            else -> {
                return ValidateState(valid = false,messge = "invalid number")
            }
        }


    }
}