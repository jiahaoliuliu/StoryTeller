package com.jiahaoliuliu.storyteller.maincontent;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;

import com.facebook.Session;
import com.jiahaoliuliu.storyteller.LoginActivity;
import com.jiahaoliuliu.storyteller.database.MainDatabase;
import com.jiahaoliuliu.storyteller.interfaces.OnExitRequestedListener;
import com.jiahaoliuliu.storyteller.interfaces.OnSessionRequestedListener;
import com.jiahaoliuliu.storyteller.interfaces.OnSetProgressBarIndeterminateRequested;
import com.jiahaoliuliu.storyteller.interfaces.OnShareStoryRequestedListener;
import com.jiahaoliuliu.storyteller.model.Story;
import com.jiahaoliuliu.storyteller.utils.Preferences;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class BaseSessionActivity extends ActionBarActivity implements OnExitRequestedListener,
        OnSessionRequestedListener, OnSetProgressBarIndeterminateRequested, OnShareStoryRequestedListener {

    private static final String TAG = "BaseSessionActivity";
    Session mSession;
    Preferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        // First of all check if the user has logged in
        mSession = ParseFacebookUtils.getSession();
        if (mSession == null || !mSession.isOpened()) {
            // There is some problem and the session of the user is not available
            // Go back to the login screen
            Log.e(TAG, "The user is in the main activity but the session has not opened. Going back to the login screen");
            backToLoginActivity();
        }

        // Set the preferences
        mPreferences = Preferences.SingletonHolder.INSTANCE;
        if (!mPreferences.hasBeenInitialized()) {
            Log.v(TAG, "The preferences has not been initialized");
            mPreferences.initialize(BaseSessionActivity.this);
        }

        // Log the user name
        Log.v(TAG, mPreferences.getString(Preferences.StringId.PARSE_USER_NAME));
    }

    void backToLoginActivity() {
        Intent backToLoginActivityIntent = new Intent(BaseSessionActivity.this, LoginActivity.class);
        // Because we are going back to the login activity, the back stack must be clean
        backToLoginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(backToLoginActivityIntent);
        finish();
    }

    @Override
    public void requestExit() {
        // Close the actual session if it has not been closed before
        if (mSession != null && mSession.isOpened()) {
            mSession.closeAndClearTokenInformation();
        }
        ParseUser.logOut();

        // Remove database
        MainDatabase.deleteTablesContent();
        backToLoginActivity();
    }

    @Override
    public Session requestSession() {
        return mSession;
    }

    @Override
    public void setProgressBar(boolean setProgressBarIndeterminate) {
        setProgressBarIndeterminate(setProgressBarIndeterminate);
    }

    @Override
    public void requestShareStory(Story story) {
        Log.d(TAG, "Story to be shared " + story.toString());
    }
}
