package com.azoft.azoft.collage.ui.activities.phone;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

public abstract class CollageAuthActivity extends CollageActivity {

    private static final String EXTRA_ACCESS_TOKEN = "com.azoft.azoft.collage.ui.activities.phone.CollageAuthActivity.EXTRA_ACCESS_TOKEN";

    public static Intent createIntent(final Context context, final Class<? extends CollageAuthActivity> activityClass, final String accessToken) {
        return new Intent(context, activityClass).putExtra(EXTRA_ACCESS_TOKEN, accessToken);
    }

    public static Intent createIntent(final CollageAuthActivity collageAuthActivity, final Class<? extends CollageAuthActivity> activityClass) {
        return new Intent(collageAuthActivity, activityClass).putExtra(EXTRA_ACCESS_TOKEN, collageAuthActivity.getIntent().getStringExtra(EXTRA_ACCESS_TOKEN));
    }

    protected String mAccessToken;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAccessToken = getIntent().getStringExtra(EXTRA_ACCESS_TOKEN);
        if (null == mAccessToken) {
            finish();
        } else {
            onCreate(savedInstanceState, mAccessToken);
        }
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // should return to start activity and clear all others
            final Intent intent = CollageAuthActivity.createIntent(this, StartActivity.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }

            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected abstract void onCreate(final Bundle savedInstanceState, final String accessToken);
}