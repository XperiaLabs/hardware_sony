/*
 * Copyright (C) 2023 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.parts

import android.os.Bundle
import androidx.preference.*

import com.xperia.parts.R

class XperiaSettingsFragment : PreferenceFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences_xperia_menu)
    }
}
