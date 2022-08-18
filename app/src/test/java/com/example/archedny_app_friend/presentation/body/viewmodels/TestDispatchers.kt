package com.example.archedny_app_friend.presentation.body.viewmodels

import com.example.archedny_app_friend.utils.ProvideDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher

class TestDispatchers:ProvideDispatchers {
    val testDispatcher=TestCoroutineDispatcher()
    override val main: CoroutineDispatcher
        get() =testDispatcher
    override val io: CoroutineDispatcher
        get() = testDispatcher
    override val default: CoroutineDispatcher
        get() =testDispatcher
}