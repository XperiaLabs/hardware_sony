# Xperia Parts (hardware_sony) Branch Info

## Base: 1 III | 5 III | Pro-I (sony_extra-sagami REQUIRED)

Also, this branch may also have some specific commits like for example moving amplifier to it from the DT to hw_sony, so it's recommended to use the Core in some cases (like for the 10 III) instead of the Base branch.

## Base-Menu: 1 III | 5 III | Pro-I (sony_extra-sagami REQUIRED)

Same as the Base branch, but the Xperia Parts menu is in Settings > Xperia Parts instead of Settings > System > Xperia Parts. (Because some ROMs do not support custom settings on the main page of the Settings so that is why the default branch keeps it in System)
This branch is useful for ROMs like AlphaDroid or ROMs with clean AOSP Settings.

## Base-MK4: 1 IV | 5 IV

Based on the Core branch but with added support for the Mark 4 display interface that Creator Mode utilizes.

## Core: Any Device

A clean branch without any extra dependecies like an sony_extra repo and this branch can be built as is.
