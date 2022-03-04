package co.abacus.android.dbconnector.demo.model

data class CalculationResult(
    val totalTaxes: List<TotalTax>,
    val totalSurcharges: List<TotalSurcharge>,
    val orderItems: List<CalculatedOrderItem>
)
