package lt.vitalikas.unsplash.utils

interface CurrencyConverter {
    val currencyCode: String
    fun convertRubToCurr(rub: Int): Double
}

class RubToEurConverter : CurrencyConverter {
    override val currencyCode: String = "EUR"

    override fun convertRubToCurr(rub: Int): Double = rub * rubToEurRate

    companion object {
        const val rubToEurRate = 0.012
    }
}

class RubToUsdConverter : CurrencyConverter {
    override val currencyCode: String = "USD"

    override fun convertRubToCurr(rub: Int): Double = rub * rubToUsdRate

    companion object {
        const val rubToUsdRate = 0.013
    }
}

object Converter {
    private val rubToEurConverter = RubToEurConverter()
    private val rubToUsdConverter = RubToUsdConverter()

    fun get(currencyCode: String): CurrencyConverter {
        return when (currencyCode) {
            "EUR" -> rubToEurConverter
            "USD" -> rubToUsdConverter
            else -> object : CurrencyConverter {
                override val currencyCode: String = currencyCode

                override fun convertRubToCurr(rub: Int): Double {
                    return rub * readInput()
                }

                fun readInput(): Double {
                    print("Enter $currencyCode exchange rate: ")
                    return readLine()?.toDoubleOrNull() ?: run {
                        println("Incorrect input")
                        readInput()
                    }
                }
            }
        }
    }
}

fun main() {
    val rub = 100
    val curr = "CAD"
    val converter = Converter.get(curr)
    val amount = Converter.get(curr).convertRubToCurr(rub)
    print("$rub RUB = $amount ${converter.currencyCode}")
}