package gl.khj.oqaaserissoqai

import android.widget.Spinner
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.containsString
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    @Test
    fun launchShowsCoreControls() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        try {
            onView(withText("OQAASERISSOQ AI")).check(matches(isDisplayed()))
            onView(withText("AI aallartiguk")).check(matches(isDisplayed()))
            onView(withText("Web version ammaruk")).check(matches(isDisplayed()))
        } finally {
            scenario.close()
        }
    }

    @Test
    fun selectingChildModeShowsPrivacyAndParentNotice() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        try {
            onView(isAssignableFrom(Spinner::class.java)).perform(click())
            onData(anything()).atPosition(4).perform(click())
            onView(withText(containsString("Meeqqap aqqa"))).check(matches(isDisplayed()))
            onView(withText(containsString("angajoqqaamik"))).check(matches(isDisplayed()))
        } finally {
            scenario.close()
        }
    }
}
