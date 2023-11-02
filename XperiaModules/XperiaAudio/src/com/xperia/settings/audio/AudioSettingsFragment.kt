/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2023 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.audio

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.xperia.settings.audio.R

class AudioSettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.audio_settings, rootKey)
 
        val yourPreference: Preference? = findPreference("android:key")
        yourPreference?.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        return true
    }
}