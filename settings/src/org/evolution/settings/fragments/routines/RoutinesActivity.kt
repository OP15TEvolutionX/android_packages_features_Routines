/*
 * Copyright (C) 2026 Evolution X
 * SPDX-License-Identifier: Apache-2.0
 */
package org.evolution.settings.fragments.routines

import android.os.Bundle
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList
import java.util.Locale
import android.view.MenuItem
import androidx.compose.ui.platform.ComposeView
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity
import com.android.settings.R
import com.android.settingslib.widget.SettingsThemeHelper

/** Full-screen Settings entry point; avoids the SubSettings fragment container. */
class RoutinesActivity : CollapsingToolbarBaseActivity() {
    override fun attachBaseContext(newBase: Context) {
        val language = Resources.getSystem().configuration.locales[0].language
        val locale = if (language == "ru") Locale.forLanguageTag("ru") else Locale.ENGLISH
        val configuration = Configuration(newBase.resources.configuration).apply {
            setLocales(LocaleList(locale))
        }
        super.attachBaseContext(newBase.createConfigurationContext(configuration))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(
            if (SettingsThemeHelper.isExpressiveTheme(this)) {
                R.style.Theme_SubSettings_Expressive
            } else {
                R.style.Theme_SubSettings
            }
        )
        super.onCreate(savedInstanceState)
        window.isStatusBarContrastEnforced = false
        window.isNavigationBarContrastEnforced = false
        actionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(ComposeView(this).apply {
            setContent {
                RoutinesTheme {
                    RoutinesScreen(onBackClick = ::finish)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    @Deprecated("Use onBackPressedDispatcher")
    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
    }
}
