/*
 * Copyright (C) 2023 XperiaLabs Project
 * Copyright (C) 2016-2021 crDroid Android Project
 * Copyright (C) 2022 The LineageOS Project
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.xperia.settings.charger.widgets

import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.TypedArrayUtils
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.xperia.settings.charger.R

class CustomSeekBarPreference @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = TypedArrayUtils.getAttr(
        context,
        R.attr.preferenceStyle,
        android.R.attr.preferenceStyle
    ), defStyleRes: Int = 0
) : Preference(context, attrs, defStyleAttr, defStyleRes), SeekBar.OnSeekBarChangeListener {

    // Values
    private var mMinValue = 0
    private var mMaxValue = 100
    private var mDefaultValueExists = false
    private var mDefaultValue = 0
    private var mDefaultValueTextExists = false
    private var mDefaultValueText: String? = null
    private var mValue = 0

    // Related components
    private var mValueTextView: TextView? = null
    private var mResetImageView: ImageView? = null
    private var mMinusImageView: ImageView? = null
    private var mPlusImageView: ImageView? = null
    private var mSeekBar: SeekBar

    // Other misc settings
    private var mInterval = 1
    private var mShowSign = false
    private var mTextSuffix = ""
    private var mContinuousUpdates = false
    private var mTrackingTouch = false
    private var mTrackingValue = 0

    init {
        val styledAttrs =
            context.obtainStyledAttributes(attrs, R.styleable.CustomSeekBarPreference)

        // Whether the "+" sign should be shown
        mShowSign = styledAttrs.getBoolean(R.styleable.CustomSeekBarPreference_showSign, mShowSign)

        // Check if we should use suffix for text
        val textSuffix = styledAttrs.getString(R.styleable.CustomSeekBarPreference_units)
        if (textSuffix != null) mTextSuffix = " $textSuffix"

        // Check if we should report values continuously
        mContinuousUpdates = styledAttrs.getBoolean(
            R.styleable.CustomSeekBarPreference_continuousUpdates,
            mContinuousUpdates
        )

        // Special texts when the value is default
        val defaultValueText =
            styledAttrs.getString(R.styleable.CustomSeekBarPreference_defaultValueText)
        mDefaultValueTextExists = !defaultValueText.isNullOrEmpty()
        if (mDefaultValueTextExists) {
            mDefaultValueText = defaultValueText
        }

        // Step for every increment / decrement
        try {
            val newInterval = attrs!!.getAttributeValue(SETTINGS_NS, "interval")
            if (newInterval != null) mInterval = newInterval.toInt()
        } catch (e: Exception) {
            Log.e(TAG, "Invalid interval value", e)
        }

        // Min, max and default values
        mMinValue = attrs!!.getAttributeIntValue(SETTINGS_NS, "min", mMinValue)
        mMaxValue = attrs.getAttributeIntValue(ANDROID_NS, "max", mMaxValue)
        if (mMaxValue < mMinValue) mMaxValue = mMinValue
        val defaultValue = attrs.getAttributeIntValue(ANDROID_NS, "defaultValue", 100)
        mDefaultValueExists = true
        mDefaultValue = getLimitedValue(defaultValue.toInt())
        mValue = mDefaultValue

        // Initialize seekbar and other resources
        mSeekBar = SeekBar(context, attrs)
        layoutResource = R.layout.preference_custom_seekbar
    }

    override fun onDependencyChanged(dependency: Preference, disableDependent: Boolean) {
        super.onDependencyChanged(dependency, disableDependent)
        this.shouldDisableView = true
        mSeekBar.isEnabled = !disableDependent
        mResetImageView?.isEnabled = !disableDependent
        mPlusImageView?.isEnabled = !disableDependent
        mMinusImageView?.isEnabled = !disableDependent
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        try {
            // move our seekbar to the new view we've been given
            val oldContainer = mSeekBar.parent
            val newContainer = holder.findViewById(R.id.seekbar) as ViewGroup
            if (oldContainer !== newContainer) {
                // remove the seekbar from the old view
                if (oldContainer != null) {
                    (oldContainer as ViewGroup).removeView(mSeekBar)
                }
                // remove the existing seekbar (there may not be one) and add ours
                newContainer.removeAllViews()
                newContainer.addView(
                    mSeekBar, ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error binding view: $ex")
        }
        mSeekBar.max = getSeekValue(mMaxValue)
        mSeekBar.progress = getSeekValue(mValue)
        mSeekBar.isEnabled = isEnabled
        mValueTextView = holder.findViewById(R.id.value) as TextView
        mResetImageView = holder.findViewById(R.id.reset) as ImageView
        mMinusImageView = holder.findViewById(R.id.minus) as ImageView
        mPlusImageView = holder.findViewById(R.id.plus) as ImageView
        updateValueViews()
        mSeekBar.setOnSeekBarChangeListener(this)
        mResetImageView!!.setOnClickListener {
            Toast.makeText(
                context,
                context.getString(
                    R.string.custom_seekbar_default_value_to_set,
                    getTextValue(mDefaultValue)
                ),
                Toast.LENGTH_LONG
            ).show()
        }
        mResetImageView!!.onLongClickListener = View.OnLongClickListener {
            setValue(mDefaultValue, true)
            true
        }
        mMinusImageView!!.setOnClickListener { setValue(mValue - mInterval, true) }
        mMinusImageView!!.onLongClickListener = View.OnLongClickListener {
            setValue(
                if (mMaxValue - mMinValue > mInterval * 2 && mMaxValue + mMinValue < mValue * 2)
                    Math.floorDiv(
                        mMaxValue + mMinValue,
                        2
                    ) else mMinValue, true
            )
            true
        }
        mPlusImageView!!.setOnClickListener { setValue(mValue + mInterval, true) }
        mPlusImageView!!.onLongClickListener = View.OnLongClickListener {
            setValue(
                if (mMaxValue - mMinValue > mInterval * 2 && mMaxValue + mMinValue > mValue * 2) -1
                        * Math.floorDiv(
                    -1 * (mMaxValue + mMinValue),
                    2
                ) else mMaxValue, true
            )
            true
        }
    }

    private fun getLimitedValue(v: Int): Int {
        return if (v < mMinValue) mMinValue else if (v > mMaxValue) mMaxValue else v
    }

    private fun getSeekValue(v: Int): Int {
        return 0 - Math.floorDiv(mMinValue - v, mInterval)
    }

    private fun getTextValue(v: Int): String? {
        return if (mDefaultValueTextExists && mDefaultValueExists && (v == mDefaultValue)) {
            mDefaultValueText
        } else (if (mShowSign && v > 0) "+" else "") + v.toString() + mTextSuffix
    }

    protected fun updateValueViews() {
        if (!mTrackingTouch || mContinuousUpdates) {
            if (mDefaultValueTextExists && mDefaultValueExists && mValue == mDefaultValue) {
                mValueTextView?.text = (mDefaultValueText + " (" +
                        context.getString(R.string.custom_seekbar_default_value) + ")")
            } else {
                mValueTextView?.text =
                    context.getString(R.string.custom_seekbar_value, getTextValue(mValue)) +
                            if (mDefaultValueExists && mValue == mDefaultValue) " (" +
                                    context.getString(R.string.custom_seekbar_default_value) + ")"
                            else ""
            }
        } else {
            if (mDefaultValueTextExists && mDefaultValueExists && (mTrackingValue == mDefaultValue)) {
                mValueTextView?.text = "[$mDefaultValueText]"
            } else {
                mValueTextView?.text = context.getString(
                    R.string.custom_seekbar_value,
                    "[" + getTextValue(mTrackingValue) + "]"
                )
            }
        }
        if (!mDefaultValueExists || (mValue == mDefaultValue) || mTrackingTouch)
            mResetImageView?.visibility =
                View.INVISIBLE else mResetImageView?.visibility = View.VISIBLE
        if (mValue == mMinValue || mTrackingTouch) {
            mMinusImageView?.isClickable = false
            mMinusImageView?.setColorFilter(
                context.getColor(R.color.disabled_text_color),
                PorterDuff.Mode.MULTIPLY
            )
        } else {
            mMinusImageView?.isClickable = true
            mMinusImageView?.clearColorFilter()
        }
        if (mValue == mMaxValue || mTrackingTouch) {
            mPlusImageView?.isClickable = false
            mPlusImageView?.setColorFilter(
                context.getColor(R.color.disabled_text_color),
                PorterDuff.Mode.MULTIPLY
            )
        } else {
            mPlusImageView?.isClickable = true
            mPlusImageView?.clearColorFilter()
        }
    }

    private fun changeValue(newValue: Int) {
        // for subclasses
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val newValue = getLimitedValue(mMinValue + (progress * mInterval))
        if (mTrackingTouch && !mContinuousUpdates) {
            mTrackingValue = newValue
            updateValueViews()
        } else if (mValue != newValue) {
            // change rejected, revert to the previous value
            if (!callChangeListener(newValue)) {
                mSeekBar.progress = getSeekValue(mValue)
                return
            }
            // change accepted, store it
            changeValue(newValue)
            persistInt(newValue)
            mValue = newValue
            updateValueViews()
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        mTrackingValue = mValue
        mTrackingTouch = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        mTrackingTouch = false
        if (!mContinuousUpdates) onProgressChanged(mSeekBar, getSeekValue(mTrackingValue), false)
        notifyChanged()
    }

    override fun onSetInitialValue(restoreValue: Boolean, defaultValue: Any?) {
        if (restoreValue) mValue = getPersistedInt(mValue)
    }

    private fun setDefaultValue(newValue: Int) {
        var v = newValue
        v = getLimitedValue(v)
        if (!mDefaultValueExists || mDefaultValue != v) {
            mDefaultValueExists = true
            mDefaultValue = v
            updateValueViews()
        }
    }

    private fun setDefaultValue(newValue: String?) {
        if (mDefaultValueExists && newValue.isNullOrEmpty()) {
            mDefaultValueExists = false
            updateValueViews()
        } else if (!newValue.isNullOrEmpty()) {
            setDefaultValue(newValue.toInt())
        }
    }

    fun setValue(newValue: Int, update: Boolean) {
        var v = newValue
        v = getLimitedValue(v)
        if (mValue != v) {
            if (update) mSeekBar.progress = getSeekValue(v) else mValue = v
        }
    }

    var value: Int
        get() = mValue
        set(newValue) {
            mValue = getLimitedValue(newValue)
            mSeekBar.progress = getSeekValue(mValue)
        }

    fun refresh(newValue: Int) {
        // this will ...
        setValue(newValue, true)
    }

    companion object {
        private const val SETTINGS_NS = "http://schemas.android.com/apk/res/com.android.settings"
        private const val ANDROID_NS = "http://schemas.android.com/apk/res/android"
        private const val TAG = "CustomSeekBarPreference"
    }
}