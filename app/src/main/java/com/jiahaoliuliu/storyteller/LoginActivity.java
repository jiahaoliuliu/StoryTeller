package com.jiahaoliuliu.storyteller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.Session;
import com.jiahaoliuliu.storyteller.maincontent.MainActivity;
import com.jiahaoliuliu.storyteller.utils.Preferences;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_login);

        // Check if the user has already authorized the from Facebook
        Session facebookSession = ParseFacebookUtils.getSession();
        if (facebookSession != null && facebookSession.isOpened()) {
            Log.v(TAG, "The user has already authorized the session.");
            goToMainActivity();
        }

        mFacebookLoginButton = (Button) findViewById(R.id.facebook_login_button);
        mFacebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the indeterminate progress bar
                setSupportProgressBarIndeterminateVisibility(true);
                // Disable the button
                mFacebookLoginButton.setEnabled(false);
                ParseFacebookUtils.logIn(LoginActivity.this, new LogInCallback() {
                    @Override
                    public void done(final ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            Log.v(TAG, "User correctly authorized " + parseUser.getUsername());

                            // Save the user name
                            Preferences preferencesInstance = Preferences.SingletonHolder.INSTANCE;
                            preferencesInstance.initialize(LoginActivity.this);
                            preferencesInstance.setString(Preferences.StringId.PARSE_USER_NAME, parseUser.getUsername());

                            if (ParseFacebookUtils.isLinked(parseUser)) {
                                Log.v(TAG, "Parse user linked correctly with facebook user. Going to main activity");
                                goToMainActivity();
                            } else {
                                ParseFacebookUtils.link(parseUser, LoginActivity.this, new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (ParseFacebookUtils.isLinked(parseUser)) {
                                            Log.v(TAG, "Parse user linked correctly with facebook user. Going to main activity");
                                            goToMainActivity();
                                        } else {
                                            // Parse user authorized but not correctly linked. Ask the user to authorize again
                                            Log.e(TAG, "Error on linking", e);
                                            Toast.makeText(LoginActivity.this, getString(R.string.error_linking), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.e(TAG, "Error on requesting facebook authorization", e);
                        }
                        // Hide the indeterminate progress bar
                        setSupportProgressBarIndeterminateVisibility(false);
                        mFacebookLoginButton.setEnabled(true);
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

    private void goToMainActivity() {
        Log.v(TAG, "Going to main activity");
        // The user has authorized the login. Just go to the main activity
        Intent startMainActivityIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(startMainActivityIntent);
        // Finish the activity since this activity won't be seen until the user has logged out
        // It is also a good idea to not allow the user to press back button and come back
        // to this screen again
        finish();
    }
}
