/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 Paranoid Android
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings

import android.os.Bundle
import android.content.Intent
import android.content.pm.PackageManager
import androidx.preference.*
import androidx.core.content.pm.PackageInfoCompat
 
import com.xperia.settings.R
 
class XperiaSettingsPackage(private val fragment: PreferenceFragmentCompat) {
    private val pm = fragment.activity?.packageManager
    private val displayPackageName = "com.xperia.settings.display"
    private val displayClassName = "com.xperia.settings.display.DisplaySettingsActivity"
    private val audioPackageName = "com.xperia.settings.audio"
    private val audioClassName = "com.xperia.settings.audio.AudioSettingsActivity"
    private val batteryPackageName = "com.xperia.settings.charger"
    private val batteryClassName = "com.xperia.settings.charger.ChargerSettingsActivity"
    private val extmonPackageName = "com.sonymobile.extmonitorapp"
    private val extmonClassName = "com.sonymobile.extmonitorapp.settings.SettingsAppLauncherActivity"
    private val usbaPackageName = "jp.co.sony.mc.usbextoutaudio"
    private val usbaClassName = "jp.co.sony.mc.usbextoutaudio.AudioSettingsActivity"
    private val dsmPackageName = "com.sonymobile.dualshockmanager"
    private val dsmClassName = "com.sonymobile.dualshockmanager.Ds4SetUpActivity"

