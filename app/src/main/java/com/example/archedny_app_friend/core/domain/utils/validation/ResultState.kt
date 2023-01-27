package com.example.archedny_app_friend.core.domain.utils.validation

sealed class ResultState<out T>(
    val data: T? = null, var message: String? = null
){
    object Init: ResultState<Nothing>()
    object IsLoading: ResultState<Nothing>()
    class IsSucsses<T>(data: T?=null): ResultState<T>(data)
    class IsError<T>( msg: String): ResultState<T>(null,msg)
}
