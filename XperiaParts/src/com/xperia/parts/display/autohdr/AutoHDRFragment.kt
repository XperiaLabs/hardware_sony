/*
 * Copyright (C) 2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
*/

package com.xperia.parts.display.autohdr

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

import com.xperia.parts.R

import vendor.semc.hardware.display.V2_5.IDisplay

const val AUTO_HDR_KEY = "switchAutoHDR"

class AutoHDRFragment : PreferenceFragmentCompat() {

    private var semc25HIDLAvailable: Boolean = false

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.advanced_disp_settings, rootKey)

        semc25HIDLAvailable = IDisplay.getService() != null

        val switchAutoHDR = findPreference<SwitchPreference>(AUTO_HDR_KEY)
        switchAutoHDR?.isEnabled = semc25HIDLAvailable

        switchAutoHDR?.setOnPreferenceChangeListener { preference, newValue ->
            true
        }
    }

    override fun onResume() {
        super.onResume()

        // Disable the preference if the SEMC 2.5 HIDL is not available
        val switchAutoHDR = findPreference<SwitchPreference>(AUTO_HDR_KEY)
        switchAutoHDR?.isEnabled = semc25HIDLAvailable
    }
}