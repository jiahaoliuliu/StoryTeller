package com.jiahaoliuliu.storyteller;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by jliu on 20/09/14.
 */
public class MainApplication extends Application {

    private static MainApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "mUlUK6fANWd1MIw6JvpUQ33C2lEY0fOJtrh9e7lO", "bW0Englla8GwE6VxvXlkq1oC3xBSSk8rsLsvwRPf");

        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
        sInstance = this;
    }

    /**
     * Get the instance of the global application object
     * @return The instance of the Main Application
     */
    public static MainApplication getsInstance() {
        return sInstance;
    }
}
