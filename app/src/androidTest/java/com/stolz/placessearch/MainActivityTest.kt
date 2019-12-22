package com.stolz.placessearch

import android.os.SystemClock
import android.widget.AutoCompleteTextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun startMockServer() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        mockWebServer.url("/")
    }


    @After
    fun shutdownMockServer() {
        mockWebServer.shutdown()
    }

    @Test
    fun test_search() {
        // FIXME: Integrate mock web server to return fake responses for typeahead results and place results
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(
                    TestUtils.getStringFromFile(
                        getInstrumentation().context, "testPlacesResponse.json"
                    )
                )
        )

        onView(withId(R.id.results_search_bar)).check(matches(isDisplayed()))
        onView(isAssignableFrom(AutoCompleteTextView::class.java)).perform(
            typeText("cof"))
        SystemClock.sleep(4000)
        onView(withId(R.id.typeahead_results_list)).check(matches(isDisplayed()))

        onView(isAssignableFrom(AutoCompleteTextView::class.java)).perform(
            typeText("fee"),
            pressImeActionButton()
        )
        SystemClock.sleep(4000)
        onView(withId(R.id.typeahead_results_list)).check(matches(not(isDisplayed())))
        onView(withId(R.id.search_results_list)).check(matches(isDisplayed()))
    }
}