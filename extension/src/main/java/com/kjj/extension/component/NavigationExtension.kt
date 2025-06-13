package com.kjj.extension.component

import androidx.navigation.NavController

fun NavController.isFragmentInBackStack(destinationId: Int): Boolean {
    return try {
        getBackStackEntry(destinationId)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun NavController.isFragmentInBackStack(route: String): Boolean {
    return try {
        getBackStackEntry(route)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}