package utils

object EndingConvertor {

    fun track(amount: Long): String {
        val value = when {
            amount % 10 == 1L -> "трек"
            amount % 10 in 2L..4L -> "трека"
            amount % 100 in 11L..19L -> "треков"
            else -> "треков"
        }
        return "$amount $value"
    }

    fun minute(amount: Long): String {
        val value = when {
            amount % 10 == 1L -> "минута"
            amount % 10 in 2L..4L -> "минуты"
            amount % 100 in 11L..19L -> "минут"
            else -> "минут"
        }
        return "$amount $value"
    }
}