/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 The Android Open Source Project
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

package com.xperia.settings.charger

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class ChargerTileService : TileService() {

    private val chargerUtil: ChargerUtils = ChargerUtils(this)

    override fun onStartListening() {
        super.onStartListening()

        // Check settings when listening
        val state = chargerUtil.isChargingEnabled
        if (state)
            qsTile.state = Tile.STATE_INACTIVE
        else
            qsTile.state = Tile.STATE_ACTIVE

        qsTile.updateTile()
    }

    override fun onClick() {
        super.onClick()

        if (qsTile.state == Tile.STATE_ACTIVE) {
            // enable --> disable
            chargerUtil.isChargingEnabled = true
            qsTile.state = Tile.STATE_INACTIVE
        } else {
            // disable -> enable
            chargerUtil.isChargingEnabled = false
            qsTile.state = Tile.STATE_ACTIVE
        }

        qsTile.updateTile()
    }
}