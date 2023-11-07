/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 Paranoid Android
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.audio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
        }
    }
}