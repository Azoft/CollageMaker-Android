package com.azoft.azoft.collage.ui.activities.phone;

import android.support.v4.app.Fragment;

import com.azoft.azoft.collage.data.CollageFillData;
import com.azoft.azoft.collage.ui.fragments.CollagePreviewFragment;

import java.io.Serializable;

/**
 * Date: 4/9/2014
 * Time: 5:56 PM
 *
 * @author MiG35
 */
public class CollagePreviewActivity extends SingleFragmentActivity {

    public static final String EXTRA_KOLAJ_FILL_DATA =
            "com.redmadrobot.azoft.collage.ui.activities.phone.CollagePreviewActivity.EXTRA_KOLAJ_FILL_DATA";

    @Override
    protected Fragment createFragment() {
        final Serializable collageFillData = getIntent().getSerializableExtra(EXTRA_KOLAJ_FILL_DATA);
        if (collageFillData instanceof CollageFillData) {
            return CollagePreviewFragment.getInstance((CollageFillData) collageFillData);
        }
        return null;
    }
}