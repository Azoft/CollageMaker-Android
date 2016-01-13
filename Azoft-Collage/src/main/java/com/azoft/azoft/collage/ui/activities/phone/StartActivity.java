package com.azoft.azoft.collage.ui.activities.phone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.azoft.azoft.collage.R;
import com.azoft.azoft.collage.data.Collage;
import com.azoft.azoft.collage.data.User;
import com.azoft.azoft.collage.ui.fragments.CollageSelectionFragment;
import com.mig35.injectorlib.utils.inject.InjectSavedState;
import com.mig35.injectorlib.utils.inject.InjectView;

/**
 * Date: 4/10/2014
 * Time: 11:57 AM
 *
 * @author MiG35
 */
public class StartActivity extends CollageAuthActivity {

    private static final int USER_SELECTION_REQUEST_CODE = 14;

    @InjectView(R.id.b_current_user)
    private Button mCurrentUserButton;

    @InjectSavedState
    private User mCurrentUser;

    @Override
    protected void onCreate(final Bundle savedInstanceState, final String accessToken) {
        setContentView(R.layout.activity_start);

        mCurrentUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startActivityForResult(CollageAuthActivity.createIntent(StartActivity.this, UserSelectionActivity.class), USER_SELECTION_REQUEST_CODE);
            }
        });
        final FragmentManager fm = getSupportFragmentManager();
        if (null == fm.findFragmentById(R.id.container_collage_selection)) {
            fm.beginTransaction().add(R.id.container_collage_selection, new CollageSelectionFragment()).commit();
        }

        updateButtonText();
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.start_collage, menu);

        super.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.menu_create_collage) {
            final CollageSelectionFragment collageSelectionFragment =
                    (CollageSelectionFragment) getSupportFragmentManager().findFragmentById(R.id.container_collage_selection);
            final Collage collage = collageSelectionFragment.getSelectedCollage();
            if (null == mCurrentUser) {
                showToast(R.string.error_select_user_first);
            } else if (null == collage) {
                showToast(R.string.error_select_collage_first);
            } else {
                openCollageBuilder(mCurrentUser, collage);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCollageBuilder(final User currentUser, final Collage collage) {
        startActivity(CollageAuthActivity.createIntent(this, CollageBuilderActivity.class).putExtra(CollageBuilderActivity.EXTRA_USER, currentUser)
                .putExtra(CollageBuilderActivity.EXTRA_KOLAJ, collage));
    }

    private void updateButtonText() {
        if (null == mCurrentUser) {
            mCurrentUserButton.setText(R.string.text_select_current_user);
        } else {
            mCurrentUserButton.setText(getString(R.string.text_current_user_selected, mCurrentUser.getNickName()));
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (USER_SELECTION_REQUEST_CODE == requestCode && resultCode == RESULT_OK && null != data) {
            mCurrentUser = (User) data.getSerializableExtra(UserSelectionActivity.RESULT_USER);
            updateButtonText();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}