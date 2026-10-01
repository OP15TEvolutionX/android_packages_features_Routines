/*
 * Copyright (C) 2025-2026 AxionOS
 * SPDX-License-Identifier: Apache-2.0
 */
package org.evolution.settings.fragments.routines

import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.os.UserHandle
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
internal fun RoutinesScaffold(
    title: String,
    onBackClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
            )
        },
        content = content,
    )
}

@Composable
internal fun RoutineSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Switch(checked = checked, onCheckedChange = onCheckedChange, modifier = modifier, enabled = enabled)
}

internal class PreferenceGroupScope {
    @Composable
    fun item(content: @Composable () -> Unit) = content()
}

@Composable
internal fun PreferenceGroup(
    title: String? = null,
    content: @Composable PreferenceGroupScope.() -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            title?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                )
            }
            PreferenceGroupScope().content()
        }
    }
}

@Composable
internal fun ClickablePreference(
    title: String,
    summary: String,
    icon: ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(summary) },
        leadingContent = { Icon(icon, contentDescription = null) },
        modifier = Modifier.clickable(enabled = enabled, onClick = onClick),
    )
}

@Composable
internal fun SecureSettingSwitch(
    settingKey: String,
    title: String,
    summary: String,
    icon: ImageVector,
    defaultValue: Boolean,
) {
    val context = LocalContext.current
    var checked by remember(settingKey) {
        mutableStateOf(readSecureSetting(context, settingKey, defaultValue))
    }
    DisposableEffect(context, settingKey) {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                checked = readSecureSetting(context, settingKey, defaultValue)
            }
        }
        context.contentResolver.registerContentObserver(
            Settings.Secure.getUriFor(settingKey),
            false,
            observer,
        )
        onDispose { context.contentResolver.unregisterContentObserver(observer) }
    }
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(summary) },
        leadingContent = { Icon(icon, contentDescription = null) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = { enabled ->
                    Settings.Secure.putIntForUser(
                        context.contentResolver,
                        settingKey,
                        if (enabled) 1 else 0,
                        UserHandle.USER_CURRENT,
                    )
                    checked = enabled
                },
            )
        },
        modifier = Modifier.clickable {
            val enabled = !checked
            Settings.Secure.putIntForUser(
                context.contentResolver,
                settingKey,
                if (enabled) 1 else 0,
                UserHandle.USER_CURRENT,
            )
            checked = enabled
        },
    )
}

private fun readSecureSetting(
    context: android.content.Context,
    key: String,
    defaultValue: Boolean,
): Boolean {
    val resolver = context.contentResolver
    val currentValue = Settings.Secure.getIntForUser(
        resolver, key, -1, UserHandle.USER_CURRENT,
    )
    if (currentValue != -1) return currentValue == 1
    if (key == ROUTINES_ENABLED_KEY) {
        val legacyValue = Settings.Secure.getIntForUser(
            resolver, LEGACY_ROUTINES_ENABLED_KEY, -1, UserHandle.USER_CURRENT,
        )
        if (legacyValue != -1) {
            Settings.Secure.putIntForUser(
                resolver, ROUTINES_ENABLED_KEY, legacyValue, UserHandle.USER_CURRENT,
            )
            return legacyValue == 1
        }
    }
    return defaultValue
}

private const val ROUTINES_ENABLED_KEY = "evo_routines_enabled"
private const val LEGACY_ROUTINES_ENABLED_KEY = "ax_routines_enabled"
