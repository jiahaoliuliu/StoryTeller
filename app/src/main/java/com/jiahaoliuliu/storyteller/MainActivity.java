package com.jiahaoliuliu.storyteller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.Session;
import com.parse.ParseFacebookUtils;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private Button mFacebookLogoutButton;

    private Session mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // First of all check if the user has logged in
        mSession = ParseFacebookUtils.getSession();
        if (mSession == null || !mSession.isOpened()) {
            // There is some problem and the session of the user is not available
            // Go back to the login screen
            Log.e(TAG, "The user is in the main activity but the session has not opened. Going back to the login screen");
            backToLoginActivity();
        }

        mFacebookLogoutButton = (Button)findViewById(R.id.facebook_logout_button);
        mFacebookLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSession.closeAndClearTokenInformation();
                backToLoginActivity();
            }
        });
    }

    private void backToLoginActivity() {
        Intent backToLoginActivityIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(backToLoginActivityIntent);
        finish();
    }
}
