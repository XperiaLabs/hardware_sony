/*
 * Copyright (C) 2023 XperiaLabs Project
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xperia.settings.dirac;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.xperia.settings.dirac.DiracUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final boolean DEBUG = false;
    private static final String TAG = "XperiaDIRAC";

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (DEBUG) Log.d(TAG, "Received boot completed intent");
        try {
            DiracUtils.getInstance(context);
        } catch (Exception e) {
            Log.d(TAG, "DIRAC is not present in system");
        }
    }
}