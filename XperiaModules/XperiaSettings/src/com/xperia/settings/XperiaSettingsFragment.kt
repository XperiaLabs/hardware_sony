/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 Paranoid Android
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings

import android.os.Bundle
import androidx.preference.*
import android.content.pm.PackageManager

import com.xperia.settings.R
import com.xperia.settings.XperiaSettingsPackage

class XperiaSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.xperia_settings, rootKey)

        val xperiaSettingsPackage = XperiaSettingsPackage(this)
        xperiaSettingsPackage.setupDisplaySettings()
        xperiaSettingsPackage.setupAudioSettings()
        xperiaSettingsPackage.setupBatterySettings()
        xperiaSettingsPackage.setupExtMonSettings()
        xperiaSettingsPackage.setupUSBASettings()
        xperiaSettingsPackage.setupDSMSettings()
    }
}