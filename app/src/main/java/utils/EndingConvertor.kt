package utils

object EndingConvertor {

    fun track(amount: Int): String {
        val value = when {
            amount % 10 == 1 -> "трек"
            amount % 10 in 2..4 -> "трека"
            amount % 100 in 11..19 -> "треков"
            else -> "треков"
        }
        return "$amount $value"
    }

    fun minute(amount: Int): String {
        val value = when {
            amount % 10 == 1 -> "минута"
            amount % 10 in 2..4 -> "минуты"
            amount % 100 in 11..19 -> "минут"
            else -> "минут"
        }
        return "$amount $value"
    }

    fun minute(amount: Long): String = minute(amount.toInt())
}