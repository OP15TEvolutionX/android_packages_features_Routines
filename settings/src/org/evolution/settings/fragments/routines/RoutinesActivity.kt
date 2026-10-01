/*
 * Copyright (C) 2026 Evolution X
 * SPDX-License-Identifier: Apache-2.0
 */
package org.evolution.settings.fragments.routines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.android.settings.R
import com.android.settingslib.spa.framework.theme.SettingsTheme
import com.android.settingslib.widget.SettingsThemeHelper

/** Full-screen Settings entry point; avoids the SubSettings fragment container. */
class RoutinesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(
            if (SettingsThemeHelper.isExpressiveTheme(this)) {
                R.style.Theme_Settings_Expressive_NoActionBar
            } else {
                R.style.Theme_Settings_NoActionBar
            }
        )
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SettingsTheme {
                RoutinesScreen(onBackClick = ::finish)
            }
        }
    }
}
