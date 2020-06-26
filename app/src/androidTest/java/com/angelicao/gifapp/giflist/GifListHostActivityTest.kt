package com.angelicao.gifapp.giflist

import androidx.navigation.NavController
import androidx.navigation.Navigation.setViewNavController
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.angelicao.gifapp.R
import com.angelicao.repository.GifRepository
import com.angelicao.repository.data.Gif
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

private val FAVORITE_GIF = Gif(id = "1",
    url = "test.com",
    title = "test",
    favorite = true)
private val GIF = Gif(id = "1",
    url = "test.com",
    title = "test",
    favorite = false)
private val GIF_LIST = listOf(FAVORITE_GIF, GIF)

@RunWith(AndroidJUnit4::class)
class GifListHostActivityTest {
    private lateinit var gifViewModel: GifListViewModel
    private val repository = mockk<GifRepository>(relaxed = true)
    private val navController = mockk<NavController>(relaxed = true, relaxUnitFun = true)

    @Before
    fun setup() {
        mockRepository()
        gifViewModel = GifListViewModel(repository)
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
        onView(withId(R.id.action_favorite)).perform(click())

        val action = GifListFragmentDirections.actionToFavoriteGifListFragment()
        verify { navController.navigate(action) }
    }

    private fun mockRepository() {
        coEvery { repository.getGIFs() } returns GIF_LIST
    }
}