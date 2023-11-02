/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2023 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.audio

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity
import com.xperia.settings.audio.R
 
class AudioSettingsActivity : CollapsingToolbarBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragmentManager: FragmentManager = supportFragmentManager
        fragmentManager.beginTransaction()
            .replace(R.id.content_frame, AudioSettingsFragment(), TAG)
            .commit()
    }

    companion object {
        private const val TAG = "AudioSettingsActivity"
    }
}