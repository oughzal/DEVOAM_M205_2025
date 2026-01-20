package com.example.m03_compose

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainScreenTest1 {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testMainScreenDisplaysCorrectly() {
        // Set the content to be tested
        composeRule.setContent {
            MainScreen()
        }
        composeRule.onNodeWithTag("nb1")
            .performTextReplacement("10")


        composeRule.onNodeWithTag("nb2")
            .performTextReplacement("5")

        composeRule
            .onNodeWithTag("btnAdd")
            .performClick()

        composeRule
            .onNodeWithTag("result")
            .assertTextEquals("15")

    }

}