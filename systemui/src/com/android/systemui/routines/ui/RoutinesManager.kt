/*
 * Copyright (C) 2025-2026 AxionOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.routines.ui

import android.content.Context
import com.android.systemui.CoreStartable
import com.android.systemui.dagger.SysUISingleton
import com.android.systemui.dagger.qualifiers.Application
import com.android.systemui.dagger.qualifiers.Main
import com.android.systemui.res.R
import com.android.systemui.routines.data.RoutinesRepository
import com.android.systemui.routines.domain.RoutinesInteractor
import com.android.systemui.routines.domain.RoutinesSettings
import com.android.systemui.statusbar.phone.ui.StatusBarIconController
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import com.android.app.tracing.coroutines.launchTraced as launch

@SysUISingleton
class RoutinesManager @Inject constructor(
    @Application private val context: Context,
    @Application private val scope: CoroutineScope,
    @Main private val mainDispatcher: CoroutineDispatcher,
    private val interactor: RoutinesInteractor,
    private val repository: RoutinesRepository,
    private val settings: RoutinesSettings,
    private val iconController: StatusBarIconController,
) : CoreStartable {

    private val slotRoutines =
        context.getString(R.string.status_bar_routines)

    override fun start() {
        interactor.init()
        scope.launch(context = mainDispatcher) {
            // Evolution X keeps routine execution silent, without a status icon.
            iconController.setIconVisibility(slotRoutines, false)
        }
    }
}
