package de.andreasschrade.androidtemplate.activities.peripheral;

import android.os.Bundle;

import de.andreasschrade.androidtemplate.R;
import de.andreasschrade.androidtemplate.activities.base.BaseActivity;

/**
 * Simple wrapper for {@link ProfileDetailFragment}
 * This wrapper is only used in single pan mode (= on smartphones)
 * Created by Andreas Schrade on 14.12.2015.
 */
public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        // Show the Up button in the action bar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ProfileDetailFragment fragment =  ProfileDetailFragment.newInstance(getIntent().getStringExtra(ProfileDetailFragment.ARG_ITEM_ID));
        getFragmentManager().beginTransaction().replace(R.id.article_detail_container, fragment).commit();
    }

    @Override
    public boolean providesActivityToolbar() {
        return false;
    }
}
