package mick.studio.itsfuntorun

import mick.studio.itsfuntorun.activities.RunActivity
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

  /*  @Test
    fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(EmailValidator.isValidEmail("name@email.com"))
    }*/

    @Test
    fun if_intent_do_not_have_extra() {
        //assertTrue(RunActivity("name@email.com"))
    }
}