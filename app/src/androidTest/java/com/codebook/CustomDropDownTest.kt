package com.codebook

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.codebook.instatest.ui.CustomDropDown
import org.junit.Rule
import org.junit.Test

class CustomDropDownTest {
    @get:Rule
    val composeTestRule= createComposeRule()

    @Test
    fun dropdown_expands_when_clicked() {
        val list = listOf("Item 1", "Item 2", "Item 3")
        var selectedIndex = -1
        val onChange: (Int) -> Unit = { index -> selectedIndex = index }

        composeTestRule.setContent {
            CustomDropDown(
                label = "Select an item",
                list = list,
                onChange = onChange
            )
        }

        composeTestRule.onNodeWithText("Select an item").performClick()
        composeTestRule.onNodeWithText("Item 1").assertIsDisplayed()
    }

    // Empty list is passed as items
    @Test
    fun empty_list_passed_as_items() {
        val list = emptyList<String>()
        var selectedIndex = -1
        val onChange: (Int) -> Unit = { index -> selectedIndex = index }

        composeTestRule.setContent {
            CustomDropDown(
                label = "Select an item",
                list = list,
                onChange = onChange
            )
        }

        composeTestRule.onNodeWithText("Select an item").performClick()
        composeTestRule.onNodeWithText("Select an item").assertIsDisplayed()
        composeTestRule.onNodeWithText("Item 1").assertDoesNotExist()
    }
}

