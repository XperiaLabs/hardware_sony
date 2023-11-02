/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2023 The LineageOS Project
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

package com.xperia.settings.switcher

import android.content.Context
import android.content.pm.PackageManager
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.view.Display
import java.util.ArrayList
import java.util.Locale

import com.xperia.settings.switcher.R

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
            val rate = m.refreshRate.toInt()
            if (m.physicalWidth == mode.physicalWidth && m.physicalHeight == mode.physicalHeight) {
                availableRates.add(rate)
            }
        }
        syncFromSettings()
    }

    private fun getSettingOf(key: String): Int {
        val defaultRate = resources.getInteger(R.integer.config_defaultRefreshRate)
        val rate = Settings.System.getInt(context.contentResolver, key, defaultRate)
        val active = availableRates.indexOf(rate)
        return active.coerceAtLeast(0)
    }

    private fun syncFromSettings() {
        activeRateMin = getSettingOf(KEY_MIN_REFRESH_RATE)
        activeRateMax = getSettingOf(KEY_PEAK_REFRESH_RATE)
    }

    private fun cycleRefreshRate() {
        activeRateMin = (activeRateMin + 1) % availableRates.size

        val rate = availableRates[activeRateMin]
        Settings.System.putInt(context.contentResolver, KEY_MIN_REFRESH_RATE, rate)
        Settings.System.putInt(context.contentResolver, KEY_PEAK_REFRESH_RATE, rate)
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
        checkRefreshRateAvailable()
    }

    private fun checkRefreshRateAvailable() {
        val hasDefaultRefreshRate = resources.getInteger(R.integer.config_defaultRefreshRate)
        val hasDefaultPeakRefreshRate = resources.getInteger(R.integer.config_defaultPeakRefreshRate)

        if (hasDefaultRefreshRate != -1 || hasDefaultPeakRefreshRate != 0) {
            stopSelf()
            tile.state = Tile.STATE_UNAVAILABLE
            tile.subtitle = "Not Supported"
            tile.updateTile()
        }
    }

    override fun onClick() {
        super.onClick()
        if (tile.state != Tile.STATE_UNAVAILABLE) {
            cycleRefreshRate()
            syncFromSettings()
            updateTileView()
        }
    }
}