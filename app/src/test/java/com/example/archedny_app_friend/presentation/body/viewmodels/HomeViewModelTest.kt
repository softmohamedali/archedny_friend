package com.example.archedny_app_friend.presentation.body.viewmodels

import com.example.archedny_app_friend.data.repo.RepoManner
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat

class HomeViewModelTest {

    @Mock
    private lateinit var repoManner: RepoManner

    private lateinit var testDispatchers: TestDispatchers


    private lateinit var  viewModel: HomeViewModel

    @Before
    fun setUp() {
        testDispatchers= TestDispatchers()
        viewModel=HomeViewModel(repoManner)
    }

    @Test
    fun `getUser ,is return user`()= runBlocking {
        viewModel.users.test {
            testDispatchers.testDispatcher.advanceTimeBy(100L)
            val emmition=awaitItem()
            assertThat(emmition).isEqualTo(1)
        }
    }
}