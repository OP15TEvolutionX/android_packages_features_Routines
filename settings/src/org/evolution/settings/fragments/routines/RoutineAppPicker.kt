/*
 * SPDX-FileCopyrightText: Evolution X
 * SPDX-License-Identifier: Apache-2.0
 */
package org.evolution.settings.fragments.routines

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
internal fun rememberRoutineAppList(query: String): List<Pair<String, String>> {
    val context = LocalContext.current
    val apps = remember(context) {
        val packageManager = context.packageManager
        val launcherIntent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        packageManager.queryIntentActivities(launcherIntent, 0)
            .map { info ->
                val packageName = info.activityInfo.packageName
                packageName to info.loadLabel(packageManager).toString()
            }
            .distinctBy { it.first }
            .sortedBy { it.second.lowercase() }
    }
    return remember(apps, query) {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isEmpty()) apps
        else apps.filter { (packageName, label) ->
            label.contains(normalizedQuery, ignoreCase = true) ||
                packageName.contains(normalizedQuery, ignoreCase = true)
        }
    }
}
