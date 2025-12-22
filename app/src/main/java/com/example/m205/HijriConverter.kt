package com.example.m205

import java.time.LocalDate
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Convertisseur entre dates Hijri (calendrier islamique) et Grégorien (Miladi)
 * Basé sur les algorithmes du Ministère des Habous et des Affaires Islamiques du Maroc
 */

data class HijriDate(
    val day: Int,
    val month: Int,
    val year: Int
) {
    override fun toString(): String {
        return "$day ${getHijriMonthName(month)} $year هـ"
    }
}

/**
 * Noms des mois Hijri en arabe
 */
fun getHijriMonthName(month: Int): String {
    val months = arrayOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الآخر",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )
    return if (month in 1..12) months[month - 1] else ""
}

/**
 * Noms des mois Grégoriens en arabe
 */
fun getGregorianMonthName(month: Int): String {
    val months = arrayOf(
        "يناير", "فبراير", "مارس", "أبريل", "ماي", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
    )
    return if (month in 1..12) months[month - 1] else ""
}

/**
 * Partie entière d'un nombre (gestion des nombres négatifs)
 */
private fun intPart(floatNum: Double): Int {
    return if (floatNum < -0.0000001) {
        ceil(floatNum - 0.0000001).toInt()
    } else {
        floor(floatNum + 0.0000001).toInt()
    }
}

/**
 * Convertir une date Grégorienne en date Hijri
 * @param day Jour du mois (1-31)
 * @param month Mois (1-12)
 * @param year Année grégorienne
 * @return Date Hijri correspondante
 */
fun gregorianToHijri(day: Int, month: Int, year: Int): HijriDate {
    val delta = 0 // Peut être ajusté selon les besoins

    // Calcul du jour Julien
    val jd = if ((year > 1582) || ((year == 1582) && (month > 10)) ||
                 ((year == 1582) && (month == 10) && (day > 14))) {
          intPart((1461.0 * (year + 4800 + intPart((month - 14) / 12.0))) / 4.0) +
        intPart((367.0 * (month - 2 - 12 * (intPart((month - 14) / 12.0)))) / 12.0) -
        intPart((3.0 * (intPart((year + 4900 + intPart((month - 14) / 12.0)) / 100.0))) / 4.0) +
        day - 32075 + delta
    } else {
        367 * year - intPart((7.0 * (year + 5001 + intPart((month - 9) / 7.0))) / 4.0) +
        intPart((275.0 * month) / 9.0) + day + 1729777 + delta
    }

    // Conversion du jour Julien en date Hijri
    val jd1 = jd - delta
    var l = jd1 - 1948440 + 10632
    val n = intPart((l - 1) / 10631.0)
    l = l - 10631 * n + 354
    val j = (intPart((10985 - l) / 5316.0)) * (intPart((50 * l) / 17719.0)) +
            (intPart(l / 5670.0)) * (intPart((43 * l) / 15238.0))
    l = l - (intPart((30 - j) / 15.0)) * (intPart((17719 * j) / 50.0)) -
        (intPart(j / 16.0)) * (intPart((15238 * j) / 43.0)) + 29
    val hMonth = intPart((24 * l) / 709.0)
    val hDay = l - intPart((709 * hMonth) / 24.0)
    val hYear = 30 * n + j - 30

    return HijriDate(hDay, hMonth, hYear)
}

/**
 * Convertir une date Grégorienne (LocalDate) en date Hijri
 * @param date Date grégorienne
 * @return Date Hijri correspondante
 */
fun gregorianToHijri(date: LocalDate): HijriDate {
    return gregorianToHijri(date.dayOfMonth, date.monthValue, date.year)
}

/**
 * Convertir une date Hijri en date Grégorienne
 * @param day Jour du mois Hijri (1-30)
 * @param month Mois Hijri (1-12)
 * @param year Année Hijri
 * @return Date grégorienne correspondante
 */
fun hijriToGregorian(day: Int, month: Int, year: Int): LocalDate {
    val delta = 0 // Peut être ajusté selon les besoins

    // Calcul du jour Julien depuis la date Hijri
    val jd = intPart((11 * year + 3) / 30.0) + 354 * year + 30 * month -
             intPart((month - 1) / 2.0) + day + 1948440 - 385 + delta

    // Conversion du jour Julien en date Grégorienne
    val gDay: Int
    val gMonth: Int
    val gYear: Int

    if (jd > 2299160) {
        var l = jd + 68569
        val n = intPart((4 * l) / 146097.0)
        l = l - intPart((146097 * n + 3) / 4.0)
        val i = intPart((4000 * (l + 1)) / 1461001.0)
        l = l - intPart((1461 * i) / 4.0) + 31
        val j = intPart((80 * l) / 2447.0)
        gDay = l - intPart((2447 * j) / 80.0)
        l = intPart(j / 11.0)
        gMonth = j + 2 - 12 * l
        gYear = 100 * (n - 49) + i + l
    } else {
        val j = jd + 1402
        val k = intPart((j - 1) / 1461.0)
        val l = j - 1461 * k
        val n = intPart((l - 1) / 365.0) - intPart(l / 1461.0)
        var i = l - 365 * n + 30
        val jMonth = intPart((80 * i) / 2447.0)
        gDay = i - intPart((2447 * jMonth) / 80.0)
        i = intPart(jMonth / 11.0)
        gMonth = jMonth + 2 - 12 * i
        gYear = 4 * k + n + i - 4716
    }

    return LocalDate.of(gYear, gMonth, gDay)
}

/**
 * Convertir une date Hijri en date Grégorienne
 * @param hijriDate Date Hijri
 * @return Date grégorienne correspondante
 */
fun hijriToGregorian(hijriDate: HijriDate): LocalDate {
    return hijriToGregorian(hijriDate.day, hijriDate.month, hijriDate.year)
}

/**
 * Valider une date Hijri
 * @return true si la date est valide, false sinon
 */
fun isValidHijriDate(day: Int, month: Int, year: Int): Boolean {
    if (month !in 1..12) return false
    if (day < 1) return false

    val hDays = arrayOf(30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29)

    // Années bissextiles: 2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29
    val leapYears = setOf(2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29)
    val m1h = year % 30
    val leap = if (month == 12 && m1h in leapYears) 1 else 0

    return day <= (hDays[month - 1] + leap)
}

/**
 * Valider une date Grégorienne
 * @return true si la date est valide, false sinon
 */
fun isValidGregorianDate(day: Int, month: Int, year: Int): Boolean {
    if (month !in 1..12) return false
    if (day < 1) return false

    val cDays = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    // Année bissextile
    val m1 = year % 4
    val m2 = year % 100
    val m3 = year % 400
    val leap = if (month == 2 && ((m3 == 0) || ((m1 == 0) && (m2 != 0)))) 1 else 0

    return day <= (cDays[month - 1] + leap)
}

