package co.abacus.android.dbconnector.demo.calculation

object TaxCalculator {

    fun calculateInclusiveTax(amount: Double, taxRate: Double) = (amount * taxRate) / (1 + taxRate)
    fun calculateExclusiveTax(amount: Double, taxRate: Double) = amount * taxRate
}