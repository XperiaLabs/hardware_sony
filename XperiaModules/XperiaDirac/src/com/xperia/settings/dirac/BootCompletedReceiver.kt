/*
 * Copyright (C) 2023 XperiaLabs Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.dirac

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xperia.settings.dirac.DiracUtils

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val millis = 1 * 60 * 1000  // 1min
        try {
            Thread.sleep(millis.toLong())
            context?.let { DiracUtils(it).onBootCompleted() }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}