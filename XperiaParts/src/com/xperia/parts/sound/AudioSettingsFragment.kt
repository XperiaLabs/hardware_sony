/*
 * Copyright (C) 2023 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

 package com.xperia.parts.sound

 import android.os.Bundle
 import androidx.preference.Preference
 import androidx.preference.PreferenceFragmentCompat
 import com.xperia.parts.R
 
 class AudioSettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
 
     override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
         setPreferencesFromResource(R.xml.advanced_audio_settings, rootKey)
 
         val yourPreference: Preference? = findPreference("android:key")
         yourPreference?.onPreferenceChangeListener = this
     }
 
     override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
         return true
     }
 }
 