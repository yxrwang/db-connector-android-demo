package co.abacus.android.dbconnector.demo


import co.abacus.android.dbconnector.demo.calculation.TaxCalculator
import co.abacus.android.dbconnector.demo.util.roundToDigit
import org.junit.Assert
import org.junit.Test

class TestTaxCalculator {

    @Test
    fun testInclusiveTaxCalculation() {
        Assert.assertEquals(0.9091, TaxCalculator.calculateInclusiveTax(10.0, 0.1).roundToDigit(4), 0.0)
        Assert.assertEquals(0.05, TaxCalculator.calculateInclusiveTax(0.5, 0.1).roundToDigit(2), 0.0)
    }

    @Test
    fun testExclusiveTaxCalculation() {

    }
}