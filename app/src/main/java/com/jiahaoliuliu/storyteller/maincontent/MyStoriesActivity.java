package com.jiahaoliuliu.storyteller.maincontent;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.maincontent.BaseSessionActivity;

public class MyStoriesActivity extends BaseSessionActivity {

    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stories);

        mActionBar = getSupportActionBar();

        // Set the action Bar
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            // Finish the activity when the user press on the home button
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
