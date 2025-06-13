package com.kjj.base_compose.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    title : String? = null,
    onBackPress : (() -> Unit)? = null,
    isShowProgress : Boolean = false,
    content : @Composable BaseScreenScope.(innerPadding : PaddingValues) -> Unit
) {
    val snacbarHostState = remember { SnackbarHostState() }
    val dismissSnackbarState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        confirmValueChange = { value ->
            if(value != SwipeToDismissBoxValue.Settled) {
                snacbarHostState.currentSnackbarData?.dismiss()
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(dismissSnackbarState.currentValue) {
        if(dismissSnackbarState.currentValue != SwipeToDismissBoxValue.Settled) {
            dismissSnackbarState.reset()
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val scope = remember {
        object : BaseScreenScope {
            override val snackbarHost: SnackbarHostState
                get() = snacbarHostState
            override val coroutineScope: CoroutineScope
                get() = coroutineScope
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White,
        topBar = {
            // todo if title != null show default toolbar
        },
        snackbarHost = {
            SwipeToDismissBox(
                state = dismissSnackbarState,
                backgroundContent = {}
            ) {
                SnackbarHost(hostState = snacbarHostState)
            }
        }
    ) { innerPadding ->

        scope.content(innerPadding)

        if(isShowProgress) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0f)
                CircularProgressIndicator(
                    modifier = Modifier.size(56.dp),
                    color = Color.DarkGray,
                    trackColor = Color.Gray
                )
            }
        }
    }
}

@Stable
interface BaseScreenScope {
    val snackbarHost : SnackbarHostState
    val coroutineScope : CoroutineScope
}