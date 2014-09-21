package com.jiahaoliuliu.storyteller.maincontent;

import android.os.Bundle;
import android.util.Log;

import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.model.Story;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class MainActivity extends BaseSessionActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Trying to querying data
        ParseQuery<ParseObject> query = ParseQuery.getQuery(Story.STORY_KEY);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (parseObjects != null) {
                    Log.v(TAG, "Stories retrieved from the parse");
                    for (ParseObject parseObject: parseObjects) {
                        String title = parseObject.getString(Story.TITLE_KEY);
                        String content = parseObject.getString(Story.CONTENT_KEY);
                        Log.v(TAG, title + " " + content);
                    }
                }
            }
        });
    }

}
