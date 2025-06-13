package com.kjj.extension

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.core.content.ContextCompat

fun Context.getScreenWidthDP(): Float {
    val displayMetrics = Resources.getSystem().displayMetrics

    return displayMetrics.widthPixels / displayMetrics.density
}

fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for(service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if(serviceClass.name == service.service.className) {
            return true
        }
    }
    return false
}

fun isAppInForeground(context: Context): Boolean {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val packageName = context.applicationContext.packageName
    val appProcesses = manager.runningAppProcesses?: return false

    for(process in appProcesses) {
        if(process.importance==ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && process.processName==packageName) {
            return true
        }
    }
    return false
}

fun Context.hasPermission(permissionType: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permissionType) ==
            PackageManager.PERMISSION_GRANTED
}