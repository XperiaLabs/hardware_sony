/*
 * Copyright (C) 2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.device

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import org.lineageos.settings.device.display.CreatorModeUtils
import org.lineageos.settings.device.charger.ChargerUtils

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Starting")
        CreatorModeUtils(context).initialize()
        ChargerUtils(context).applyOnBoot()
    }

    companion object {
        private const val TAG = "XperiaParts"
    }
}
