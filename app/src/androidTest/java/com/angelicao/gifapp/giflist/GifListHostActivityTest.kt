package com.angelicao.gifapp.giflist

import android.content.ComponentName
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.angelicao.gifapp.R
import com.angelicao.repository.GifRepository
import com.angelicao.repository.data.Gif
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.After
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

    @Before
    fun setup() {
        mockRepository()
        gifViewModel = GifListViewModel(repository)
        loadKoinModules(module {
            viewModel(override = true) { gifViewModel }
        })

        launchActivity<GifListHostActivity>()
        Intents.init()
    }

    @Test
    fun onFavoriteActionClicked_favoriteActivityIsStarted() {
        onView(withId(R.id.action_favorite)).perform(click())

        intended(hasComponent(ComponentName("com.angelicao.gifapp", "com.angelicao.favorite.FavoriteGifListActivity")))
    }

    @After
    fun release() {
        Intents.release()
    }

    private fun mockRepository() {
        coEvery { repository.getGIFs() } returns GIF_LIST
    }
}