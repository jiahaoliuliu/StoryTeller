package com.jiahaoliuliu.storyteller;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.facebook.Session;
import com.parse.ParseFacebookUtils;


public class BaseSessionActivity extends ActionBarActivity {

    private static final String TAG = "BaseSessionActivity";

    Session mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // First of all check if the user has logged in
        mSession = ParseFacebookUtils.getSession();
        if (mSession == null || !mSession.isOpened()) {
            // There is some problem and the session of the user is not available
            // Go back to the login screen
            Log.e(TAG, "The user is in the main activity but the session has not opened. Going back to the login screen");
            backToLoginActivity();
        }

    }

    void backToLoginActivity() {
        Intent backToLoginActivityIntent = new Intent(BaseSessionActivity.this, LoginActivity.class);
        // Because we are going back to the login activity, the back stack must be clean
        backToLoginActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(backToLoginActivityIntent);
        finish();
    }
}
