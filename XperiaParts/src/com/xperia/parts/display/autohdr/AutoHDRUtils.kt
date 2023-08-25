/*
 * Copyright (C) 2022 The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.parts.display.autohdr

import android.content.Context
import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.media.MediaFormat
import android.media.MediaFeature
import vendor.semc.hardware.display.V2_5.ColorModeConfig
import vendor.semc.hardware.display.V2_5.IDisplay

class AutoHDRUtils(private val context: Context) {
    private val semcDisplayService: IDisplay =
            IDisplay.getService() ?: throw Exception("SEMC Display HIDL 2.5 not found")

    fun initialize() {
        semcDisplayService.setup()
    }

    fun detectHdrVideo(): Boolean {
        val codecList = MediaCodecList(MediaCodecList.REGULAR_CODECS)
        val codecs = codecList.codecInfos.filter { codecInfo ->
            codecInfo.isEncoder == false && codecInfo.supportedTypes.contains("video/avc")
        }

        val hdrCodecs = codecs.filter { codecInfo ->
            val formats = codecInfo.getCapabilitiesForType("video/avc").colorFormats
            formats.any { format ->
                0x7F000001 == format || 0x7F000002 == format || 0x7F000003 == format // HDR10, HDR10_PLUS, HLG
            }
        }

        return hdrCodecs.isNotEmpty()
    }

    class AutoHDRUtils(private val semcDisplayService: DisplayService) {
        fun enableAutoHDR() {
            val config = semcDisplayService.get_color_config(0, 0)

            semcDisplayService.set_hdr_tm_mode(true)

            val newConfig = ColorModeConfig().apply {
                state_id = config.state_id
                mode_id = config.mode_id
                brightness = config.brightness
                contrast = config.contrast
                saturation = config.saturation
                blue_high = 1.0f
            }

            semcDisplayService.set_color_mode(newConfig.toConfigBits())
        }

        fun disableAutoHDR() {
            semcDisplayService.set_hdr_tm_mode(false)
    
            semcDisplayService.set_color_mode(ColorModeConfig().toConfigBits())
        }
    }

    class ColorModeConfig {
        var state_id: Int = 0
        var mode_id: Int = 0
        var brightness: Float = 0.0f
        var contrast: Float = 0.0f
        var saturation: Float = 0.0f
        var blue_high: Float = 1.0f
    
        fun toConfigBits(): Long {
            return 0L
        }
    }

    interface DisplayService {
        fun get_color_config(id: Int, mode: Int): ColorModeConfig
        fun set_hdr_tm_mode(enabled: Boolean)
        fun set_color_mode(configBits: Long)
    }
}