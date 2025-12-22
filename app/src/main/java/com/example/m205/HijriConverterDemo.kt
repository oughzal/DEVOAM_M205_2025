package com.example.m205

import java.time.LocalDate

/**
 * Exemple d'utilisation des fonctions de conversion Hijri/Grégorien
 * Basé sur le code source du Ministère des Habous et des Affaires Islamiques du Maroc
 */

/**
 * Exemple 1: Afficher la date Hijri d'aujourd'hui
 */
fun showTodayHijriDate() {
    val today = LocalDate.now()
    val hijriToday = gregorianToHijri(today)

    println("Aujourd'hui:")
    println("Date Grégorienne: ${today.dayOfMonth} ${getGregorianMonthName(today.monthValue)} ${today.year}")
    println("Date Hijri: $hijriToday")
}

/**
 * Exemple 2: Calculer la date grégorienne du premier jour de Ramadan
 */
fun calculateRamadanStart(hijriYear: Int): LocalDate {
    // Premier jour de Ramadan (mois 9)
    val ramadanFirst = HijriDate(1, 9, hijriYear)
    return hijriToGregorian(ramadanFirst)
}

/**
 * Exemple 3: Convertir une date spécifique
 */
fun convertSpecificDate() {
    // Exemple: 22 Décembre 2025
    val gregDate = LocalDate.of(2025, 12, 22)
    val hijriDate = gregorianToHijri(gregDate)

    println("\nConversion de date spécifique:")
    println("${gregDate.dayOfMonth} ${getGregorianMonthName(gregDate.monthValue)} ${gregDate.year}")
    println("correspond à:")
    println("$hijriDate")
}

/**
 * Exemple 4: Afficher les dates des événements islamiques importants
 */
fun showIslamicEvents(hijriYear: Int) {
    println("\n=== Événements islamiques importants pour l'année $hijriYear ===\n")

    // Nouvel An Hijri (1er Muharram)
    val muharram1 = hijriToGregorian(1, 1, hijriYear)
    println("1er Muharram $hijriYear (Nouvel An Hijri):")
    println("  ${muharram1.dayOfMonth} ${getGregorianMonthName(muharram1.monthValue)} ${muharram1.year}")

    // Mawlid (12 Rabi' al-Awwal)
    val mawlid = hijriToGregorian(12, 3, hijriYear)
    println("\n12 Rabi' al-Awwal $hijriYear (Mawlid An-Nabawi):")
    println("  ${mawlid.dayOfMonth} ${getGregorianMonthName(mawlid.monthValue)} ${mawlid.year}")

    // Début Ramadan (1er Ramadan)
    val ramadan = hijriToGregorian(1, 9, hijriYear)
    println("\n1er Ramadan $hijriYear:")
    println("  ${ramadan.dayOfMonth} ${getGregorianMonthName(ramadan.monthValue)} ${ramadan.year}")

    // Laylat al-Qadr (27 Ramadan)
    val laylat = hijriToGregorian(27, 9, hijriYear)
    println("\n27 Ramadan $hijriYear (Laylat al-Qadr):")
    println("  ${laylat.dayOfMonth} ${getGregorianMonthName(laylat.monthValue)} ${laylat.year}")

    // Eid al-Fitr (1er Shawwal)
    val eidFitr = hijriToGregorian(1, 10, hijriYear)
    println("\n1er Shawwal $hijriYear (Eid al-Fitr):")
    println("  ${eidFitr.dayOfMonth} ${getGregorianMonthName(eidFitr.monthValue)} ${eidFitr.year}")

    // Arafat (9 Dhul-Hijjah)
    val arafat = hijriToGregorian(9, 12, hijriYear)
    println("\n9 Dhul-Hijjah $hijriYear (Jour d'Arafat):")
    println("  ${arafat.dayOfMonth} ${getGregorianMonthName(arafat.monthValue)} ${arafat.year}")

    // Eid al-Adha (10 Dhul-Hijjah)
    val eidAdha = hijriToGregorian(10, 12, hijriYear)
    println("\n10 Dhul-Hijjah $hijriYear (Eid al-Adha):")
    println("  ${eidAdha.dayOfMonth} ${getGregorianMonthName(eidAdha.monthValue)} ${eidAdha.year}")
}

/**
 * Exemple 5: Validation de dates
 */
fun validateDates() {
    println("\n=== Validation de dates ===\n")

    // Date Hijri valide
    val validHijri = isValidHijriDate(15, 9, 1447)
    println("15 Ramadan 1447 est valide? $validHijri")

    // Date Hijri invalide (31ème jour d'un mois de 30 jours)
    val invalidHijri = isValidHijriDate(31, 9, 1447)
    println("31 Ramadan 1447 est valide? $invalidHijri")

    // Date Grégorienne valide (année bissextile)
    val validGreg = isValidGregorianDate(29, 2, 2024)
    println("29 Février 2024 est valide? $validGreg")

    // Date Grégorienne invalide (année non bissextile)
    val invalidGreg = isValidGregorianDate(29, 2, 2025)
    println("29 Février 2025 est valide? $invalidGreg")
}

/**
 * Fonction principale de démonstration
 */
fun demonstrateHijriConverter() {
    println("===========================================")
    println("   Convertisseur Hijri/Grégorien")
    println("   Basé sur les algorithmes du")
    println("   Ministère des Habous du Maroc")
    println("===========================================\n")

    // Afficher la date d'aujourd'hui
    showTodayHijriDate()

    // Convertir une date spécifique
    convertSpecificDate()

    // Afficher les événements islamiques de l'année 1447
    showIslamicEvents(1447)

    // Validation de dates
    validateDates()

    println("\n===========================================")
}

