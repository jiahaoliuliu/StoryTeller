package com.jiahaoliuliu.storyteller;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;


public class MainActivity extends BaseSessionActivity {

    private static final String TAG = "MainActivity";

    // Layouts
    private TextView mUsernameTextView;
    private Button mFacebookLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsernameTextView = (TextView) findViewById(R.id.user_name_text_view);
        mFacebookLogoutButton = (Button)findViewById(R.id.facebook_logout_button);
        mFacebookLogoutButton.setOnClickListener(onclickListener);

        // Request the user name
        setProgressBarIndeterminate(true);
        Request.newMeRequest(mSession, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    mUsernameTextView.setText(user.getName());
                } else {
                    Log.e(TAG, "Error on request facebook user " + response);
                }
                setProgressBarIndeterminate(false);
            }
        }).executeAsync();
    }

    private View.OnClickListener onclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.facebook_logout_button:
                    mSession.closeAndClearTokenInformation();
                    backToLoginActivity();
                    break;
            }
        }
    };
}
