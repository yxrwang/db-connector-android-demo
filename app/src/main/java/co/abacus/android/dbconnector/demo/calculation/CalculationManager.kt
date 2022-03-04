package co.abacus.android.dbconnector.demo.calculation


import co.abacus.android.dbconnector.demo.model.*
import co.abacus.android.dbconnector.demo.model.SurchargeType.*
import co.abacus.android.dbconnector.demo.model.TaxType.*
import co.abacus.android.dbconnector.demo.util.roundToDigit

class CalculationManager(private val currencyDigits: Int) {

    val currentTransactionItems = ArrayList<OrderItem>()

    fun calculateOrderItemTax(orderItems: List<OrderItem>) =
        List(orderItems.size) {
            val item = orderItems[it]
            CalculatedOrderItem(
                item.id,
                List(item.taxes.size) { taxIndex -> getTax(item.price, item.taxes[taxIndex])})
        }

    fun calculateTotalTax(calculatedOrderItems: List<CalculatedOrderItem>): List<TotalTax> {
        val taxes =
            calculatedOrderItems.flatMap { it.totalTaxes }.groupBy { Pair(it.name, it.type) }
        val taxNamesAndTypes = taxes.keys.toTypedArray()
        return List(taxes.size) { index ->
            val taxNameAndType = taxNamesAndTypes[index]
            TotalTax(
                taxNameAndType.first,
                taxes[taxNameAndType]?.sumOf { it.amount }?.roundToDigit(currencyDigits) ?: 0.0,
                taxNameAndType.second
            )
        }
    }

    fun calculateSurcharge(surcharge: Surcharge, transactionTotal: Double): TotalSurcharge {
        val surchargeAmount = when (surcharge.type) {
            Percentage -> (transactionTotal * surcharge.rate).roundToDigit(currencyDigits)
            Fixed -> (transactionTotal+surcharge.rate).roundToDigit(currencyDigits)
        }
        return TotalSurcharge(surcharge.name, surchargeAmount, surcharge.tax?.let { getTax(surchargeAmount, it) })
    }

    fun calculateOrderTotal(orderItems: List<OrderItem>) = orderItems.sumOf { it.price }.roundToDigit(currencyDigits)

    private fun getTax(amount: Double, tax: Tax) =
        TotalTax(
            tax.name, when (tax.type) {
                Inclusive -> TaxCalculator.calculateInclusiveTax(amount, tax.rate)
                Exclusive -> TaxCalculator.calculateExclusiveTax(amount, tax.rate)
            }, tax.type
        )
}