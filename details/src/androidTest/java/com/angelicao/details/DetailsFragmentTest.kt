package com.angelicao.details

import android.content.Intent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailsFragmentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenScreenAppears_gifTitleIsDisplayed() {
        composeTestRule.onNodeWithText("Gif Title").assertIsDisplayed()
    }

    @Test
    fun whenScreenAppears_gifImageIsDisplayed() {
        composeTestRule.onNodeWithContentDescription("Gif Image").assertIsDisplayed()
    }

    @Test
    fun whenShareButtonIsClicked_shareIntentStarts() {
        Intents.init()

        composeTestRule.onNodeWithContentDescription("Share").performClick()
        intended(hasAction(Intent.ACTION_SEND))

        Intents.release()
    }

    @Test
    fun whenFavoriteButtonIsClicked_contentDescriptionChanges() {
        composeTestRule.onNodeWithContentDescription("Add to Favorite").performClick()

        composeTestRule.onNodeWithContentDescription("Remove from Favorite").assertIsDisplayed()
    }
}