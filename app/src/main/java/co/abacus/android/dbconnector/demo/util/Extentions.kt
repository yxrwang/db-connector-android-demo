package co.abacus.android.dbconnector.demo.util

import kotlin.math.pow

fun Double.toCentString()=(this * 100).toInt().toString()

fun Double.roundToDigit(n: Int): Double = Math.round(this * (10.0.pow(n.toDouble()))) / 10.0.pow(n.toDouble())



