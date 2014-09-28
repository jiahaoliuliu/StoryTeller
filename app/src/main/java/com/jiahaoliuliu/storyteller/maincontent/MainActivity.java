package com.jiahaoliuliu.storyteller.maincontent;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.database.MainDatabase;
import com.jiahaoliuliu.storyteller.database.StoryDataLayer;
import com.jiahaoliuliu.storyteller.interfaces.OnCreateStoryRequestedListener;
import com.jiahaoliuliu.storyteller.interfaces.OnShareStoryRequestedListener;
import com.jiahaoliuliu.storyteller.maincontent.loaders.FillAllStoriesCursorLoader;
import com.jiahaoliuliu.storyteller.model.Story;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;


public class MainActivity extends BaseSessionActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, OnCreateStoryRequestedListener,
        OnShareStoryRequestedListener {

    private static final String TAG = "MainActivity";

    private static final int MENU_ITEM_RIGHT_DRAWER_ID = 10000;
    private static final int MENU_ITEM_SEARCH_ID = 10001;
    private static final int MENU_ITEM_NEW_STORY_ID = 10002;

    // Special icons to create a new story
    private static final int MENU_ITEM_NEW_STORY_ACCEPT_ID = 10003;
    private static final int MENU_ITEM_NEW_STORY_CANCEL_ID = 10004;

    private static final int LOADER_ID = 1;

    // Content and layouts
    private StaggeredGridView mStoriesGridView;
    private CustomCursorAdapter mCustomCursorAdapter;
    private LeftFragment mLeftFragment;
    private RightFragment mRightFragment;
    // The list of the view ids where the data goes
    private int[] to = {R.id.title_text_view, R.id.content_text_view};

    // Drawers
    private DrawerLayout mDrawerLayout;
    private FrameLayout mLeftFrameLayout;
    private FrameLayout mRightFrameLayout;
    private FragmentManager mFragmentManager;
    private ActionBarDrawerToggle mDrawerToggle;

    // Internal data
    private StoryDataLayer mStoryDataLayer;

    // System
    private InputMethodManager mImm;
    private LoaderManager mLoaderManager;

    // The menu for the action bar
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // System
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mLoaderManager = getSupportLoaderManager();
        mFragmentManager = getSupportFragmentManager();

        mStoryDataLayer = StoryDataLayer.getInstance();

        // layouts
        mStoriesGridView = (StaggeredGridView)findViewById(R.id.stories_grid_view);

        mLeftFragment = new LeftFragment();
        mFragmentManager.beginTransaction().add(R.id.drawer_left_frame_layout, mLeftFragment).commit();

        mRightFragment = new RightFragment();
        mFragmentManager.beginTransaction().add(R.id.drawer_right_frame_layout, mRightFragment).commit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftFrameLayout = (FrameLayout) findViewById(R.id.drawer_left_frame_layout);
        mRightFrameLayout = (FrameLayout) findViewById(R.id.drawer_right_frame_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_navigation_drawer,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (drawerView.equals(mLeftFrameLayout)) {
                    Log.v(TAG, "Left drawer closed");
                } else if (drawerView.equals(mRightFrameLayout)) {
                    Log.v(TAG, "Right drawer closed");
                }
                addActionBarMenuItems();
                // Close the soft keyboard in case it is open
                mImm.hideSoftInputFromWindow(mLeftFrameLayout.getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (drawerView.equals(mLeftFrameLayout)) {
                    Log.v(TAG, "Left drawer opened");
                    createActionBarItemForLeftDrawer();
                } else if (drawerView.equals(mRightFrameLayout)) {
                    Log.v(TAG, "Right drawer opened");
                    createActionBarItemForRightDrawer();
                }
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // Trying to querying data
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Story.STORY_KEY);
        setSupportProgressBarIndeterminateVisibility(true);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    Log.v(TAG, "Stories retrieved from the parse");
                    for (ParseObject parseObject: parseObjects) {
                        try {
                            Story tmpStory = new Story(parseObject);
                            Log.v(TAG, tmpStory.toString());
                            // Save the data persistently to be used for specific situations
                            // such as search or off-line mode
                            mStoryDataLayer.insertOrUpdateStory(tmpStory);
                        } catch (IllegalArgumentException illegalArgumentException) {
                            Log.w(TAG, "Error getting stories from the Parse ", illegalArgumentException);
                        }
                    }
                }
                mLoaderManager.initLoader(LOADER_ID, null, MainActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Close all the drawers
        if (mDrawerLayout.isDrawerOpen(mLeftFrameLayout)) {
            mDrawerLayout.closeDrawer(mLeftFrameLayout);
        }

        if (mDrawerLayout.isDrawerOpen(mRightFrameLayout)) {
            mDrawerLayout.closeDrawer(mRightFrameLayout);
        }

        // Close the soft keyboard in case it is open
        mImm.hideSoftInputFromWindow(mLeftFrameLayout.getWindowToken(), 0);
    }

    // =================================================  Action bar ====================================================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        addActionBarMenuItems();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_NEW_STORY_ID:
                // If the right drawer was open, close it
                if (mDrawerLayout.isDrawerOpen(mLeftFrameLayout)) {
                    mDrawerLayout.closeDrawer(mLeftFrameLayout);
                    // If the left drawer was closed, check if
                    // the left drawer is open. If so, close the left drawer
                    // Then opens the right drawer
                } else {
                    if (mDrawerLayout.isDrawerOpen(mRightFrameLayout)) {
                        mDrawerLayout.closeDrawer(mRightFrameLayout);
                    }
                    mDrawerLayout.openDrawer(mLeftFrameLayout);
                }
                return true;
            case MENU_ITEM_RIGHT_DRAWER_ID :
                // If the right drawer was open, close it
                if (mDrawerLayout.isDrawerOpen(mRightFrameLayout)) {
                    mDrawerLayout.closeDrawer(mRightFrameLayout);
                // If the left drawer was closed, check if
                // the left drawer is open. If so, close the left drawer
                // Then opens the right drawer
                } else {
                    if (mDrawerLayout.isDrawerOpen(mLeftFrameLayout)) {
                        mDrawerLayout.closeDrawer(mLeftFrameLayout);
                    }
                    mDrawerLayout.openDrawer(mRightFrameLayout);
                }
                return true;
            case MENU_ITEM_NEW_STORY_ACCEPT_ID:
                // If the story was created correctly, the close the drawer
                if (mLeftFragment.createStory()) {
                    mDrawerLayout.closeDrawer(mLeftFrameLayout);
                }
                return true;
            case MENU_ITEM_NEW_STORY_CANCEL_ID:
                mLeftFragment.cancelStory();
                mDrawerLayout.closeDrawer(mLeftFrameLayout);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addActionBarMenuItems() {
        // First remove all the items left
        if (mMenu == null) {
            throw new IllegalArgumentException("The mMenu cannot be null");
        }

        mMenu.clear();

        // Then add each one of the items
        addActionBarMenuNewStoryItem();
        addActionBarMenuSearchItem();
        addActionBarMenuDrawerItem();
    }

    private void addActionBarMenuSearchItem() {
        // If the search item has not been added in the menu, add it
        if (mMenu.findItem(MENU_ITEM_SEARCH_ID) != null) {
            Log.w(TAG, "The search item has been already added in the action bar");
            return;
        }

        MenuItem searchMenuItem = mMenu.add(Menu.NONE, MENU_ITEM_SEARCH_ID, Menu
                .NONE, R.string.action_bar_search)
                .setIcon(R.drawable.ic_action_search)
                .setActionView(R.layout.search_layout);
        searchMenuItem.setShowAsAction
                (MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        View searchActionView = searchMenuItem.getActionView();
        final EditText searchEditText = (EditText) searchActionView.findViewById(R.id.search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mCustomCursorAdapter != null) {
                    mCustomCursorAdapter.getFilter().filter(s.toString());
                }
            }
        });

        // Set the expand/collapse listener because when it has been collapsed, the list should
        // return to the normal state
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchEditText.requestFocus();
                mImm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                // Remove all the other items in the menu
                mMenu.removeItem(MENU_ITEM_RIGHT_DRAWER_ID);
                mMenu.removeItem(MENU_ITEM_NEW_STORY_ID);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchEditText.setText("");
                searchEditText.clearFocus();
                mImm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                addActionBarMenuItems();
                return true;
            }
        });
    }

    private void addActionBarMenuDrawerItem () {
        // If the drawer item has already been added in the menu, not do anything
        if (mMenu.findItem(MENU_ITEM_RIGHT_DRAWER_ID) != null) {
            Log.w(TAG, "The drawer item has been already added in the action bar");
            return;
        }

        // This icon must be shown always, and it must be placed
        // on the rightest place of the action bar
        mMenu.add(Menu.NONE, MENU_ITEM_RIGHT_DRAWER_ID, Menu
                .NONE, R.string.open_right_drawer)
                .setIcon(R.drawable.ic_navigation_drawer)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM );
    }

    private void addActionBarMenuNewStoryItem() {
        // If the item has already been added in the menu, not do anything
        if (mMenu.findItem(MENU_ITEM_NEW_STORY_ID) != null) {
            Log.w(TAG, "The new story item has been already added in the action bar");
            return;
        }

        mMenu.add(Menu.NONE, MENU_ITEM_NEW_STORY_ID, Menu
                .NONE, R.string.action_bar_new_story)
                .setIcon(R.drawable.ic_action_story_new)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM );
    }

    private void addActionBarMenuNewStoryAcceptItem() {
        // If the item has already been added in the menu, not do anything
        if (mMenu.findItem(MENU_ITEM_NEW_STORY_ACCEPT_ID) != null) {
            Log.w(TAG, "The new story accept item has been already added in the action bar");
            return;
        }

        mMenu.add(Menu.NONE, MENU_ITEM_NEW_STORY_ACCEPT_ID, Menu
                .NONE, R.string.action_bar_new_story_accept)
                .setIcon(R.drawable.ic_action_accept)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    private void addActionBarMenuNewStoryCancelItem() {
        // If the item has already been added in the menu, not do anything
        if (mMenu.findItem(MENU_ITEM_NEW_STORY_CANCEL_ID) != null) {
            Log.w(TAG, "The new story cancel item has been already added in the action bar");
            return;
        }

        mMenu.add(Menu.NONE, MENU_ITEM_NEW_STORY_CANCEL_ID, Menu
                .NONE, R.string.action_bar_new_story_cancel)
                .setIcon(R.drawable.ic_action_cancel)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
    }

    private void createActionBarItemForRightDrawer() {
        if (mMenu == null) {
            Log.e(TAG, "The drawer is open but the menu has not created. This shouldn't happens");
            return;
        }

        // First remove all the items
        mMenu.clear();
        addActionBarMenuDrawerItem();
    }

    private void createActionBarItemForLeftDrawer() {
        if (mMenu == null) {
            Log.e(TAG, "The drawer is open but the menu has not created. This shouldn't happens");
            return;
        }

        // First remove all the items
        mMenu.clear();
        addActionBarMenuNewStoryAcceptItem();
        addActionBarMenuNewStoryCancelItem();
    }

    // ==================================================================================================================

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        return new FillAllStoriesCursorLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        Log.v(TAG, "Cursor retrieved");
        mCustomCursorAdapter = new CustomCursorAdapter(MainActivity.this, R.layout.story_layout,
                cursor, MainDatabase.TableStory.COLUMNS_SHOWN, to, 0, this);
        mStoriesGridView.setAdapter(mCustomCursorAdapter);

        mCustomCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                return mStoryDataLayer.searchStoryByText(constraint.toString());
            }
        });
        setSupportProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.w(TAG, "Loader reset");
    }

    @Override
    public void requestCreateStory(Story story) {
        setSupportProgressBarIndeterminateVisibility(true);
        final ParseObject newStoryParseObject = new ParseObject(Story.STORY_KEY);
        newStoryParseObject.put(Story.TITLE_KEY, story.getTitle());
        newStoryParseObject.put(Story.CONTENT_KEY, story.getContent());
        newStoryParseObject.put(Story.AUTHOR_KEY, story.getAuthor());
        newStoryParseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Story newStory = new Story(newStoryParseObject);
                    mStoryDataLayer.insertOrUpdateStory(newStory);
                    mLoaderManager.restartLoader(LOADER_ID, null, MainActivity.this);
                    mLeftFragment.onStorySuccessfulCreated();
                    Toast.makeText(MainActivity.this, R.string.story_saved_correctly, Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Error saving the parse object", e);
                    Toast.makeText(MainActivity.this, R.string.error_saving_story, Toast.LENGTH_LONG).show();
                    setSupportProgressBarIndeterminateVisibility(false);
                }
            }
        });
    }
}
