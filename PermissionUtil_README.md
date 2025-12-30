# PermissionUtil - Gestionnaire de Permissions Android

Une classe utilitaire complète et réutilisable pour gérer les permissions Android dans tous vos projets.

## 📋 Fonctionnalités

- ✅ Vérification des permissions (simple et multiple)
- ✅ Demande de permissions (simple et multiple)
- ✅ Détection des permissions définitivement refusées
- ✅ Gestion du "rationale" (explication)
- ✅ Ouverture automatique des paramètres de l'application
- ✅ Callbacks pour tous les scénarios
- ✅ Codes de requête prédéfinis
- ✅ Compatible avec Activities traditionnelles et Jetpack Compose

## 🚀 Installation

Copiez simplement le fichier `PermissionUtil.kt` dans votre projet Android.

Aucune dépendance externe n'est requise (utilise uniquement AndroidX).

## 📖 Utilisation de base

### 1. Vérifier si une permission est accordée

```kotlin
if (PermissionUtil.isPermissionGranted(context, Manifest.permission.CAMERA)) {
    // Utiliser la caméra
} else {
    // Demander la permission
}
```

### 2. Demander une permission avec gestion complète

```kotlin
PermissionUtil.handlePermission(
    activity = this,
    permission = Manifest.permission.READ_CONTACTS,
    requestCode = PermissionUtil.RequestCodes.CONTACTS,
    onGranted = {
        // Permission déjà accordée
        chargerContacts()
    },
    onDenied = {
        // Permission refusée, peut encore être demandée
        showExplicationDialog()
    },
    onPermanentlyDenied = {
        // Permission définitivement refusée, rediriger vers les paramètres
        PermissionUtil.openAppSettings(this)
    }
)
```

### 3. Gérer le résultat dans onRequestPermissionsResult

```kotlin
override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    
    PermissionUtil.handlePermissionResult(
        requestCode = requestCode,
        permissions = permissions,
        grantResults = grantResults,
        expectedRequestCode = PermissionUtil.RequestCodes.CONTACTS,
        onGranted = {
            // Permission accordée
            chargerContacts()
        },
        onDenied = {
            // Permission refusée
            Toast.makeText(this, "Permission refusée", Toast.LENGTH_SHORT).show()
        }
    )
}
```

## 🎯 Exemples avancés

### Gérer plusieurs permissions

```kotlin
val permissions = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
)

if (PermissionUtil.arePermissionsGranted(this, *permissions)) {
    demarrerEnregistrement()
} else {
    PermissionUtil.requestPermissions(this, permissions, REQUEST_CODE)
}
```

### Utilisation avec Jetpack Compose

```kotlin
@Composable
fun MyScreen() {
    val context = LocalContext.current
    val activity = context as Activity
    var hasPermission by remember {
        mutableStateOf(
            PermissionUtil.isPermissionGranted(context, Manifest.permission.CAMERA)
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (!isGranted && PermissionUtil.isPermissionPermanentlyDenied(
            activity, 
            Manifest.permission.CAMERA
        )) {
            PermissionUtil.openAppSettings(context)
        }
    }

    Button(onClick = {
        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }) {
        Text("Demander permission")
    }
}
```

## 📚 API complète

### Méthodes de vérification

| Méthode | Description |
|---------|-------------|
| `isPermissionGranted(context, permission)` | Vérifie si une permission est accordée |
| `arePermissionsGranted(context, ...permissions)` | Vérifie si plusieurs permissions sont accordées |
| `isPermissionPermanentlyDenied(activity, permission)` | Vérifie si une permission est définitivement refusée |
| `shouldShowRationale(activity, permission)` | Vérifie s'il faut montrer une explication |

### Méthodes de demande

| Méthode | Description |
|---------|-------------|
| `requestPermission(activity, permission, requestCode)` | Demande une permission |
| `requestPermissions(activity, permissions, requestCode)` | Demande plusieurs permissions |
| `handlePermission(...)` | Gère le flux complet de demande |

### Méthodes de traitement

| Méthode | Description |
|---------|-------------|
| `handlePermissionResult(...)` | Traite le résultat d'une permission |
| `handleMultiplePermissionsResult(...)` | Traite le résultat de plusieurs permissions |
| `openAppSettings(context)` | Ouvre les paramètres de l'application |

### Codes de requête prédéfinis

```kotlin
PermissionUtil.RequestCodes.CAMERA          // 100
PermissionUtil.RequestCodes.LOCATION        // 101
PermissionUtil.RequestCodes.STORAGE         // 102
PermissionUtil.RequestCodes.CONTACTS        // 103
PermissionUtil.RequestCodes.PHONE           // 104
PermissionUtil.RequestCodes.SMS             // 105
PermissionUtil.RequestCodes.MICROPHONE      // 106
PermissionUtil.RequestCodes.CALENDAR        // 107
PermissionUtil.RequestCodes.SENSORS         // 108
PermissionUtil.RequestCodes.NOTIFICATION    // 109
```

## 🔄 Pattern recommandé (Flow complet)

```kotlin
fun demanderPermission() {
    val permission = Manifest.permission.READ_CONTACTS
    
    // 1. Vérifier si accordée
    if (PermissionUtil.isPermissionGranted(this, permission)) {
        utiliserPermission()
        return
    }
    
    // 2. Vérifier si définitivement refusée
    if (PermissionUtil.isPermissionPermanentlyDenied(this, permission)) {
        showDialogParametres()
        return
    }
    
    // 3. Montrer explication si nécessaire
    if (PermissionUtil.shouldShowRationale(this, permission)) {
        showDialogExplication()
    } else {
        // 4. Première demande
        PermissionUtil.requestPermission(this, permission, REQUEST_CODE)
    }
}
```

## 🎨 Bonnes pratiques

1. **Toujours expliquer** pourquoi la permission est nécessaire
2. **Gérer le refus définitif** en redirigeant vers les paramètres
3. **Ne pas demander trop de permissions** à la fois
4. **Demander au bon moment** (juste avant d'utiliser la fonctionnalité)
5. **Offrir des alternatives** si la permission est refusée

## 📝 Notes importantes

- Cette classe utilise AndroidX (androidx.core)
- Compatible avec Android 6.0+ (API 23+)
- Thread-safe et peut être utilisée depuis n'importe quel thread
- Les callbacks sont appelés sur le thread appelant

## 🔧 Personnalisation

Vous pouvez facilement étendre cette classe pour ajouter :
- Des permissions personnalisées
- Des codes de requête supplémentaires
- Des méthodes spécifiques à votre application

## 📄 Licence

Cette classe est libre d'utilisation dans tous vos projets (personnels ou commerciaux).

## 🤝 Contribution

N'hésitez pas à améliorer cette classe selon vos besoins !

---

Pour plus d'exemples, consultez le fichier `PermissionUtilExamples.kt`.

