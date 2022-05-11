package com.example.archedny_app_friend.utils.validation




import com.example.archedny_app_friend.utils.out
import com.example.util.ValidationUtils
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ValidationUtilsViewTest{


    @Test
    fun `empty email false`(){
        val result= ValidationUtils.isValidEmail(
            ""
        )
        assertThat(result.valid).isFalse()
    }

    @Test
    fun `not pattern email false`(){
        val result= ValidationUtils.isValidEmail(
            "fafafd.fdfsd"
        )
        assertThat(result.valid).isFalse()
    }

    @Test
    fun ` valid email true`(){
        val result= ValidationUtils.isValidEmail(
            "fs@gmail.com"
        )
        assertThat(result.valid).isTrue()
    }

    @Test
    fun `empty password false`(){
        val result= ValidationUtils.isValidPassword(
            ""
        )
        assertThat(result.valid).isFalse()
    }

    @Test
    fun `password lenght less than 6 false`(){
        val result= ValidationUtils.isValidPassword(
            "43gdf"
        )
        assertThat(result.valid).isFalse()
    }

    @Test
    fun `valid password true`(){
        val result= ValidationUtils.isValidPassword(
            "43gdf5"
        )
        assertThat(result.valid).isTrue()
    }

}