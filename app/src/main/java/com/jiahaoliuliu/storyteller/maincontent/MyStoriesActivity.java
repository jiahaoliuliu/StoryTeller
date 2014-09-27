package com.jiahaoliuliu.storyteller.maincontent;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import com.etsy.android.grid.StaggeredGridView;
import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.database.MainDatabase;
import com.jiahaoliuliu.storyteller.maincontent.loaders.FillStoriesByAuthorCursorLoader;
import com.jiahaoliuliu.storyteller.utils.Preferences;

public class MyStoriesActivity extends BaseSessionActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MyStoriesActivity";

    private static final int LOADER_ID = 1;

    // Contents and layouts
    private ActionBar mActionBar;
    private StaggeredGridView mStoriesGridView;
    private CustomCursorAdapter mCustomCursorAdapter;
    // The list of the view ids where the data goes
    private int[] to = {R.id.title_text_view, R.id.content_text_view};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_stories);

        mActionBar = getSupportActionBar();

        // Set the action Bar
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

        // Layouts
        mStoriesGridView = (StaggeredGridView) findViewById(R.id.stories_grid_view);
        mStoriesGridView.setEmptyView(findViewById(R.id.empty_view));
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        setSupportProgressBarIndeterminate(true);
        return new FillStoriesByAuthorCursorLoader(
                MyStoriesActivity.this, mPreferences.getString(Preferences.StringId.PARSE_USER_NAME));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(TAG, cursor.getCount() + " stories retrieved");

        mCustomCursorAdapter = new CustomCursorAdapter(MyStoriesActivity.this, R.layout.story_layout,
                cursor, MainDatabase.TableStory.COLUMNS_SHOWN, to, 0);
        mStoriesGridView.setAdapter(mCustomCursorAdapter);
        mCustomCursorAdapter.notifyDataSetChanged();
        setSupportProgressBarIndeterminate(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.w(TAG, "Loader reset");
    }
}
