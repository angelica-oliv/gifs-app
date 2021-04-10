package com.angelicao.details

import androidx.compose.material.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.angelicao.repository.data.Gif
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private val GIF = Gif(id = "1",
    url = "test.com",
    largerGifUrl = "test.com",
    title = "test",
    favorite = false)

class DetailsFragmentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        composeTestRule.setContent {
            MaterialTheme {
                GifImageDetails(GIF)
            }
        }
    }

    @Test
    fun whenScreenAppears_gifTitleIsDisplayed() {
        composeTestRule.onNodeWithText("Title:").assertIsDisplayed()
        composeTestRule.onNodeWithText(GIF.title).assertIsDisplayed()
    }

    @Test
    fun whenScreenAppears_gifImageIsDisplayed() {
        composeTestRule.onNodeWithContentDescription("Gif image").assertIsDisplayed()
    }
}