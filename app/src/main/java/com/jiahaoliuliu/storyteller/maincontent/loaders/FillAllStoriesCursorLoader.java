package com.jiahaoliuliu.storyteller.maincontent.loaders;

import android.content.Context;
import android.database.Cursor;

import com.jiahaoliuliu.storyteller.database.StoryDataLayer;
import com.jiahaoliuliu.storyteller.utils.SimpleCursorLoader;

/**
 * Created by jliu on 23/09/14.
 */
public class FillAllStoriesCursorLoader extends SimpleCursorLoader {

    public FillAllStoriesCursorLoader(Context context) {
        super(context);
    }

    @Override
    public Cursor loadInBackground() {
        return StoryDataLayer.getInstance().getAllStories();
    }
}
