/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 Paranoid Android
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
 
class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Starting")
    }
 
    companion object {
        private const val TAG = "XperiaSettings"
    }
}
