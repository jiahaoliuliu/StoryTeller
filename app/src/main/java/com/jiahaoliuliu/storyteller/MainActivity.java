package com.jiahaoliuliu.storyteller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends BaseSessionActivity {

    private static final String TAG = "MainActivity";
    private Button mFacebookLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFacebookLogoutButton = (Button)findViewById(R.id.facebook_logout_button);
        mFacebookLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSession.closeAndClearTokenInformation();
                backToLoginActivity();
            }
        });
    }
}
