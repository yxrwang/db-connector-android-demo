package co.abacus.android.dbconnector.demo.calculation

object SurchargeCalculator {

    fun calculateSurcharge(transactionTotal: Double, rate: Double) = transactionTotal * rate
}