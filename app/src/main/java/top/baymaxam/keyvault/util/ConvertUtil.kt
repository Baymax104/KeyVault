package top.baymaxam.keyvault.util

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import top.baymaxam.keyvault.model.domain.PassType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ================================= Date String Convertor =========================================

fun Date?.toDateString() =
    this?.let {
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(this)
    } ?: ""

fun String?.toDate(): Date? =
    this?.runCatching {
        SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).parse(this)
    }?.getOrNull()


// =========================================== Json ================================================

val JsonCoder = Json {
    encodeDefaults = true
    prettyPrint = true
}


object DateConverter {

    @TypeConverter
    fun string2Date(str: String): Date {
        return str.runCatching {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(this)
        }.getOrNull() ?: Date()
    }

    @TypeConverter
    fun date2String(date: Date): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date)
    }

}

object PassTypeConverter {

    @TypeConverter
    fun type2Int(type: PassType): Int {
        return when (type) {
            PassType.Website -> 0
            PassType.Card -> 1
        }
    }

    @TypeConverter
    fun int2Type(i: Int): PassType {
        return when (i) {
            0 -> PassType.Website
            1 -> PassType.Card
            else -> PassType.Website
        }
    }
}