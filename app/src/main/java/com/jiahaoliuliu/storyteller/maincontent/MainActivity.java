package com.jiahaoliuliu.storyteller.maincontent;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.etsy.android.grid.StaggeredGridView;
import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.database.StoryDataLayer;
import com.jiahaoliuliu.storyteller.model.Story;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseSessionActivity {

    private static final String TAG = "MainActivity";

    private static final int MENU_ITEM_RIGHT_LIST_ID = 10000;

    // Content and layouts
    private StaggeredGridView mStoriesGridView;
    private StoriesAdapter mStoriesAdapter;
    private RightFragment mRightFragment;

    // Drawers
    private DrawerLayout mDrawerLayout;
    private FrameLayout mRightFrameLayout;
    private FragmentManager mFragmentManager;
    private ActionBarDrawerToggle mDrawerToggle;

    // Internal data
    private List<Story> mAllStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStoriesGridView = (StaggeredGridView)findViewById(R.id.stories_grid_view);

        mFragmentManager = getSupportFragmentManager();
        mRightFragment = new RightFragment();
        mFragmentManager.beginTransaction().add(R.id.drawer_right_frame_layout, mRightFragment).commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRightFrameLayout = (FrameLayout) findViewById(R.id.drawer_right_frame_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Trying to querying data
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Story.STORY_KEY);
        setProgressBarIndeterminate(true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    Log.v(TAG, "Stories retrieved from the parse");
                    mAllStories = new ArrayList<Story>();
                    StoryDataLayer storyDataLayer = StoryDataLayer.getInstance();
                    for (ParseObject parseObject: parseObjects) {
                        try {
                            Story tmpStory = new Story(parseObject);
                            Log.v(TAG, tmpStory.toString());
                            mAllStories.add(tmpStory);
                            storyDataLayer.insertOrUpdateStory(tmpStory);
                        } catch (IllegalArgumentException illegalArgumentException) {
                            Log.w(TAG, "Error getting stories from the Parse ", illegalArgumentException);
                        }
                    }

                    mStoriesAdapter = new StoriesAdapter(MainActivity.this, mAllStories);
                    mStoriesGridView.setAdapter(mStoriesAdapter);
                }
                setProgressBarIndeterminate(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ITEM_RIGHT_LIST_ID, Menu
                .NONE, "R.List")
                .setIcon(R.drawable.ic_navigation_drawer)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_ITEM_RIGHT_LIST_ID) {
            if (mDrawerLayout.isDrawerOpen(mRightFrameLayout)) {
                mDrawerLayout.closeDrawer(mRightFrameLayout);
            } else {
                mDrawerLayout.openDrawer(mRightFrameLayout);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
