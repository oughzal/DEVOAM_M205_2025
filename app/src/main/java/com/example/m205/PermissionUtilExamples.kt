package com.example.m205


/**
 * Exemples d'utilisation de PermissionUtil
 * Ce fichier peut être supprimé de votre projet, il sert uniquement de documentation
 *
 * NOTE: Ce fichier contient des exemples de code commentés pour référence.
 * Décommentez et adaptez selon vos besoins.
 */
/**
 * Exemples d'utilisation de PermissionUtil
 * Ce fichier peut être supprimé de votre projet, il sert uniquement de documentation
 *
 * NOTE: Ce fichier contient des exemples de code commentés pour référence.
 * Décommentez et adaptez selon vos besoins.
 */

@Suppress("unused", "CommentedOutCode")
object PermissionUtilExamples {

    // ==================== EXEMPLE 1 : Vérification simple ====================
    /*
    fun exemple1_VerificationSimple(context: Context) {
        if (PermissionUtil.isPermissionGranted(context, Manifest.permission.CAMERA)) {
            // La permission est accordée, utiliser la caméra
            ouvrirCamera()
        } else {
            // La permission n'est pas accordée
            Toast.makeText(context, "Permission caméra requise", Toast.LENGTH_SHORT).show()
        }
    }
    */

    // ==================== EXEMPLE 2 : Demander une permission ====================
    /*
    fun exemple2_DemanderPermission(activity: Activity) {
        PermissionUtil.handlePermission(
            activity = activity,
            permission = Manifest.permission.READ_CONTACTS,
            requestCode = PermissionUtil.RequestCodes.CONTACTS,
            onGranted = {
                // Permission déjà accordée
                chargerContacts()
            },
            onDenied = {
                // Expliquer pourquoi la permission est nécessaire
                showExplicationDialog()
            },
            onPermanentlyDenied = {
                // Rediriger vers les paramètres
                PermissionUtil.openAppSettings(activity)
            }
        )
    }
    */

    // ==================== EXEMPLE 3 : Gérer le résultat de la permission ====================
    /*
    // Dans votre Activity :
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
    */

    // ==================== EXEMPLE 4 : Demander plusieurs permissions ====================
    /*
    fun exemple4_PlusieursPermissions(activity: Activity) {
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )

        if (PermissionUtil.arePermissionsGranted(activity, *permissions)) {
            // Toutes les permissions sont accordées
            demarrerEnregistrementVideo()
        } else {
            // Demander les permissions
            PermissionUtil.requestPermissions(
                activity,
                permissions,
                PermissionUtil.RequestCodes.CAMERA
            )
        }
    }
    */

    // ==================== EXEMPLE 5 : Utilisation avec Jetpack Compose ====================
    /*
    @Composable
    fun ExempleCompose() {
        val context = LocalContext.current
        val activity = context as Activity
        var hasPermission by remember {
            mutableStateOf(
                PermissionUtil.isPermissionGranted(context, Manifest.permission.CAMERA)
            )
        }

        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            hasPermission = isGranted
            if (isGranted) {
                // Permission accordée
            } else {
                // Permission refusée
                if (PermissionUtil.isPermissionPermanentlyDenied(activity, Manifest.permission.CAMERA)) {
                    // Rediriger vers les paramètres
                    PermissionUtil.openAppSettings(context)
                }
            }
        }

        Button(onClick = {
            if (!hasPermission) {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text("Demander permission caméra")
        }
    }
    */

    // ==================== EXEMPLE 6 : Pattern complet recommandé ====================
    /*
    fun exemple6_PatternComplet(activity: Activity, context: Context) {
        val permission = Manifest.permission.READ_CONTACTS

        // Étape 1 : Vérifier si la permission est accordée
        if (PermissionUtil.isPermissionGranted(context, permission)) {
            chargerContacts()
            return
        }

        // Étape 2 : Vérifier si la permission est définitivement refusée
        if (PermissionUtil.isPermissionPermanentlyDenied(activity, permission)) {
            // Montrer un dialog avec redirection vers les paramètres
            // (Utilisez AlertDialog avec Material3 ou Dialog classique)
            PermissionUtil.openAppSettings(context)
            return
        }

        // Étape 3 : Montrer une explication si nécessaire
        if (PermissionUtil.shouldShowRationale(activity, permission)) {
            // Montrer une explication puis demander
            PermissionUtil.requestPermission(
                activity,
                permission,
                PermissionUtil.RequestCodes.CONTACTS
            )
        } else {
            // Étape 4 : Première demande
            PermissionUtil.requestPermission(
                activity,
                permission,
                PermissionUtil.RequestCodes.CONTACTS
            )
        }
    }
    */

    // ==================== EXEMPLE 7 : Exemple Compose complet avec Dialog ====================
    /*
    @Composable
    fun ExempleComposeAvecDialog() {
        val context = LocalContext.current
        val activity = context as Activity

        var hasPermission by remember {
            mutableStateOf(
                PermissionUtil.isPermissionGranted(context, Manifest.permission.READ_CONTACTS)
            )
        }
        var showRationaleDialog by remember { mutableStateOf(false) }
        var showSettingsDialog by remember { mutableStateOf(false) }

        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            hasPermission = isGranted
            if (!isGranted) {
                if (PermissionUtil.isPermissionPermanentlyDenied(activity, Manifest.permission.READ_CONTACTS)) {
                    showSettingsDialog = true
                }
            }
        }

        // Dialog d'explication
        if (showRationaleDialog) {
            AlertDialog(
                onDismissRequest = { showRationaleDialog = false },
                title = { Text("Permission nécessaire") },
                text = { Text("Cette permission est nécessaire pour accéder à vos contacts") },
                confirmButton = {
                    TextButton(onClick = {
                        showRationaleDialog = false
                        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showRationaleDialog = false }) {
                        Text("Annuler")
                    }
                }
            )
        }

        // Dialog pour les paramètres
        if (showSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showSettingsDialog = false },
                title = { Text("Permission refusée") },
                text = { Text("Veuillez activer la permission dans les paramètres") },
                confirmButton = {
                    TextButton(onClick = {
                        showSettingsDialog = false
                        PermissionUtil.openAppSettings(context)
                    }) {
                        Text("Paramètres")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSettingsDialog = false }) {
                        Text("Annuler")
                    }
                }
            )
        }

        // Bouton pour demander la permission
        Button(onClick = {
            when {
                hasPermission -> {
                    // Utiliser la fonctionnalité
                }
                PermissionUtil.shouldShowRationale(activity, Manifest.permission.READ_CONTACTS) -> {
                    showRationaleDialog = true
                }
                else -> {
                    permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                }
            }
        }) {
            Text(if (hasPermission) "Contacts disponibles" else "Demander permission")
        }
    }
    */
}

