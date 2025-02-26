package com.mucheng.mucute.client.router.main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Slider
import com.mucheng.mucute.client.util.LocalSnackbarHostState
import com.mucheng.mucute.client.util.SnackbarHostStateScope
import androidx.core.content.edit
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mucheng.mucute.client.R
import com.mucheng.mucute.client.util.LocalSnackbarHostState
import com.mucheng.mucute.client.util.SnackbarHostStateScope
import com.mucheng.mucute.client.overlay.OverlayManager

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Opacity
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BasicAlertDialog
import androidx.core.content.edit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPageContent() {
    SnackbarHostStateScope {
        val context = LocalContext.current
        val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        var showOpacityDialog by remember { mutableStateOf(false) }
        var overlayOpacity by remember {
            mutableStateOf(sharedPreferences.getFloat("overlay_opacity", 1f))
        }
        var shortcutOpacity by remember {
            mutableStateOf(sharedPreferences.getFloat("shortcut_opacity", 1f))
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.settings)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        titleContentColor = contentColorFor(MaterialTheme.colorScheme.surfaceContainer)
                    )
                )
            },
            bottomBar = {
                SnackbarHost(
                    LocalSnackbarHostState.current,
                    modifier = Modifier.animateContentSize()
                )
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) { padding ->
            Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Opacity Settings Card
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    onClick = { showOpacityDialog = true }
                ) {
                    Row(
                        Modifier.padding(15.dp),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Rounded.Opacity,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Column(Modifier.weight(1f)) {
                            Text(
                                stringResource(R.string.overlay_opacity_settings),
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                stringResource(R.string.overlay_opacity_description),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Icon(
                            Icons.Rounded.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .scale(0.8f)
                                .size(20.dp)
                        )
                    }
                }
            }
        }

        if (showOpacityDialog) {
            BasicAlertDialog(
                onDismissRequest = { showOpacityDialog = false },
                modifier = Modifier.padding(vertical = 24.dp)
            ) {
                Surface(
                    shape = AlertDialogDefaults.shape,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Column(
                        Modifier
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            stringResource(R.string.overlay_opacity_settings),
                            modifier = Modifier.align(Alignment.Start),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(24.dp)
                        ) {
                            Column {
                                Text(
                                    stringResource(R.string.overlay_opacity),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Slider(
                                    value = overlayOpacity,
                                    onValueChange = { value ->
                                        overlayOpacity = value
                                        sharedPreferences.edit {
                                            putFloat("overlay_opacity", value)
                                        }
                                        OverlayManager.currentContext?.let {
                                            OverlayManager.updateOverlayOpacity(value)
                                        }
                                    },
                                    valueRange = 0.0f..1f
                                )
                            }

                            Column {
                                Text(
                                    stringResource(R.string.shortcut_opacity),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                Slider(
                                    value = shortcutOpacity,
                                    onValueChange = { value ->
                                        shortcutOpacity = value
                                        sharedPreferences.edit {
                                            putFloat("shortcut_opacity", value)
                                        }
                                        OverlayManager.currentContext?.let {
                                            OverlayManager.updateShortcutOpacity(value)
                                        }
                                    },
                                    valueRange = 0.0f..1f
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}