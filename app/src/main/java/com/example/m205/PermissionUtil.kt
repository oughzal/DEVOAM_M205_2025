package com.example.m205

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Classe utilitaire pour gérer les permissions Android
 * Peut être utilisée dans n'importe quel projet Android
 */
 @Suppress("unused", "MemberVisibilityCanBePrivate")
object PermissionUtil {

    /**
     * Vérifie si une permission est accordée
     * @param context Le contexte de l'application
     * @param permission La permission à vérifier (ex: Manifest.permission.READ_CONTACTS)
     * @return true si la permission est accordée, false sinon
     */
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Vérifie si plusieurs permissions sont accordées
     * @param context Le contexte de l'application
     * @param permissions Liste des permissions à vérifier
     * @return true si toutes les permissions sont accordées, false sinon
     */
    fun arePermissionsGranted(context: Context, vararg permissions: String): Boolean {
        return permissions.all { permission ->
            isPermissionGranted(context, permission)
        }
    }

    /**
     * Vérifie si une permission a été définitivement refusée (Don't ask again)
     * @param activity L'activité depuis laquelle vérifier
     * @param permission La permission à vérifier
     * @return true si la permission a été définitivement refusée, false sinon
     */
    fun isPermissionPermanentlyDenied(activity: Activity, permission: String): Boolean {
        return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) &&
                !isPermissionGranted(activity, permission)
    }

    /**
     * Vérifie si l'utilisateur peut être invité à accorder la permission
     * (retourne true si c'est la première fois ou si l'utilisateur n'a pas coché "Don't ask again")
     * @param activity L'activité depuis laquelle vérifier
     * @param permission La permission à vérifier
     * @return true si on peut demander la permission, false si elle est définitivement refusée
     */
    fun shouldShowRationale(activity: Activity, permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

    /**
     * Demande une permission unique
     * @param activity L'activité depuis laquelle demander
     * @param permission La permission à demander
     * @param requestCode Le code de requête pour identifier la réponse dans onRequestPermissionsResult
     */
    fun requestPermission(activity: Activity, permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }

    /**
     * Demande plusieurs permissions
     * @param activity L'activité depuis laquelle demander
     * @param permissions Les permissions à demander
     * @param requestCode Le code de requête pour identifier la réponse dans onRequestPermissionsResult
     */
    fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    /**
     * Ouvre les paramètres de l'application pour permettre à l'utilisateur d'accorder manuellement les permissions
     * @param context Le contexte de l'application
     */
    fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    /**
     * Gère le flux complet de demande de permission avec gestion de tous les cas
     * @param activity L'activité depuis laquelle gérer
     * @param permission La permission à gérer
     * @param requestCode Le code de requête
     * @param onGranted Callback appelé si la permission est déjà accordée
     * @param onDenied Callback appelé si la permission est refusée (peut encore être demandée)
     * @param onPermanentlyDenied Callback appelé si la permission est définitivement refusée
     * @return true si la permission a été demandée, false si elle était déjà accordée
     */
    fun handlePermission(
        activity: Activity,
        permission: String,
        requestCode: Int,
        onGranted: (() -> Unit)? = null,
        onDenied: (() -> Unit)? = null,
        onPermanentlyDenied: (() -> Unit)? = null
    ): Boolean {
        return when {
            // Permission déjà accordée
            isPermissionGranted(activity, permission) -> {
                onGranted?.invoke()
                false
            }
            // Permission définitivement refusée
            isPermissionPermanentlyDenied(activity, permission) -> {
                onPermanentlyDenied?.invoke()
                false
            }
            // Afficher une explication si nécessaire
            shouldShowRationale(activity, permission) -> {
                onDenied?.invoke()
                requestPermission(activity, permission, requestCode)
                true
            }
            // Première demande
            else -> {
                requestPermission(activity, permission, requestCode)
                true
            }
        }
    }

    /**
     * Traite le résultat d'une demande de permission
     * À appeler dans onRequestPermissionsResult de l'activité
     * @param requestCode Le code de requête
     * @param permissions Les permissions demandées
     * @param grantResults Les résultats de la demande
     * @param expectedRequestCode Le code de requête attendu
     * @param onGranted Callback appelé si la permission est accordée
     * @param onDenied Callback appelé si la permission est refusée
     */
    fun handlePermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        expectedRequestCode: Int,
        onGranted: (() -> Unit)? = null,
        onDenied: (() -> Unit)? = null
    ) {
        if (requestCode == expectedRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onGranted?.invoke()
            } else {
                onDenied?.invoke()
            }
        }
    }

    /**
     * Traite le résultat de plusieurs permissions
     * @param requestCode Le code de requête
     * @param permissions Les permissions demandées
     * @param grantResults Les résultats de la demande
     * @param expectedRequestCode Le code de requête attendu
     * @param onAllGranted Callback appelé si toutes les permissions sont accordées
     * @param onSomeDenied Callback appelé si certaines permissions sont refusées (reçoit la liste des permissions refusées)
     */
    fun handleMultiplePermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        expectedRequestCode: Int,
        onAllGranted: (() -> Unit)? = null,
        onSomeDenied: ((List<String>) -> Unit)? = null
    ) {
        if (requestCode == expectedRequestCode) {
            val deniedPermissions = mutableListOf<String>()

            permissions.forEachIndexed { index, permission ->
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission)
                }
            }

            if (deniedPermissions.isEmpty()) {
                onAllGranted?.invoke()
            } else {
                onSomeDenied?.invoke(deniedPermissions)
            }
        }
    }

    /**
     * Constantes pour les codes de requête communs
     */
    object RequestCodes {
        const val CAMERA = 100
        const val LOCATION = 101
        const val STORAGE = 102
        const val CONTACTS = 103
        const val PHONE = 104
        const val SMS = 105
        const val MICROPHONE = 106
        const val CALENDAR = 107
        const val SENSORS = 108
        const val NOTIFICATION = 109
    }
}
