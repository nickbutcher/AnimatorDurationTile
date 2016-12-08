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

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CheckedTextView;

/**
 * An Activity which allows selecting the animator duration scale from a full list, accessed by
 * long pressing the quick action tile.
 */
public class SelectAnimatorDuration extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_scale_dialog);
        float scale = AnimatorDurationScaler.getAnimatorScale(getContentResolver());
        ((Checkable) findViewById(getScaleItemId(scale))).setChecked(true);
    }

    public void scaleClick(View v) {
        uncheckAllChildren((ViewGroup) v.getParent());
        ((CheckedTextView) v).setChecked(true);
        AnimatorDurationScaler.setAnimatorScale(this, getScale(v.getId()));
        finishAfterTransition();
    }

    public void cancel(View v) {
        finishAfterTransition();
    }

    private float getScale(@IdRes int id) {
        switch (id) {
            case R.id.scale_off: return 0f;
            case R.id.scale_0_5: return 0.5f;
            case R.id.scale_1_5: return 1.5f;
            case R.id.scale_2: return 2f;
            case R.id.scale_5: return 5f;
            case R.id.scale_10: return 10f;
            default:
                return 1f;
        }
    }

    private @IdRes int getScaleItemId(@FloatRange(from = 0.0, to = 10.0) float scale) {
        if (scale <= 0f) {
            return R.id.scale_off;
        } else if (scale <= 0.5f) {
            return R.id.scale_0_5;
        } else if (scale <= 1f) {
            return R.id.scale_1;
        } else if (scale <= 1.5f) {
            return R.id.scale_1_5;
        } else if (scale <= 2f) {
            return R.id.scale_2;
        } else if (scale <= 5f) {
            return R.id.scale_5;
        } else {
            return R.id.scale_10;
        }
    }

    private void uncheckAllChildren(@NonNull ViewGroup vg) {
        for (int i = vg.getChildCount() - 1; i >= 0; i--) {
            View child = vg.getChildAt(i);
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(false);
            }
        }
    }

}
