package com.example.m03_compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainScreenTest2 {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testMainScreen() {
        composeRule.setContent {
            MainScreen()
        }

        composeRule.onNodeWithTag("nb1")
            .performTextReplacement("9")
        composeRule.onNodeWithTag("nb2")
            .performTextReplacement("11")
        composeRule.onNodeWithText("Additionner")
            .performClick()
        composeRule.onNodeWithTag("result")
            .assertTextEquals("20")
        composeRule.onNodeWithTag("chkHideResult")
            .performClick()
        composeRule.onNodeWithTag("result")
            .assertIsNotDisplayed()

        composeRule.onNodeWithTag("chkHideResult")
            .performClick()
        composeRule.onNodeWithTag("result")
            .assertIsDisplayed()

        composeRule.onNodeWithTag("nb1")



    }

}