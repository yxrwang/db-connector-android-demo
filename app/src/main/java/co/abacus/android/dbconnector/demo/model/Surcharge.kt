package co.abacus.android.dbconnector.demo.model

data class Surcharge(val name: String, val rate: Double, val type: SurchargeType, val tax: Tax?)

enum class SurchargeType {
    Percentage, Fixed
}
