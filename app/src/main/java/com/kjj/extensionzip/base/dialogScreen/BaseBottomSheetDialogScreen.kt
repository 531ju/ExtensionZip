package com.kjj.extensionzip.base.dialogScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseBottomSheetDialogScreen(
    isShowDialog : Boolean = false,
    onDismiss : (() -> Unit) = {},
    content : @Composable BaseDialogScope.() -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val state = rememberModalBottomSheetState()

    val scope = remember {
        object : BaseDialogScope {
            override val isShowDialog: Boolean
                get() = isShowDialog

            override fun dismiss(onDismiss: () -> Unit) {
                coroutineScope.launch {
                    state.hide()
                }.invokeOnCompletion {
                    onDismiss.invoke()
                }
            }

        }
    }

    if(isShowDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    state.hide()
                }.invokeOnCompletion {
                    onDismiss.invoke()
                }
            },
            windowInsets = WindowInsets.displayCutout,
            sheetState = state,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            dragHandle = null,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.White
        ) {
            val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

            Column(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = (16.dp + bottomPadding))
                    .fillMaxWidth()
            ) {
                scope.content()
            }
        }
    }
}

@Stable
interface BaseDialogScope {
    val isShowDialog : Boolean
    fun dismiss(onDismiss : () -> Unit)
}