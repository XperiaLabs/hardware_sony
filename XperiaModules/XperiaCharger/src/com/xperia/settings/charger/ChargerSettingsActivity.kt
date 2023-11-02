/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.charger

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity
import com.xperia.settings.charger.R

class ChargerSettingsActivity : CollapsingToolbarBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.battery_care_preview)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, ChargerSettingsFragment())
                .commit()
        }
    }

    companion object {
        private const val TAG = "ChargerSettingsActivity"
    }
}