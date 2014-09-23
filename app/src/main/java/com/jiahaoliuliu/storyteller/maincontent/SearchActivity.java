package com.jiahaoliuliu.storyteller.maincontent;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

import com.etsy.android.grid.StaggeredGridView;
import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.database.MainDatabase;

public class SearchActivity extends BaseSessionActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String TAG = "SearchActivity";
    private static final int LOADER_ID = 1;

    // Layouts
    private ActionBar mActionBar;
    private EditText searchEditText;
    private StaggeredGridView mStoriesGridView;

    private SimpleCursorAdapter mSimpleCursorAdapter;

    // The list of the view ids where the data goes
    private int[] to = {R.id.title_text_view, R.id.content_text_view};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Set the actionbar
        mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        // Link the layout elements
        searchEditText = (EditText) findViewById(R.id.search_edit_text);
        mStoriesGridView = (StaggeredGridView) findViewById(R.id.stories_grid_view);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Go back to the previous activity
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new SearchCursorLoader(SearchActivity.this);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        Log.v(TAG, "Cursor retrieved");
        mSimpleCursorAdapter = new SimpleCursorAdapter(SearchActivity.this, R.layout.story_layout,
                cursor, MainDatabase.TableStory.COLUMNS, to, 0);
        mStoriesGridView.setAdapter(mSimpleCursorAdapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.w(TAG, "Loader reset");
    }
}
