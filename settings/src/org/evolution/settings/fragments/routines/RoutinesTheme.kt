/*
 * Copyright (C) 2026 Evolution X
 * SPDX-License-Identifier: Apache-2.0
 */
package org.evolution.settings.fragments.routines

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.android.settings.R
import com.android.settingslib.spa.framework.theme.SettingsTheme

/** Use the same background and preference colors as XML pages in Evolver. */
@Composable
internal fun RoutinesTheme(content: @Composable () -> Unit) {
    SettingsTheme {
        val background = colorResource(R.color.evolution_routines_background)
        val preference = colorResource(R.color.evolution_routines_preference_background)
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(
                background = background,
                surface = background,
                surfaceBright = preference,
            ),
            content = content,
        )
    }
}
