package com.jiahaoliuliu.storyteller.maincontent;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.jiahaoliuliu.storyteller.LoginActivity;
import com.jiahaoliuliu.storyteller.R;
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
    UiLifecycleHelper uiHelper;

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

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

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
        Log.v(TAG, "Request to share the story " + story);

        // It uses the web version of the sharing dialog because there are
        // problems with the native share dialog
        Bundle params = new Bundle();
        params.putString("name", story.getTitle());
        params.putString("description", story.getContent());
        params.putString("link", "https://play.google.com/store/apps/details?id=com.jiahaoliuliu.storyteller");
        params.putString("picture", "https://raw.githubusercontent.com/jiahaoliuliu/StoryTeller/master/app/src/main/res/drawable-xxhdpi/ic_launcher.png");

        WebDialog feedDialog = (
                new WebDialog.FeedDialogBuilder(this,
                        Session.getActiveSession(),
                        params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                                           FacebookException error) {
                        if (error == null) {
                            // Show the publish status
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                Toast.makeText(BaseSessionActivity.this,
                                        R.string.publish_success, Toast.LENGTH_SHORT).show();
                            } else {
                                // User clicked the Cancel button
                                Toast.makeText(BaseSessionActivity.this,
                                        R.string.publish_cancelled, Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Toast.makeText(BaseSessionActivity.this,
                                    R.string.publish_cancelled, Toast.LENGTH_SHORT).show();
                        } else {
                            // Generic, ex: network error
                            Toast.makeText(BaseSessionActivity.this,
                                    R.string.publish_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .build();
        feedDialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Log.e("Activity", String.format("Error: %s", error.toString()));
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                Log.i("Activity", "Success!");
            }
        });
    }
}
