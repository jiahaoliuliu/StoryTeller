package com.jiahaoliuliu.storyteller.database;

import android.database.Cursor;
import android.util.Log;

import com.jiahaoliuliu.storyteller.database.daos.StoryDao;
import com.jiahaoliuliu.storyteller.model.Story;

/**
 * Created by jliu on 23/09/14.
 */
public class StoryDataLayer {
    private static final String TAG = "StoryDataLayer";
    private StoryDao mStoryDao;

    private StoryDataLayer() {
        mStoryDao = new StoryDao();
    }

    private static class SingletonHolder {
        public static final StoryDataLayer INSTANCE = new StoryDataLayer();
    }

    public static StoryDataLayer getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Cursor getAllStories() {
        return mStoryDao.queryAllStories();
    }

    public boolean insertOrUpdateStory(Story story) {
        Log.v(TAG, "Inserting or updating the story " + story);
        return mStoryDao.insertOrUpdateStory(story);
    }
}
