package com.jiahaoliuliu.storyteller;

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
        mFacebookLogoutButton.setOnClickListener(onclickListener);
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
