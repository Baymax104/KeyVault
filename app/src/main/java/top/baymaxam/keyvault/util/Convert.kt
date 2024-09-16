package top.baymaxam.keyvault.util

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import top.baymaxam.keyvault.model.domain.KeyType
import top.baymaxam.keyvault.model.domain.toKeyType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// ================================= Date String Convertor =========================================

fun Date?.toDateString(pattern: String) =
    this?.let {
        SimpleDateFormat(pattern, Locale.CHINA).format(this)
    } ?: ""

fun String?.toDate(pattern: String): Date? =
    this?.runCatching {
        SimpleDateFormat(pattern, Locale.CHINA).parse(this)
    }?.getOrNull()


// =========================================== Json ================================================

val Json = Json {
    encodeDefaults = true
    prettyPrint = true
}

// =========================================== Room ================================================

object DateConverter {

    @TypeConverter
    fun long2Date(timestamp: Long): Date = Date(timestamp)

    @TypeConverter
    fun date2Long(date: Date): Long = date.time
}

object KeyTypeConverter {

    @TypeConverter
    fun type2String(type: KeyType): String = type.name

    @TypeConverter
    fun string2Type(string: String): KeyType = string.toKeyType() ?: KeyType.Website
}