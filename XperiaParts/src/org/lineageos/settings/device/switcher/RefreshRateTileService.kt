/*
 * Copyright (C) 2022 - LineageOS Project
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

package org.lineageos.settings.device.switcher

import android.content.Context
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.view.Display
import java.lang.Math.max
import java.util.*

class RefreshRateTileService : TileService() {
    private val KEY_MIN_REFRESH_RATE = "min_refresh_rate"
    private val KEY_PEAK_REFRESH_RATE = "peak_refresh_rate"

    private lateinit var context: Context
    private lateinit var tile: Tile

    private val availableRates = ArrayList<Int>()
    private var activeRateMin = 0
    private var activeRateMax = 0

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        val mode: Display.Mode = context.display.mode
        val modes: Array<Display.Mode> = context.display.supportedModes
        for (m in modes) {
            val rate = Math.round(m.refreshRate)
            if (m.physicalWidth == mode.physicalWidth && m.physicalHeight == mode.physicalHeight) {
                availableRates.add(rate)
            }
        }
        syncFromSettings()
    }

    private fun getSettingOf(key: String): Int {
        val rate = Settings.System.getFloat(context.contentResolver, key, 60f)
        val active = max(availableRates.indexOf(Math.round(rate)), 0)
        return maxOf(active, 0)
    }

    private fun syncFromSettings() {
        activeRateMin = getSettingOf(KEY_MIN_REFRESH_RATE)
        activeRateMax = getSettingOf(KEY_PEAK_REFRESH_RATE)
    }

    private fun cycleRefreshRate() {
        if (activeRateMin < availableRates.size - 1) {
            activeRateMin++
        } else {
            activeRateMin = 0
        }

        val rate = availableRates[activeRateMin].toFloat()
        Settings.System.putFloat(context.contentResolver, KEY_MIN_REFRESH_RATE, rate)
        Settings.System.putFloat(context.contentResolver, KEY_PEAK_REFRESH_RATE, rate)
    }

    private fun updateTileView() {
        val displayText: String
        val min = availableRates[activeRateMin]
        val max = availableRates[activeRateMax]

        displayText = if (min == max) "%d Hz".format(Locale.US, min) else "%d - %d Hz".format(Locale.US, min, max)
        tile.contentDescription = displayText
        tile.subtitle = displayText
        tile.state = if (min == max) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        tile.updateTile()
    }

    override fun onStartListening() {
        super.onStartListening()
        tile = qsTile
        syncFromSettings()
        updateTileView()
    }

    override fun onClick() {
        super.onClick()
        cycleRefreshRate()
        syncFromSettings()
        updateTileView()
    }
}