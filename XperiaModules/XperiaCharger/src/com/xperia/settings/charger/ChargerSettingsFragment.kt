/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.charger

import android.util.Log

import android.content.SharedPreferences
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
 
import com.xperia.settings.charger.R
import com.xperia.settings.charger.widgets.CustomSeekBarPreference

const val CHARGER_SETTING_ENABLE_KEY = "charger_setting_main_enable"
const val CHARGER_CHARGING_ENABLE_KEY = "device_charging_enable"
const val CHARGER_CHARGING_ENABLE_BACKUP = "device_charging_enable_backup"
const val CHARGER_CHARGING_LIMIT_KEY = "device_charging_control"
const val CHARGER_CHARGING_LIMIT_BACKUP = "device_charging_control_backup"

class ChargerSettingsFragment : PreferenceFragmentCompat(),
    Preference.OnPreferenceChangeListener, OnMainSwitchChangeListener {

    private lateinit var chargerUtils: ChargerUtils

    private var mSwitch: MainSwitchPreference? = null
    private var mChargingSwitch: SwitchPreference? = null
    private var mChargingLimit: CustomSeekBarPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.charger_settings, rootKey)
        chargerUtils = ChargerUtils(requireContext())

        mSwitch = findPreference<MainSwitchPreference>(CHARGER_SETTING_ENABLE_KEY)?.apply {
            isChecked = chargerUtils.mainSwitch
            addOnSwitchChangeListener(this@ChargerSettingsFragment)
        }

        mChargingSwitch = findPreference<SwitchPreference>(CHARGER_CHARGING_ENABLE_KEY)?.apply {
            isChecked = chargerUtils.isChargingEnabled
            onPreferenceChangeListener = this@ChargerSettingsFragment
        }

        mChargingLimit = findPreference<CustomSeekBarPreference>(CHARGER_CHARGING_LIMIT_KEY)?.apply {
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
        mSwitch!!.setChecked(isChecked)

        val sharedPreferences: SharedPreferences = 
                PreferenceManager.getDefaultSharedPreferences(requireContext());

        if (!isChecked) {
            val prefChargingEnabled = sharedPreferences.getBoolean(CHARGER_CHARGING_ENABLE_KEY, true)
            val prefChargingLimit = sharedPreferences.getInt(CHARGER_CHARGING_LIMIT_KEY, 100)
            sharedPreferences.edit().putBoolean(CHARGER_CHARGING_ENABLE_BACKUP, prefChargingEnabled).apply()
            sharedPreferences.edit().putInt(CHARGER_CHARGING_LIMIT_BACKUP, prefChargingLimit).apply()

            chargerUtils.isChargingEnabled = true
            mChargingSwitch!!.setChecked(true)
            chargerUtils.chargingLimit = 100
            mChargingLimit!!.setValue(100, true)
        } else {
            val prefChargingEnabled = sharedPreferences.getBoolean(CHARGER_CHARGING_ENABLE_BACKUP, true)
            val prefChargingLimit = sharedPreferences.getInt(CHARGER_CHARGING_LIMIT_BACKUP, 100)

            chargerUtils.isChargingEnabled = prefChargingEnabled
            mChargingSwitch!!.setChecked(prefChargingEnabled)
            chargerUtils.chargingLimit = prefChargingLimit
            mChargingLimit!!.setValue(prefChargingLimit, true)
        }

        Log.i(TAG, "Main charger switch toggled to $isChecked")
        chargerUtils.mainSwitch = isChecked
    }

    companion object {
        private const val TAG = "ChargerSettings"
    }

    private fun addViewPager() {
    }
}