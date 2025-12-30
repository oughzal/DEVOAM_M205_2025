package com.example.m205

/**
 * EXEMPLE D'INTÉGRATION DE PERMISSIONUTIL DANS MAINACTIVITY
 *
 * Ce fichier montre comment intégrer PermissionUtil dans votre MainActivity existante.
 * Copiez et adaptez le code selon vos besoins.
 */

/*
import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactScreenWithPermission()
        }
    }
}

@Composable
fun ContactScreenWithPermission() {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val vm = remember { ContactsViewModel() }
    val scope = rememberCoroutineScope()

    // État de la permission
    var hasPermission by remember {
        mutableStateOf(
            PermissionUtil.isPermissionGranted(context, Manifest.permission.READ_CONTACTS)
        )
    }

    // États pour les dialogs
    var showRationaleDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    // Launcher pour demander la permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            // Permission accordée - charger les contacts
            scope.launch {
                vm.loadContact(context)
            }
        } else {
            // Permission refusée - vérifier si définitivement refusée
            if (PermissionUtil.isPermissionPermanentlyDenied(
                activity,
                Manifest.permission.READ_CONTACTS
            )) {
                showSettingsDialog = true
            }
        }
    }

    // Charger les contacts si la permission est déjà accordée
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            vm.loadContact(context)
        }
    }

    // Dialog d'explication
    if (showRationaleDialog) {
        AlertDialog(
            onDismissRequest = { showRationaleDialog = false },
            title = { Text("Permission nécessaire") },
            text = {
                Text("L'accès aux contacts est nécessaire pour afficher votre liste de contacts.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showRationaleDialog = false
                    permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                }) {
                    Text("Accorder")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRationaleDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    // Dialog pour rediriger vers les paramètres
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Permission refusée") },
            text = {
                Text("Vous avez refusé définitivement la permission. Veuillez l'activer dans les paramètres de l'application.")
            },
            confirmButton = {
                TextButton(onClick = {
                    showSettingsDialog = false
                    PermissionUtil.openAppSettings(context)
                }) {
                    Text("Ouvrir paramètres")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }

    // Interface utilisateur
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!hasPermission) {
            // Afficher un message et un bouton pour demander la permission
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "Permission de lecture des contacts requise",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                when {
                    // Vérifier si on doit montrer une explication
                    PermissionUtil.shouldShowRationale(
                        activity,
                        Manifest.permission.READ_CONTACTS
                    ) -> {
                        showRationaleDialog = true
                    }
                    // Première demande ou permission définitivement refusée
                    else -> {
                        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
                    }
                }
            }) {
                Text("Accorder la permission")
            }
        } else {
            // Afficher les contacts
            if (vm.contactList.isEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                Text("Aucun contact trouvé")
            } else {
                Text(
                    "Mes Contacts (${vm.contactList.size})",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn {
                    items(vm.contactList) { contact ->
                        ContactItem(contact = contact)
                    }
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = contact.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = contact.phone,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
*/

/**
 * RÉSUMÉ DE L'INTÉGRATION :
 *
 * 1. Utilisez PermissionUtil.isPermissionGranted() pour vérifier l'état initial
 * 2. Utilisez rememberLauncherForActivityResult pour demander la permission
 * 3. Dans le callback du launcher, vérifiez avec PermissionUtil.isPermissionPermanentlyDenied()
 * 4. Utilisez PermissionUtil.shouldShowRationale() pour savoir quand montrer une explication
 * 5. Utilisez PermissionUtil.openAppSettings() pour rediriger vers les paramètres
 *
 * AVANTAGES DE CETTE APPROCHE :
 * - Gestion complète de tous les cas (accordée, refusée, définitivement refusée)
 * - Interface utilisateur claire avec explications
 * - Pas de crash si la permission est refusée
 * - Redirection automatique vers les paramètres si nécessaire
 */

