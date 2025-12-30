package com.example.m205

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class Permissions(val context: Activity) {

    fun isGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isPermanentlyDenied(permission: String): Boolean {
           return !context.shouldShowRequestPermissionRationale(permission) &&
                    !isGranted(permission)

    }

    fun requestPermission(permission: String, requestCode: Int) {
        context.requestPermissions(arrayOf(permission), requestCode)
    }

    fun openAppSettings() {
        val intent = android.content.Intent(
            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            android.net.Uri.fromParts("package", context.packageName, null)
        )
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun requestIfNeeded(permission: String, requestCode: Int) {
        if (!isGranted(permission)) {
            if(isPermanentlyDenied(permission)) {
                openAppSettings()
                return
            }
            requestPermission(permission, requestCode)
        }
    }
}