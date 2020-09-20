package com.angelicao.gifapp.giflist

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDirections
import androidx.navigation.Navigation.setViewNavController
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.angelicao.gifapp.R
import com.angelicao.repository.GifRepository
import com.angelicao.repository.source.GifPagingSource
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class GifListHostActivityTest {
    private lateinit var gifViewModel: GifListViewModel
    private val repository = mockk<GifRepository>(relaxed = true)
    private val navController = mockk<NavController>()
    private val gifPagingSource = mockk<GifPagingSource>(relaxed = true)

    @Before
    fun setup() {
        mockLoadGifList()
        gifViewModel = GifListViewModel(repository, gifPagingSource)
        loadKoinModules(module {
            viewModel(override = true) { gifViewModel }
        })

        val gifListScenario = launchActivity<GifListHostActivity>()
        gifListScenario.onActivity {
            setViewNavController(it.findViewById(R.id.main_content), navController)
        }
    }

    @Test
    fun onFavoriteActionClicked_favoriteFragmentIsStarted() {
        val destination = mockk<NavDestination>()
        every { destination.id } returns R.id.gif_list_fragment
        every { navController.currentDestination } returns destination
        every { navController.navigate(any<NavDirections>()) } returns Unit

        onView(withId(R.id.action_favorite)).perform(click())

        val action = GifListFragmentDirections.navigateToFavoriteGifListFragment()
        verify { navController.navigate(action) }
    }

    private fun mockLoadGifList() {
        coEvery { gifPagingSource.load(any()) } returns mockk(relaxed = true)
    }
}