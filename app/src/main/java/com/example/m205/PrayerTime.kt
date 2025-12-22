package com.example.m205

import java.time.*
import kotlin.math.*

/* ============================================================
 * DATA : Résultat
 * ============================================================ */

data class PrayerTimes(
    val fajr: LocalDateTime,
    val sunrise: LocalDateTime,
    val dhuhr: LocalDateTime,
    val asr: LocalDateTime,
    val maghrib: LocalDateTime,
    val isha: LocalDateTime
) {
    override fun toString(): String {
        val formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm")
        return "date: ${fajr.toLocalDate()}\n" +
                "Fajr: ${fajr.format(formatter)}\n" +
                "Sunrise: ${sunrise.format(formatter)}\n" +
                "Dhuhr: ${dhuhr.format(formatter)}\n" +
                "Asr: ${asr.format(formatter)}\n" +
                "Maghrib: ${maghrib.format(formatter)}\n" +
                "Isha: ${isha.format(formatter)}"
    }
}

/* ============================================================
 * ENUM : Méthode de calcul de Asr
 * ============================================================ */

enum class AsrMethod {
    SHAFII,
    HANAFI
}

/* ============================================================
 * DATA : Méthode de calcul
 * ============================================================ */

data class CalculationMethod(
    val fajrAngle: Double,
    val ishaAngle: Double? = null,
    val ishaAfterMaghrib: Int? = null,
    val asrMethod: AsrMethod = AsrMethod.SHAFII
)

/* ============================================================
 * MÉTHODES STANDARDS
 * ============================================================ */

object PrayerCalculationMethods {

    val MUSLIM_WORLD_LEAGUE = CalculationMethod(
        fajrAngle = 18.0,
        ishaAngle = 17.0
    )

    val ISNA = CalculationMethod(
        fajrAngle = 15.0,
        ishaAngle = 15.0
    )

    val EGYPT = CalculationMethod(
        fajrAngle = 19.5,
        ishaAngle = 17.5
    )

    val UMM_AL_QURA = CalculationMethod(
        fajrAngle = 18.5,
        ishaAfterMaghrib = 90
    )

    val KARACHI = CalculationMethod(
        fajrAngle = 18.0,
        ishaAngle = 18.0,
        asrMethod = AsrMethod.HANAFI
    )
}

/* ============================================================
 * CALCULATEUR PRINCIPAL
 * ============================================================ */

