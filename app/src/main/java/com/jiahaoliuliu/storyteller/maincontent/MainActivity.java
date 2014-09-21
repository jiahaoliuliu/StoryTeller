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

import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.model.Story;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class MainActivity extends BaseSessionActivity {

    private static final String TAG = "MainActivity";

    private static final int MENU_ITEM_RIGHT_LIST_ID = 10000;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mRightFrameLayout;
    private RightFragment mRightFragment;
    private FragmentManager mFragmentManager;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
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
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    Log.v(TAG, "Stories retrieved from the parse");
                    for (ParseObject parseObject: parseObjects) {
                        String title = parseObject.getString(Story.TITLE_KEY);
                        String content = parseObject.getString(Story.CONTENT_KEY);
                        Log.v(TAG, title + " " + content);
                    }
                }
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
