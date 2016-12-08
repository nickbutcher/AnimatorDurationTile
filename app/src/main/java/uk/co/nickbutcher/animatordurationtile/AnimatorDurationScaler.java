/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.nickbutcher.animatordurationtile;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

/**
 * A helper class for working with the system animator duration scale.
 * <p>
 * Note that this requires a system level permission, so consumers <b>must</b> run this
 * <code>adb</code> command to use.
 * <p>
 * <code>adb shell pm grant uk.co.nickbutcher.animatordurationtile android.permission.WRITE_SECURE_SETTINGS</code>
 */
class AnimatorDurationScaler {

    private static final String TAG = "AnimatorDurationScaler";

    private AnimatorDurationScaler() { }

    static @DrawableRes int getIcon(float scale) {
        if (scale <= 0f) {
            return R.drawable.ic_animator_duration_off;
        } else if (scale <= 0.5f) {
            return R.drawable.ic_animator_duration_half_x;
        } else if (scale <= 1f) {
            return R.drawable.ic_animator_duration_1x;
        } else if (scale <= 1.5f) {
            return R.drawable.ic_animator_duration_1_5x;
        } else if (scale <= 2f) {
            return R.drawable.ic_animator_duration_2x;
        } else if (scale <= 5f) {
            return R.drawable.ic_animator_duration_5x;
        } else if (scale <= 10f) {
            return R.drawable.ic_animator_duration_10x;
        }
        return R.drawable.ic_animator_duration;
    }

    static float getAnimatorScale(@NonNull ContentResolver contentResolver) {
        float scale = 1f;
        try {
            scale = Settings.Global.getFloat(contentResolver,
                    Settings.Global.ANIMATOR_DURATION_SCALE);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Could not read Animator Duration Scale setting", e);
        }
        return scale;
    }

    static boolean setAnimatorScale(
            @NonNull Context context,
            @FloatRange(from = 0.0, to = 10.0) float scale) {
        try {
            Settings.Global.putFloat(
                    context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, scale);
            return true;
        } catch (SecurityException se) {
            String message = context.getString(R.string.permission_required_toast);
            Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
            Log.d(TAG, message);
            return false;
        }
    }
}
