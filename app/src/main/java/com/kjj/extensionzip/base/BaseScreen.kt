package com.kjj.extensionzip.base

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.kjj.extensionzip.ui.theme.ExtensionZipTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    title : String? = null,
    onBackPress : (() -> Unit)? = null,
    isShowProgress : Boolean = false,
    content : @Composable BaseScreenScope.(innerPadding : PaddingValues) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val dismissSnackbarState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        confirmValueChange = { value ->
            if(value != SwipeToDismissBoxValue.Settled) {
                snackbarHostState.currentSnackbarData?.dismiss()
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
                get() = snackbarHostState
            override val coroutineScope: CoroutineScope
                get() = coroutineScope
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
//            if(title != null) {
//                CarelevoToolbar(
//                    title = title,
//                    onBackPress = onBackPress
//                )
//            }
        },
        snackbarHost = {
            SwipeToDismissBox(
                state = dismissSnackbarState,
                backgroundContent = {},
                content = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            )
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

fun BaseScreenScope.showMessage(msg : String) {
    coroutineScope.launch {
        snackbarHost.showSnackbar(
            message = msg,
            duration = SnackbarDuration.Short
        )
    }
}

@Stable
interface BaseScreenScope {

    val snackbarHost : SnackbarHostState
    val coroutineScope : CoroutineScope
}

@Preview
@Composable
fun PreviewBaseScreen() {
    ExtensionZipTheme {
        BaseScreen(
            title = "제목 ",
            onBackPress = {},
        ) {

        }
    }
}