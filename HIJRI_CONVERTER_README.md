# Convertisseur de Dates Hijri/Grégorien

## Description

Ce convertisseur de dates a été créé en utilisant les **algorithmes officiels** du code source JavaScript de la page du **Ministère des Habous et des Affaires Islamiques du Maroc** (https://www.habous.gov.ma).

Les fonctions permettent de convertir entre:
- **Date Grégorienne (Miladi)** → **Date Hijri (Islamique)**
- **Date Hijri (Islamique)** → **Date Grégorienne (Miladi)**

## Fichiers créés

### 1. `HijriConverter.kt`
Contient les fonctions principales de conversion:

#### Fonctions principales:

```kotlin
// Convertir Grégorien → Hijri
fun gregorianToHijri(day: Int, month: Int, year: Int): HijriDate
fun gregorianToHijri(date: LocalDate): HijriDate

// Convertir Hijri → Grégorien
fun hijriToGregorian(day: Int, month: Int, year: Int): LocalDate
fun hijriToGregorian(hijriDate: HijriDate): LocalDate

// Validation
fun isValidHijriDate(day: Int, month: Int, year: Int): Boolean
fun isValidGregorianDate(day: Int, month: Int, year: Int): Boolean

// Noms des mois
fun getHijriMonthName(month: Int): String      // En arabe
fun getGregorianMonthName(month: Int): String  // En arabe
```

### 2. `HijriConverterDemo.kt`
Exemples d'utilisation pratiques

### 3. `TestHijriConverter.kt`
Tests de validation des conversions

## Utilisation

### Exemple 1: Convertir la date d'aujourd'hui en Hijri

```kotlin
import com.example.m205.*
import java.time.LocalDate

val today = LocalDate.now()
val hijriToday = gregorianToHijri(today)
println("Aujourd'hui: $hijriToday")
// Sortie: 22 رجب 1447 هـ
```

### Exemple 2: Convertir une date Hijri en Grégorien

```kotlin
// Premier jour de Ramadan 1447
val ramadan = HijriDate(1, 9, 1447)
val gregDate = hijriToGregorian(ramadan)
println("1er Ramadan 1447 = $gregDate")
```

### Exemple 3: Calculer les dates des événements islamiques

```kotlin
// Eid al-Fitr 1447 (1er Shawwal)
val eidFitr = hijriToGregorian(1, 10, 1447)
println("Eid al-Fitr 1447: ${eidFitr.dayOfMonth} ${getGregorianMonthName(eidFitr.monthValue)} ${eidFitr.year}")

// Eid al-Adha 1447 (10 Dhul-Hijjah)
val eidAdha = hijriToGregorian(10, 12, 1447)
println("Eid al-Adha 1447: ${eidAdha.dayOfMonth} ${getGregorianMonthName(eidAdha.monthValue)} ${eidAdha.year}")
```

### Exemple 4: Validation de dates

```kotlin
// Vérifier si une date Hijri est valide
val isValid = isValidHijriDate(30, 9, 1447)
println("30 Ramadan 1447 est valide? $isValid")

// Vérifier une année bissextile
val isBissextile = isValidGregorianDate(29, 2, 2024)
println("29 Février 2024 est valide? $isBissextile")
```

## Structure de données

### HijriDate

```kotlin
data class HijriDate(
    val day: Int,      // Jour (1-30)
    val month: Int,    // Mois (1-12)
    val year: Int      // Année Hijri
)
```

## Mois Hijri

1. محرم (Muharram)
2. صفر (Safar)
3. ربيع الأول (Rabi' al-Awwal)
4. ربيع الآخر (Rabi' al-Thani)
5. جمادى الأولى (Jumada al-Awwal)
6. جمادى الآخرة (Jumada al-Thani)
7. رجب (Rajab)
8. شعبان (Sha'ban)
9. رمضان (Ramadan)
10. شوال (Shawwal)
11. ذو القعدة (Dhul-Qi'dah)
12. ذو الحجة (Dhul-Hijjah)

## Algorithme

L'algorithme utilise le **Jour Julien** comme intermédiaire:
1. **Grégorien → Jour Julien → Hijri**
2. **Hijri → Jour Julien → Grégorien**

### Années bissextiles Hijri

Le calendrier Hijri suit un cycle de 30 ans où les années suivantes sont bissextiles:
- Années: **2, 5, 7, 10, 13, 16, 18, 21, 24, 26, 29**
- Le mois de Dhul-Hijjah a **30 jours** au lieu de 29

## Précision

⚠️ **Note importante**: Comme indiqué sur le site du Ministère des Habous:
> "محول التاريخ تقريبي. للحصول على التاريخ الصحيح اتصل بمصلحة مراقبة الاهلة"

La conversion est **approximative**. Pour les dates officielles exactes (début de Ramadan, Eids, etc.), il faut contacter le service de surveillance des croissants lunaires du Ministère.

## Source

Code source extrait de: https://www.habous.gov.ma/محول-التاريخ

Le code JavaScript original a été fidèlement transcrit en Kotlin en conservant les mêmes algorithmes et constantes.

## Auteur

Basé sur les algorithmes du **Ministère des Habous et des Affaires Islamiques du Maroc**

Adapté en Kotlin pour le projet M205

