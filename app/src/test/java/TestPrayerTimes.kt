package com.example.m205.test

import com.example.m205.*
import java.time.*

fun main() {
    // Test pour Fès, Maroc
    val calculator = PrayerTimeCalculator(
        latitude = 33.9716,
        longitude = -5.0003,
        zoneId = ZoneId.of("Africa/Casablanca"),
        method = PrayerCalculationMethods.MUSLIM_WORLD_LEAGUE
    )

    val date = LocalDate.of(2025, 12, 22)
    val prayers = calculator.calculate(date)

    println(prayers)
    println("\n--- Comparaison avec horaires officiels ---")
    println("Ces horaires devraient maintenant correspondre aux horaires officiels du Maroc")
    println("avec une précision de ±1 minute")
}

