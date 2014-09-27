package com.jiahaoliuliu.storyteller.maincontent.loaders;

import android.content.Context;
import android.database.Cursor;

import com.jiahaoliuliu.storyteller.database.StoryDataLayer;
import com.jiahaoliuliu.storyteller.utils.SimpleCursorLoader;

/**
 * Created by jliu on 23/09/14.
 */
public class FillStoriesByAuthorCursorLoader extends SimpleCursorLoader {

    private String mAuthor;

    public FillStoriesByAuthorCursorLoader(Context context, String author) {
        super(context);
        this.mAuthor = author;
    }

    @Override
    public Cursor loadInBackground() {
        return StoryDataLayer.getInstance().getStoriesByAuthor(mAuthor);
    }
}
