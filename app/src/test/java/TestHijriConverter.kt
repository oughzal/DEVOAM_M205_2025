package com.example.m205

import java.time.LocalDate

/**
 * Fonction de test pour les conversions de dates Hijri/Grégorien
 */
fun main() {
    println("=== Test de conversion Grégorien vers Hijri ===\n")

    // Test 1: Date actuelle (22 Décembre 2025)
    val today = LocalDate.of(2025, 12, 22)
    val hijriToday = gregorianToHijri(today)
    println("Date Grégorienne: ${today.dayOfMonth} ${getGregorianMonthName(today.monthValue)} ${today.year}")
    println("Date Hijri: $hijriToday")
    println()

    // Test 2: Premier Ramadan 1446
    val ramadan = LocalDate.of(2025, 3, 1)
    val hijriRamadan = gregorianToHijri(ramadan)
    println("Date Grégorienne: ${ramadan.dayOfMonth} ${getGregorianMonthName(ramadan.monthValue)} ${ramadan.year}")
    println("Date Hijri: $hijriRamadan")
    println()

    // Test 3: Conversion inverse
    println("=== Test de conversion Hijri vers Grégorien ===\n")

    // Premier Rajab 1447 (selon la page des Habous)
    val hijriDate1 = HijriDate(1, 7, 1447)
    val gregDate1 = hijriToGregorian(hijriDate1)
    println("Date Hijri: $hijriDate1")
    println("Date Grégorienne: ${gregDate1.dayOfMonth} ${getGregorianMonthName(gregDate1.monthValue)} ${gregDate1.year}")
    println()

    // Premier Muharram 1447
    val hijriDate2 = HijriDate(1, 1, 1447)
    val gregDate2 = hijriToGregorian(hijriDate2)
    println("Date Hijri: $hijriDate2")
    println("Date Grégorienne: ${gregDate2.dayOfMonth} ${getGregorianMonthName(gregDate2.monthValue)} ${gregDate2.year}")
    println()

    // Test de validation
    println("=== Test de validation des dates ===\n")
    println("Date Hijri 30/12/1447 valide? ${isValidHijriDate(30, 12, 1447)}")
    println("Date Hijri 31/12/1447 valide? ${isValidHijriDate(31, 12, 1447)}")
    println("Date Grégorienne 29/2/2024 valide? ${isValidGregorianDate(29, 2, 2024)}")
    println("Date Grégorienne 29/2/2025 valide? ${isValidGregorianDate(29, 2, 2025)}")
    println()

    // Test de conversion aller-retour
    println("=== Test de conversion aller-retour ===\n")
    val testDate = LocalDate.of(2025, 6, 27)
    val hijri = gregorianToHijri(testDate)
    val backToGreg = hijriToGregorian(hijri)
    println("Date originale: ${testDate.dayOfMonth} ${getGregorianMonthName(testDate.monthValue)} ${testDate.year}")
    println("Converti en Hijri: $hijri")
    println("Retour en Grégorien: ${backToGreg.dayOfMonth} ${getGregorianMonthName(backToGreg.monthValue)} ${backToGreg.year}")
    println("Correspondance: ${testDate == backToGreg}")
}

