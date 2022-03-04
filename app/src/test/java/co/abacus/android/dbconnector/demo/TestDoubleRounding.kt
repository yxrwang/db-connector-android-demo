package co.abacus.android.dbconnector.demo

import co.abacus.android.dbconnector.demo.util.roundToDigit
import org.junit.Assert
import org.junit.Test

class TestDoubleRounding {

    @Test
    fun `test double round to 2 digits`() {
        Assert.assertEquals(0.04, 0.04499.roundToDigit(2), 0.0)
    }

    @Test
    fun `test double round to 1 digit`() {
        Assert.assertEquals(0.0, 0.1.roundToDigit(0), 0.0)
    }
}