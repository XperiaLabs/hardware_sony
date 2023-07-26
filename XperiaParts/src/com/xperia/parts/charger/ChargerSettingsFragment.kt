/*
 * Copyright (C) 2023 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.parts.charger

import android.util.Log

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import androidx.preference.*
import com.android.settingslib.widget.MainSwitchPreference
import com.android.settingslib.widget.OnMainSwitchChangeListener

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*
 
import com.xperia.parts.R
import com.xperia.parts.widgets.CustomSeekBarPreference

const val CHARGER_SETTING_ENABLE_KEY = "charger_setting_main_enable"
const val CHARGER_CHARGING_ENABLE_KEY = "device_charging_enable"
const val CHARGER_CHARGING_LIMIT_KEY = "device_charging_control"
 
class ChargerSettingsFragment : PreferenceFragmentCompat(),
    Preference.OnPreferenceChangeListener, OnMainSwitchChangeListener {

    private lateinit var chargerUtils: ChargerUtils

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.advanced_charger_settings, rootKey)
        chargerUtils = ChargerUtils(requireContext())

        findPreference<MainSwitchPreference>(CHARGER_SETTING_ENABLE_KEY)?.apply {
            isChecked = chargerUtils.mainSwitch
            addOnSwitchChangeListener(this@ChargerSettingsFragment)
        }

        findPreference<SwitchPreference>(CHARGER_CHARGING_ENABLE_KEY)?.apply {
            isChecked = chargerUtils.isChargingEnabled
            onPreferenceChangeListener = this@ChargerSettingsFragment
        }

        findPreference<CustomSeekBarPreference>(CHARGER_CHARGING_LIMIT_KEY)?.apply {
            value = chargerUtils.chargingLimit
            onPreferenceChangeListener = this@ChargerSettingsFragment
        }

        addViewPager()
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        Log.i(TAG, "${preference.key} has changed.")
        when (preference.key) {
            CHARGER_CHARGING_ENABLE_KEY -> {
                Log.i(TAG, "Charge enable: $newValue")
                chargerUtils.isChargingEnabled = newValue as Boolean
            }

            CHARGER_CHARGING_LIMIT_KEY -> chargerUtils.chargingLimit = newValue as Int
        }

        return true
    }

    override fun onSwitchChanged(switchView: Switch, isChecked: Boolean) {
        Log.i(TAG, "Main charger switch toggled to $isChecked")
        chargerUtils.isChargingEnabled = !isChecked
        chargerUtils.chargingLimit = 100
        chargerUtils.mainSwitch = true
    }

    companion object {
        private const val TAG = "ChargerSettings"
    }

    private fun addViewPager() {
    }
}
