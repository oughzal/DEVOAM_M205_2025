package com.example.xmlapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testAddNumbers() {
        onView(withId(R.id.etNb1))
            .perform(
                clearText(),
                typeText("3"),
                closeSoftKeyboard()
            )
                onView(withId(R.id.etNb2))
            .perform(
                clearText(),
                typeText("8"),
                closeSoftKeyboard()
            )
        onView(withId(R.id.btnCalculer))
            .perform(click())

        onView(withId(R.id.tvResult))
            .check(matches(withText("11")))

        onView(withId(R.id.cbHideResult)).perform(click())

        onView(withId(R.id.tvResult))
            .check(matches(not(isDisplayed())))
    }

}