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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

/**
 * A {@link TileService quick settings tile} for scaling animation durations. Presents a dialog
 * allowing you to pick the desired animator duration scale.
 * <p>
 * Note that this requires a system level permission, so consumers <b>must</b> run this
 * <code>adb</code> command to use.
 * <p>
 * <code>adb shell pm grant uk.co.nickbutcher.animatordurationtile android.permission.WRITE_SECURE_SETTINGS</code>
 */
public class SelectAnimatorDuration extends AnimatorDurationTileService {

    private static final float[] scales = {
            0f,
            0.5f,
            1f,
            1.5f,
            2f,
            5f,
            10f
    };

    private final Runnable setScaleDialogRunnable = new Runnable() {
        @Override
        public void run() {
            final Dialog dialog = new AlertDialog.Builder(getBaseContext(),
                    android.R.style.Theme_Material_Light_Dialog)
                    .setTitle(R.string.dialog_title)
                    .setSingleChoiceItems(getScaleLabels(), getCurrentScaleIndex(),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int index) {
                                    if (setAnimatorScale(scales[index])) {
                                        updateTile();
                                    }
                                    dialogInterface.dismiss();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel, null)
                    .setCancelable(true)
                    .create();
            showDialog(dialog);
        }
    };

    public SelectAnimatorDuration() { }

    @Override
    public void onClick() {
        unlockAndRun(setScaleDialogRunnable);
    }

    private int getCurrentScaleIndex() {
        final float currentScale = getAnimatorScale();
        for (int i = 0; i < scales.length; i++) {
            if (currentScale == scales[i]) return i;
        }
        return -1;
    }

    private CharSequence[] getScaleLabels() {
        final CharSequence[] labels = new CharSequence[scales.length];
        for (int i = 0; i < scales.length; i++) {
            final float scale = scales[i];
            if (scale == 0f) {
                labels[i] = getString(R.string.animation_off);
            } else {
                labels[i] = getString(R.string.animation_scale, getScaleDisplay(scale));
            }
        }
        return labels;
    }

    private CharSequence getScaleDisplay(float scale) {
        final int roundedScale = Math.round(scale);
        if (roundedScale == scale) {
            return roundedScale + "x";
        }
        return scale + "x";
    }
}
