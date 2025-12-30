# PermissionUtil - Guide de Démarrage Rapide

## 🚀 Configuration en 3 minutes

### Étape 1 : Copier le fichier
Copiez `PermissionUtil.kt` dans votre package principal.

### Étape 2 : Ajouter la permission dans AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.CAMERA" />
```

### Étape 3 : Utiliser dans votre code

## 📱 Exemple Minimal (Jetpack Compose)

```kotlin
@Composable
fun MaScreen() {
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
        if (!isGranted) {
            if (PermissionUtil.isPermissionPermanentlyDenied(
                activity, 
                Manifest.permission.CAMERA
            )) {
                // Rediriger vers paramètres
                PermissionUtil.openAppSettings(context)
            }
        }
    }

    Button(onClick = {
        if (hasPermission) {
            // Utiliser la caméra
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }) {
        Text("Ouvrir Caméra")
    }
}
```

## 📱 Exemple avec Activity Classique

```kotlin
class MainActivity : AppCompatActivity() {
    
    companion object {
        private const val REQUEST_CAMERA = 100
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Vérifier et demander la permission
        checkCameraPermission()
    }
    
    private fun checkCameraPermission() {
        PermissionUtil.handlePermission(
            activity = this,
            permission = Manifest.permission.CAMERA,
            requestCode = REQUEST_CAMERA,
            onGranted = {
                // Permission déjà accordée
                openCamera()
            },
            onDenied = {
                // Montrer une explication
                Toast.makeText(
                    this, 
                    "Permission caméra nécessaire", 
                    Toast.LENGTH_SHORT
                ).show()
            },
            onPermanentlyDenied = {
                // Rediriger vers les paramètres
                showSettingsDialog()
            }
        )
    }
    
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
            expectedRequestCode = REQUEST_CAMERA,
            onGranted = {
                openCamera()
            },
            onDenied = {
                Toast.makeText(
                    this, 
                    "Permission refusée", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
    
    private fun openCamera() {
        // Votre code pour ouvrir la caméra
    }
    
    private fun showSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission requise")
            .setMessage("Veuillez activer la permission caméra dans les paramètres")
            .setPositiveButton("Paramètres") { _, _ ->
                PermissionUtil.openAppSettings(this)
            }
            .setNegativeButton("Annuler", null)
            .show()
    }
}
```

## 🎯 Cas d'utilisation courants

### 1. Vérifier simplement une permission
```kotlin
if (PermissionUtil.isPermissionGranted(context, Manifest.permission.CAMERA)) {
    // Utiliser la fonctionnalité
}
```

### 2. Vérifier plusieurs permissions
```kotlin
val permissions = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.RECORD_AUDIO
)

if (PermissionUtil.arePermissionsGranted(context, *permissions)) {
    // Toutes les permissions sont accordées
}
```

### 3. Ouvrir les paramètres de l'app
```kotlin
PermissionUtil.openAppSettings(context)
```

### 4. Demander une permission
```kotlin
PermissionUtil.requestPermission(
    activity,
    Manifest.permission.CAMERA,
    REQUEST_CODE
)
```

## 📋 Permissions courantes

```kotlin
// Caméra
Manifest.permission.CAMERA

// Contacts
Manifest.permission.READ_CONTACTS
Manifest.permission.WRITE_CONTACTS

// Localisation
Manifest.permission.ACCESS_FINE_LOCATION
Manifest.permission.ACCESS_COARSE_LOCATION

// Stockage
Manifest.permission.READ_EXTERNAL_STORAGE
Manifest.permission.WRITE_EXTERNAL_STORAGE

// Téléphone
Manifest.permission.CALL_PHONE
Manifest.permission.READ_PHONE_STATE

// SMS
Manifest.permission.SEND_SMS
Manifest.permission.READ_SMS

// Microphone
Manifest.permission.RECORD_AUDIO

// Calendrier
Manifest.permission.READ_CALENDAR
Manifest.permission.WRITE_CALENDAR
```

## 🔢 Codes de requête prédéfinis

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

## ⚠️ Points importants

1. **Ajouter la permission dans AndroidManifest.xml** avant de l'utiliser
2. **Expliquer** pourquoi vous avez besoin de la permission
3. **Gérer le refus définitif** en redirigeant vers les paramètres
4. **Demander au bon moment** (juste avant d'utiliser la fonctionnalité)

## 🆘 Dépannage

### La permission ne se demande pas
- Vérifiez que la permission est dans AndroidManifest.xml
- Vérifiez que vous utilisez une Activity, pas un Context simple
- Vérifiez la version Android (permissions runtime à partir d'Android 6.0)

### La permission est toujours refusée
- L'utilisateur a peut-être coché "Ne plus demander"
- Utilisez `isPermissionPermanentlyDenied()` pour détecter ce cas
- Redirigez l'utilisateur vers les paramètres avec `openAppSettings()`

### Crash lors de la demande
- Assurez-vous de passer une Activity, pas un Context
- Vérifiez que l'activité n'est pas en cours de destruction

## 📚 Ressources

- Documentation complète: voir `PermissionUtil_README.md`
- Exemples de code: voir `PermissionUtilExamples.kt`
- Code source: voir `PermissionUtil.kt`

## 💡 Astuce

Pour des projets Compose modernes, utilisez directement `rememberLauncherForActivityResult` avec `PermissionUtil` pour vérifier les états (accordée/refusée/définitivement refusée).

