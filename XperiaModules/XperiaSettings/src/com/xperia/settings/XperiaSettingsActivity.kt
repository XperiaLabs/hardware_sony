/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 Paranoid Android
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings

import android.os.Bundle
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity
import com.android.settingslib.widget.R

class XperiaSettingsActivity : CollapsingToolbarBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, XperiaSettingsFragment(), TAG)
                .commit()
    }

    companion object {
        private const val TAG = "XperiaSettingsActivity"
    }   
}