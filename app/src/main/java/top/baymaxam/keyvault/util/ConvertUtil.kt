package top.baymaxam.keyvault.util

import kotlinx.serialization.json.Json
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