package gl.khj.oqaaserissoqai

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private lateinit var device: UiDevice

    @Before
    fun prepareDevice() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.wakeUp()
        device.executeShellCommand("wm dismiss-keyguard")
    }

    @Test
    fun launchShowsCoreControls() {
        ActivityScenario.launch(MainActivity::class.java).use {
            assertTextAppears("OQAASERISSOQ AI")
            assertTextAppears("Suliassaq")
            assertObjectAppears(By.clazz("android.widget.Spinner"), "Tool selector")
            assertObjectAppears(By.clazz("android.widget.EditText"), "Text input")
        }
    }

    @Test
    fun selectingChildModeShowsPrivacyAndParentNotice() {
        ActivityScenario.launch(MainActivity::class.java).use {
            val spinner = device.wait(Until.findObject(By.clazz("android.widget.Spinner")), TIMEOUT_MS)
            assertTrue("Tool selector did not appear", spinner != null)
            spinner.click()

            val childMode = device.wait(Until.findObject(By.text("Meeqqat Create")), TIMEOUT_MS)
            assertTrue("Meeqqat Create option did not appear", childMode != null)
            childMode.click()

            assertTextContains("Meeqqap aqqa")
            assertTextContains("angajoqqaamik")
        }
    }

    private fun assertTextAppears(text: String) {
        assertTrue(
            "Expected text did not appear: $text",
            device.wait(Until.hasObject(By.text(text)), TIMEOUT_MS)
        )
    }

    private fun assertTextContains(text: String) {
        assertTrue(
            "Expected text fragment did not appear: $text",
            device.wait(Until.hasObject(By.textContains(text)), TIMEOUT_MS)
        )
    }

    private fun assertObjectAppears(selector: androidx.test.uiautomator.BySelector, label: String) {
        assertTrue(
            "$label did not appear",
            device.wait(Until.hasObject(selector), TIMEOUT_MS)
        )
    }

    private companion object {
        const val TIMEOUT_MS = 10_000L
    }
}
