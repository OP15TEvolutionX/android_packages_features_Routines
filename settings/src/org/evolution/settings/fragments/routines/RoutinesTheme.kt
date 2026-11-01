/*
 * Copyright (C) 2026 Evolution X
 * SPDX-License-Identifier: Apache-2.0
 */
package org.evolution.settings.fragments.routines

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.evolution.settings.extrafeatures.ui.ExtraFeaturesTheme

/** Scaffolds fill with background, so it stays transparent like the other Extra screens. */
@Composable
internal fun RoutinesTheme(content: @Composable () -> Unit) {
    ExtraFeaturesTheme {
        MaterialTheme(
            colorScheme = MaterialTheme.colorScheme.copy(background = Color.Transparent),
            content = content,
        )
    }
}
