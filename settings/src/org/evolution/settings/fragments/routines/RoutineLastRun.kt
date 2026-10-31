/*
 * Copyright (C) 2026 Evolution X
 * SPDX-License-Identifier: Apache-2.0
 */
package org.evolution.settings.fragments.routines

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.android.settings.R

@Composable
internal fun describeLastRun(timestamp: Long): String {
    val minutes = ((System.currentTimeMillis() - timestamp).coerceAtLeast(0) / 60_000)
    if (minutes == 0L) return stringResource(R.string.routines_last_run_now)
    val (resource, count) = when {
        minutes >= 1440 -> R.plurals.routines_last_run_days to minutes / 1440
        minutes >= 60 -> R.plurals.routines_last_run_hours to minutes / 60
        else -> R.plurals.routines_last_run_minutes to minutes
    }
    val quantity = count.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
    return pluralStringResource(resource, quantity, quantity)
}
