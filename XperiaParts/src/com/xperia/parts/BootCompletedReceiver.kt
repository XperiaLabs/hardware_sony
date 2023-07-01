/*
 * Copyright (C) 2023 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.parts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xperia.parts.display.CreatorModeUtils
import com.xperia.parts.charger.ChargerUtils

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
