/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.charger

import android.content.Context
import android.os.ServiceManager
import android.provider.Settings
import android.util.Log
import vendor.sony.charger.ICharger

class ChargerUtils(private val context: Context) {
    private val sonyChargerService: ICharger =
        ICharger.Stub.asInterface(
            ServiceManager.getService("vendor.sony.charger.ICharger/default")
        )

    var chargingLimit: Int
        get() {
            return Settings.Secure.getInt(context.contentResolver, CHARGER_LIMIT_ENABLE, 100)
        }
        set(value) {
            Settings.Secure.putInt(context.contentResolver, CHARGER_LIMIT_ENABLE, value)
            sonyChargerService.setChargingLimit(value)
        }

    var isChargingEnabled: Boolean
        get() = Settings.Secure.getInt(context.contentResolver, CHARGER_CHARGING_ENABLE, 1) > 0
        set(value) {
            sonyChargerService.setChargingEnable(value)
            Settings.Secure.putInt(
                context.contentResolver,
                CHARGER_CHARGING_ENABLE,
                if (value) 1 else 0
            )
        }

    var mainSwitch: Boolean
        get() {
            return Settings.Secure.getInt(context.contentResolver, CHARGER_MAIN_ENABLE, 0) > 0
        }
        set(value) {
            Settings.Secure.putInt(
                context.contentResolver,
                CHARGER_MAIN_ENABLE,
                if (value) 1 else 0
            )
        }

    fun isChargingLimitEnabled(): Boolean {
        return chargingLimit in 1..99
    }

    fun applyOnBoot() {
        val chargingEnabled: Int =
            Settings.Secure.getInt(context.contentResolver, CHARGER_CHARGING_ENABLE, 0)
        val chargingLimit: Int =
            Settings.Secure.getInt(context.contentResolver, CHARGER_LIMIT_ENABLE, 100)

        Log.i(TAG, "Charger: $chargingEnabled")
        this.isChargingEnabled = chargingEnabled > 0
        this.chargingLimit = chargingLimit
    }

    companion object {
        private const val TAG = "ChargerUtils"
        private const val JOB_ID = 100
        private const val DEADLINE_TIME: Long = 20 * 1000
        const val CHARGER_MAIN_ENABLE = "device_charging_main_enable"
        const val CHARGER_CHARGING_ENABLE = "device_charging_enable"
        const val CHARGER_LIMIT_ENABLE = "device_charging_limit_enable"
    }
}