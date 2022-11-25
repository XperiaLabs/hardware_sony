/*
 * Copyright (C) 2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.settings.device.charger

import android.util.Log

import android.os.Bundle
import android.widget.Switch
import androidx.preference.*
import com.android.settingslib.widget.MainSwitchPreference
import com.android.settingslib.widget.OnMainSwitchChangeListener

import org.lineageos.settings.device.R
import org.lineageos.settings.device.widgets.CustomSeekBarPreference

const val CHARGER_SETTING_ENABLE_KEY = "charger_setting_main_enable"
const val CHARGER_CHARGING_ENABLE_KEY = "device_charging_enable"
const val CHARGER_CHARGING_LIMIT_KEY = "device_charging_control"

class ChargerSettingsFragment : PreferenceFragment(), Preference.OnPreferenceChangeListener,
    OnMainSwitchChangeListener {
    private lateinit var chargerUtils: ChargerUtils
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.advanced_charger_settings)
        chargerUtils = ChargerUtils(context)

        val chargerSettingPreference =
            findPreference<MainSwitchPreference>(CHARGER_SETTING_ENABLE_KEY)!!
        chargerSettingPreference.isChecked = chargerUtils.mainSwitch
        chargerSettingPreference.addOnSwitchChangeListener(this)

        val chargerChargingPreference =
            findPreference<SwitchPreference>(CHARGER_CHARGING_ENABLE_KEY)!!
        chargerChargingPreference.isChecked = chargerUtils.isChargingEnabled
        chargerChargingPreference.onPreferenceChangeListener = this

        val chargerChargingLimitPreference =
            findPreference<CustomSeekBarPreference>(CHARGER_CHARGING_LIMIT_KEY)!!
        chargerChargingLimitPreference.value = chargerUtils.chargingLimit
        chargerChargingLimitPreference.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        Log.i(TAG, preference.key + " has changed.")
        when (preference.key) {
            CHARGER_CHARGING_ENABLE_KEY -> {
                Log.i(TAG, "Charge enable: " + newValue as Boolean)
                chargerUtils.isChargingEnabled = newValue
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
}