class PrayerTimeCalculator(
    private val latitude: Double,
    private val longitude: Double,
    method: CalculationMethod? = null,
    private val zoneId: ZoneId = ZoneId.systemDefault()
) {

    private val method: CalculationMethod =
        method ?: detectMethodByLocation(latitude, longitude)

    /* ===================== API ===================== */

    fun calculate(date: LocalDate): PrayerTimes {

        val jd = getJulianDay(date)
        val declination = getSolarDeclination(jd)
        val equation = getEquationOfTime(jd)

        val dhuhr = 12 + timeZoneOffset(date) - longitude / 15.0 + equation / 60.0

        val fajr = dhuhr - hourAngle(-method.fajrAngle, declination)
        val sunrise = dhuhr - hourAngle(-0.833, declination)
        val asr = dhuhr + hourAngleAsr(declination)
        val maghrib = dhuhr + hourAngle(-0.833, declination)

        val isha = when {
            method.ishaAngle != null ->
                dhuhr + hourAngle(-method.ishaAngle, declination)

            method.ishaAfterMaghrib != null ->
                maghrib + method.ishaAfterMaghrib / 60.0

            else -> error("Méthode Isha invalide")
        }

        return PrayerTimes(
            fajr = toLocalDateTime(date, fajr),
            sunrise = toLocalDateTime(date, sunrise),
            dhuhr = toLocalDateTime(date, dhuhr),
            asr = toLocalDateTime(date, asr),
            maghrib = toLocalDateTime(date, maghrib),
            isha = toLocalDateTime(date, isha)
        )
    }

    fun getCalculationMethod(): CalculationMethod = method

    /* ===================== DÉTECTION AUTO ===================== */

    private fun detectMethodByLocation(lat: Double, lon: Double): CalculationMethod =
        when {
            lat in 16.0..32.0 && lon in 34.0..56.0 ->
                PrayerCalculationMethods.UMM_AL_QURA

            lat in 28.0..37.0 && lon in -12.0..12.0 ->
                PrayerCalculationMethods.MUSLIM_WORLD_LEAGUE

            lat in 22.0..32.0 && lon in 25.0..36.0 ->
                PrayerCalculationMethods.EGYPT

            lat in 5.0..35.0 && lon in 60.0..90.0 ->
                PrayerCalculationMethods.KARACHI

            lat in 35.0..60.0 && lon in -130.0..40.0 ->
                PrayerCalculationMethods.ISNA

            else ->
                PrayerCalculationMethods.MUSLIM_WORLD_LEAGUE
        }

    /* ===================== ASTRONOMIE ===================== */

    private fun getJulianDay(date: LocalDate): Double {
        val y = date.year
        val m = date.monthValue
        val d = date.dayOfMonth
        val a = (14 - m) / 12
        val y2 = y + 4800 - a
        val m2 = m + 12 * a - 3
        return d.toDouble() + (153.0 * m2 + 2) / 5.0 +
                365.0 * y2 + y2 / 4.0 - y2 / 100.0 + y2 / 400.0 - 32045.0
    }

    private fun getSolarDeclination(jd: Double): Double {
        val n = jd - 2451545.0
        val g = Math.toRadians((357.529 + 0.98560028 * n) % 360)
        val q = Math.toRadians((280.459 + 0.98564736 * n) % 360)
        val l = q + Math.toRadians(1.915) * sin(g) +
                Math.toRadians(0.020) * sin(2 * g)
        return Math.toDegrees(
            asin(sin(Math.toRadians(23.439)) * sin(l))
        )
    }

    private fun getEquationOfTime(jd: Double): Double {
        val n = jd - 2451545.0

        // Anomalie moyenne du Soleil
        val g = Math.toRadians((357.529 + 0.98560028 * n) % 360)

        // Longitude moyenne du Soleil
        val q = (280.459 + 0.98564736 * n) % 360

        // Longitude vraie du Soleil
        val l = q + 1.915 * sin(g) + 0.020 * sin(2 * g)

        // Obliquité de l'écliptique
        val e = Math.toRadians(23.439)

        // Ascension droite
        val lRad = Math.toRadians(l)
        var ra = Math.toDegrees(atan2(cos(e) * sin(lRad), cos(lRad)))

        // Normaliser RA dans [0, 360]
        ra = (ra + 360) % 360

        // Équation du temps (en minutes)
        var eqTime = 4 * (q - ra)

        // Ajuster pour les discontinuités
        if (eqTime > 20) eqTime -= 1440
        if (eqTime < -20) eqTime += 1440

        return eqTime
    }

    private fun hourAngle(angle: Double, declination: Double): Double {
        val latRad = Math.toRadians(latitude)
        val decRad = Math.toRadians(declination)
        val angRad = Math.toRadians(angle)

        return Math.toDegrees(
            acos(
                (sin(angRad) - sin(latRad) * sin(decRad)) /
                        (cos(latRad) * cos(decRad))
            )
        ) / 15
    }

    private fun hourAngleAsr(declination: Double): Double {
        val latRad = Math.toRadians(latitude)
        val decRad = Math.toRadians(declination)
        val factor = if (method.asrMethod == AsrMethod.HANAFI) 2 else 1
        val angle = atan(1.0 / (factor + tan(abs(latRad - decRad))))

        return Math.toDegrees(
            acos(
                (sin(angle) - sin(latRad) * sin(decRad)) /
                        (cos(latRad) * cos(decRad))
            )
        ) / 15
    }

    private fun timeZoneOffset(date: LocalDate = LocalDate.now()): Double =
        zoneId.rules.getOffset(date.atStartOfDay(zoneId).toInstant()).totalSeconds / 3600.0

    private fun toLocalDateTime(date: LocalDate, time: Double): LocalDateTime {
        val t = (time + 24) % 24
        val hour = t.toInt()
        val minute = ((t - hour) * 60).toInt()
        return date.atTime(hour, minute)
    }
}

val calculator = PrayerTimeCalculator(
    latitude = 32.9487952821122,
    longitude = -5.66577036117201,
    zoneId = ZoneId.of("Africa/Casablanca"),
    method = PrayerCalculationMethods.MUSLIM_WORLD_LEAGUE
)

val prayers = calculator.calculate(LocalDate.now())  
