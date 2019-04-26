package com.example.airport20.utils

fun sanitizeString(str: String): String {
    var localStr = str
    val re = Regex("[^A-Za-z0-9]")
    localStr = re.replace(str, "")
    return localStr
}
