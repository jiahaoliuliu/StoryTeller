package com.jiahaoliuliu.storyteller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

public class LoginActivity extends ActionBarActivity {

    // The follow TAG could be done using LoginActivity.class.getSimpleName(), but
    // in case we use Proguard, the name of the classes will be mapped, so the TAG
    // will be some random name, but never LoginActivity. So, instead, a simple
    // string is used.
    private static final String TAG = "LoginActivity";

    private Button mFacebookLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFacebookLoginButton = (Button) findViewById(R.id.facebook_login_button);
        mFacebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logIn(LoginActivity.this, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            Log.v(TAG, "User correctly authorized " + parseUser.getUsername());
                            // The user has authorized the login. Just go to the main activity
                            Intent startMainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(startMainActivityIntent);
                            // Finish the activity since this activity won't be seen until the user has logged out
                            // It is also a good idea to not allow the user to press back button and come back
                            // to this screen again
                            finish();
                        } else {
                            Log.e(TAG, "Error on requesting facebook authorization", e);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}
