package co.abacus.android.dbconnector.demo.model

data class Tax(val name: String, val rate: Double, val type: TaxType)

enum class TaxType {
    Inclusive, Exclusive
}
