/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.display

import android.os.Bundle
 
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity
import com.android.settingslib.widget.R

class DisplaySettingsActivity : CollapsingToolbarBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, DisplaySettingsFragment(), TAG)
                .commit()
    }

    companion object {
        private const val TAG = "CreatorModeSettingsActivity"
    }
}