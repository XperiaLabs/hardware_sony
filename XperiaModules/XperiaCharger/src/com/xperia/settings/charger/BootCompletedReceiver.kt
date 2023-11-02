/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.charger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

import com.xperia.settings.charger.ChargerUtils
 
class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Starting")
        ChargerUtils(context).applyOnBoot()
    }
 
    companion object {
        private const val TAG = "XperiaParts"
    }
}