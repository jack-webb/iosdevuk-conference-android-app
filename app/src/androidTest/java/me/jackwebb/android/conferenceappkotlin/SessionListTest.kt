package me.jackwebb.android.conferenceappkotlin

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.internal.util.Checks
import androidx.test.rule.ActivityTestRule
import me.jackwebb.android.conferenceappkotlin.ui.util.SessionRecyclerViewAdapter
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class SessionListTest {
    private val NUM_TALKS = 22
    private val NUM_SESSIONS = 34

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Before
    fun init() {
        val navController = activityRule.activity.findNavController(R.id.nav_host_fragment)
    }

    private fun onSessionList(): ViewInteraction = onView(withId(R.id.sessionListRecyclerView))

    @Test
    fun showAllItems() {
        onSessionList().check(RecyclerViewItemCountAssertion(NUM_SESSIONS))
    }

    @Test
    fun showFilterTalks() {
        // Just show talks
        onView(withId(R.id.action_filter)).perform(click())
        onView(withText(R.string.talks)).perform(click())
        onSessionList().check(RecyclerViewItemCountAssertion(NUM_TALKS))

        // Reset and show everything
        onView(withId(R.id.action_filter)).perform(click())
        onView(withText(R.string.everything)).perform(click())
        onSessionList().check(RecyclerViewItemCountAssertion(NUM_SESSIONS))
    }

    @Test
    fun showQuery() {
        onView(withId(R.id.action_search)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText("UI"))
        onSessionList().check(RecyclerViewItemCountAssertion(2)) // There are two sessions with "UI" in the title
    }

    @Test
    fun showQueryNoResults() {
        onView(withId(R.id.action_search)).perform(click())
        onView(withId(R.id.search_src_text)).perform(typeText("abcdefg"))
        onSessionList().check(RecyclerViewItemCountAssertion(0)) // should be no results
    }

    // Make sure the first session isn't already favourited - todo ensure this for the test
    @Test
    fun swipeAction() {
        // Favourite a session
        onSessionList().perform(actionOnItemAtPosition<SessionRecyclerViewAdapter.SessionViewHolder>(0, swipeLeft()))
        onView(allOf(withId(R.id.favourite), withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
        // Swipe again to show the unfavourite button
        onSessionList().perform(actionOnItemAtPosition<SessionRecyclerViewAdapter.SessionViewHolder>(0, swipeLeft()))
        // Check the button changed
        onView(allOf(withId(R.id.favourite), withEffectiveVisibility(Visibility.VISIBLE)))
            .check(matches(hasBackgroundColor(R.color.warning)))

        // Unfavourite the session
        onSessionList().perform(actionOnItemAtPosition<SessionRecyclerViewAdapter.SessionViewHolder>(0, swipeLeft()))
        onView(allOf(withId(R.id.favourite), withEffectiveVisibility(Visibility.VISIBLE))).perform(click())
        // Swipe again to show the favourite button
        onSessionList().perform(actionOnItemAtPosition<SessionRecyclerViewAdapter.SessionViewHolder>(0, swipeLeft()))
        // Check the button changed
        onView(allOf(withId(R.id.favourite), withEffectiveVisibility(Visibility.VISIBLE)))
            .check(matches(hasBackgroundColor(R.color.favourite)))
    }

    @Test
    fun openDetailView() {
        onSessionList().perform(
            actionOnItemAtPosition<SessionRecyclerViewAdapter.SessionViewHolder>(0, click())
        )
        // First session in list values are as follows
        onView(withId(R.id.txtSessionTitle)).check(matches(withText("Using ARKit with SpriteKit")))
        onView(withId(R.id.txtSessionType)).check(matches(withText("Workshop")))
    }
}

// https://stackoverflow.com/a/37339656
class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {
    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertThat(adapter!!.itemCount, `is`(expectedCount))
    }
}


// https://stackoverflow.com/a/51668551
private fun hasBackgroundColor(colorRes: Int): Matcher<View> {
    Checks.checkNotNull(colorRes)

    return object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description?) {
            description?.appendText("background color: $colorRes")
        }

        override fun matchesSafely(item: View?): Boolean {
            return (item?.background as ColorDrawable).color == item.context?.getColor(colorRes)
        }
    }
}

