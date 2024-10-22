package data.convertors

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object CustomConvertors {

    private val gson = Gson()

    @TypeConverter
    fun toJson(trackIds: List<Int>): String = gson.toJson(trackIds)

    @TypeConverter
    fun fromJson(json: String): List<Int> =
        gson.fromJson(json, object : TypeToken<List<Long>>() {}.type)
}