    fun setupDisplaySettings() {
        try {
            val packageInfo = pm?.getPackageInfo(displayPackageName, PackageManager.GET_ACTIVITIES)
            if (packageInfo != null && PackageInfoCompat.getLongVersionCode(packageInfo) >= 1) {
                // Package exists and has a version code greater than or equal to 1, set preference to visible
                fragment.findPreference<Preference>("display_settings")?.isVisible = true
                val intent = Intent().apply {
                    setClassName(displayPackageName, displayClassName)
                }
                fragment.findPreference<Preference>("display_settings")?.intent = intent
            } else {
                // Package does not exist or has a version code less than 1, set preference to hidden
                val category = fragment.findPreference<PreferenceCategory>("display")
                fragment.findPreference<Preference>("display_settings")?.isVisible = false
                category?.isVisible = false
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // Package does not exist, set preference to hidden
            val category = fragment.findPreference<PreferenceCategory>("display")
            fragment.findPreference<Preference>("display_settings")?.isVisible = false
            category?.isVisible = false
        }
    }

    fun setupAudioSettings() {
        try {
            val packageInfo = pm?.getPackageInfo(audioPackageName, PackageManager.GET_ACTIVITIES)
            if (packageInfo != null && PackageInfoCompat.getLongVersionCode(packageInfo) >= 1) {
                // Package exists and has a version code greater than or equal to 1, set preference to visible
                fragment.findPreference<Preference>("audio_settings")?.isVisible = true
                val intent = Intent().apply {
                    setClassName(audioPackageName, audioClassName)
                }
                fragment.findPreference<Preference>("audio_settings")?.intent = intent
            } else {
                // Package does not exist or has a version code less than 1, set preference to hidden
                val category = fragment.findPreference<PreferenceCategory>("sound")
                fragment.findPreference<Preference>("audio_settings")?.isVisible = false
                category?.isVisible = false
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // Package does not exist, set preference to hidden
            val category = fragment.findPreference<PreferenceCategory>("sound")
            fragment.findPreference<Preference>("audio_settings")?.isVisible = false
            category?.isVisible = false
        }
    }

    fun setupBatterySettings() {
        try {
            val packageInfo = pm?.getPackageInfo(batteryPackageName, PackageManager.GET_ACTIVITIES)
            if (packageInfo != null && PackageInfoCompat.getLongVersionCode(packageInfo) >= 1) {
                // Package exists and has a version code greater than or equal to 1, set preference to visible
                fragment.findPreference<Preference>("charging_settings")?.isVisible = true
                val intent = Intent().apply {
                    setClassName(batteryPackageName, batteryClassName)
                }
                fragment.findPreference<Preference>("charging_settings")?.intent = intent
            } else {
                // Package does not exist or has a version code less than 1, set preference to hidden
                val category = fragment.findPreference<PreferenceCategory>("charger")
                fragment.findPreference<Preference>("charging_settings")?.isVisible = false
                category?.isVisible = false
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // Package does not exist, set preference to hidden
            val category = fragment.findPreference<PreferenceCategory>("charger")
            fragment.findPreference<Preference>("charging_settings")?.isVisible = false
            category?.isVisible = false
        }
    }
    fun setupExtMonSettings() {
        try {
            val packageInfo = pm?.getPackageInfo(extmonPackageName, PackageManager.GET_ACTIVITIES)
            if (packageInfo != null && PackageInfoCompat.getLongVersionCode(packageInfo) >= 1) {
                // Package exists and has a version code greater than or equal to 1, set preference to visible
                fragment.findPreference<Preference>("usb_extmon_settings")?.isVisible = true
                val intent = Intent().apply {
                    setClassName(extmonPackageName, extmonClassName)
                }
                fragment.findPreference<Preference>("usb_extmon_settings")?.intent = intent
            } else {
                // Package does not exist or has a version code less than 1, set preference to hidden
                val category = fragment.findPreference<PreferenceCategory>("usb")
                fragment.findPreference<Preference>("usb_extmon_settings")?.isVisible = false
                category?.isVisible = false
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // Package does not exist, set preference to hidden
            val category = fragment.findPreference<PreferenceCategory>("usb")
            fragment.findPreference<Preference>("usb_extmon_settings")?.isVisible = false
            category?.isVisible = false
        }
    }
    fun setupUSBASettings() {
        try {
            val packageInfo = pm?.getPackageInfo(usbaPackageName, PackageManager.GET_ACTIVITIES)
            if (packageInfo != null && PackageInfoCompat.getLongVersionCode(packageInfo) >= 1) {
                // Package exists and has a version code greater than or equal to 1, set preference to visible
                fragment.findPreference<Preference>("usb_audio_settings")?.isVisible = true
                val intent = Intent().apply {
                    setClassName(usbaPackageName, usbaClassName)
                }
                fragment.findPreference<Preference>("usb_audio_settings")?.intent = intent
            } else {
                // Package does not exist or has a version code less than 1, set preference to hidden
                val category = fragment.findPreference<PreferenceCategory>("usb")
                fragment.findPreference<Preference>("usb_audio_settings")?.isVisible = false
                category?.isVisible = false
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // Package does not exist, set preference to hidden
            val category = fragment.findPreference<PreferenceCategory>("usb")
            fragment.findPreference<Preference>("usb_audio_settings")?.isVisible = false
            category?.isVisible = false
        }
    }
    fun setupDSMSettings() {
        try {
            val packageInfo = pm?.getPackageInfo(dsmPackageName, PackageManager.GET_ACTIVITIES)
            if (packageInfo != null && PackageInfoCompat.getLongVersionCode(packageInfo) >= 1) {
                // Package exists and has a version code greater than or equal to 1, set preference to visible
                fragment.findPreference<Preference>("dsm_settings")?.isVisible = true
                val intent = Intent().apply {
                    setClassName(dsmPackageName, dsmClassName)
                }
                fragment.findPreference<Preference>("dsm_settings")?.intent = intent
            } else {
                // Package does not exist or has a version code less than 1, set preference to hidden
                val category = fragment.findPreference<PreferenceCategory>("gaming")
                fragment.findPreference<Preference>("dsm_settings")?.isVisible = false
                category?.isVisible = false
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // Package does not exist, set preference to hidden
            val category = fragment.findPreference<PreferenceCategory>("gaming")
            fragment.findPreference<Preference>("dsm_settings")?.isVisible = false
            category?.isVisible = false
        }
    }
}