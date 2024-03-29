package com.example.archedny_app_friend.core.domain.utils.validation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface ProvideDispatchers {
    val main:CoroutineDispatcher
    val io:CoroutineDispatcher
    val default:CoroutineDispatcher
}

class DefaultDispatchers : ProvideDispatchers {
    override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    override val default: CoroutineDispatcher
        get() = Dispatchers.Default

}