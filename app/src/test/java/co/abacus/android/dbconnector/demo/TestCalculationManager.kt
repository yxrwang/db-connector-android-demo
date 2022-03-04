package co.abacus.android.dbconnector.demo


import co.abacus.android.dbconnector.demo.calculation.CalculationManager
import co.abacus.android.dbconnector.demo.model.OrderItem
import co.abacus.android.dbconnector.demo.model.Tax
import co.abacus.android.dbconnector.demo.model.TaxType
import co.abacus.android.dbconnector.demo.util.roundToDigit
import org.junit.Assert
import org.junit.Test

class TestCalculationManager {

    @Test
    fun `test order item tax calculation`() {
        val testOrderItems = listOf(
            OrderItem("1", 10.0, listOf(Tax("GST", 0.1, TaxType.Inclusive))), OrderItem("2", 55.0, listOf(
            Tax("WET", 0.49, TaxType.Inclusive)
        )), OrderItem("3", 22.0, listOf(Tax("WET", 0.5, TaxType.Exclusive), Tax("VAT", 0.175, TaxType.Exclusive)))
        )
        val calculationManager = CalculationManager(2)
        val result = calculationManager.calculateOrderItemTax(testOrderItems)
        Assert.assertEquals(0.91, result[0].totalTaxes[0].amount.roundToDigit(2), 0.0)
        Assert.assertEquals(18.09, result[1].totalTaxes[0].amount.roundToDigit(2), 0.0)
        Assert.assertEquals(11.0, result[2].totalTaxes[0].amount.roundToDigit(2), 0.0)
        Assert.assertEquals(2, result[2].totalTaxes.size)
        Assert.assertEquals(3.85, result[2].totalTaxes[1].amount.roundToDigit(2), 0.0)
    }

    @Test
    fun `test total tax calculation`() {
        val testOrderItems = listOf(OrderItem("1", 10.0, listOf(Tax("GST", 0.1, TaxType.Inclusive))), OrderItem("2", 55.0, listOf(
            Tax("WET", 0.49, TaxType.Inclusive)
        )), OrderItem("3", 22.0, listOf(Tax("WET", 0.5, TaxType.Exclusive), Tax("VAT", 0.175, TaxType.Exclusive))), OrderItem("4", 20.0, listOf(Tax("GST", 0.1, TaxType.Inclusive)))
        )

        val calculationManager = CalculationManager(2)
        val result = calculationManager.calculateOrderItemTax(testOrderItems)
        val totalTaxResult = calculationManager.calculateTotalTax(result)
        Assert.assertEquals(2.73, totalTaxResult[0].amount, 0.0)
        Assert.assertEquals("GST", totalTaxResult[0].name)
        Assert.assertEquals(18.09, totalTaxResult[1].amount, 0.0)
        Assert.assertEquals(3.85, totalTaxResult[3].amount, 0.0)
    }
}