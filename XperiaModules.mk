#
# Copyright (C) 2023 XperiaLabs Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Flags
TARGET_SUPPORTS_CREATOR_MODE ?= true
TARGET_SUPPORTS_HIGH_REFRESH_RATE ?= true
TARGET_SUPPORTS_SOUND_ENHANCEMENT ?= true
TARGET_SUPPORTS_BATTERY_CARE ?= true
TARGET_SUPPORTS_EUICC ?= true

# Soong Namespace
PRODUCT_SOONG_NAMESPACES += \
    $(LOCAL_PATH)/XperiaModules

# Main Module
PRODUCT_PACKAGES += \
    XperiaSettings \

# Submodules
ifeq ($(TARGET_SUPPORTS_CREATOR_MODE),true)
	PRODUCT_PACKAGES += XperiaDisplay
endif

ifeq ($(TARGET_SUPPORTS_HIGH_REFRESH_RATE),true)
	PRODUCT_PACKAGES += XperiaSwitcher
endif

ifeq ($(TARGET_SUPPORTS_SOUND_ENHANCEMENT),true)
	PRODUCT_PACKAGES += \
	XperiaAudio \
    XperiaDirac \
	XperiaTSRA
endif

ifeq ($(TARGET_SUPPORTS_BATTERY_CARE),true)
include hardware/sony/sepolicy/qti/SEPolicy.mk
	PRODUCT_PACKAGES += XperiaCharger
endif

ifeq ($(TARGET_SUPPORTS_EUICC),true)
	PRODUCT_PACKAGES += XperiaEuicc
endif