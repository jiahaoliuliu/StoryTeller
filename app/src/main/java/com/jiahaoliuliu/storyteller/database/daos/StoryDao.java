package com.jiahaoliuliu.storyteller.database.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jiahaoliuliu.storyteller.database.MainDatabase;
import com.jiahaoliuliu.storyteller.database.MainDatabase.TableStory;
import com.jiahaoliuliu.storyteller.model.Story;

import java.sql.SQLException;

/**
 * Created by jliu on 23/09/14.
 */
public class StoryDao {
    private static final String TAG = "StoryDao";

    private SQLiteDatabase mDatabase;

    public StoryDao() {
        mDatabase = MainDatabase.getDbHelper().getWritableDatabase();
    }

    /**
     * Query all the stories in the database
     * @return The cursor which contains all the stories of the database
     */
    public Cursor queryAllStories() {
        return mDatabase.query(TableStory.TABLE_NAME, null, null, null, null, null, null);
    }


    public boolean insertOrUpdateStory(Story story) {
        if (story == null) {
            throw new IllegalArgumentException("Trying to insert or update a story while the story is null");
        }
        Log.v(TAG, "Trying to insert or update the story");

        boolean result = false;
        mDatabase.beginTransaction();
        try {
            if (existStory(story.get_id())) {
                result = (updateStory(story) > 0);
            } else {
                result = insertStory(story);
            }

            mDatabase.setTransactionSuccessful();
        } catch (SQLException sqlException) {
            Log.e(TAG, "Error inserting the story into the database", sqlException);
        } finally {
            mDatabase.endTransaction();
            return result;
        }
    }

    private boolean existStory(String storyId) {
        Log.v(TAG, "Checking if the story exists");
        Cursor cursor = mDatabase.query(TableStory.TABLE_NAME, null, TableStory._ID +
        "=?", new String[] {storyId}, null, null, null, null);
        return (cursor != null && cursor.getCount() > 0);
    }

    /**
     * Insert one story inside of the database
     * @param story The story to be inserted
     * @return True if the story was correctly inserted. False otherwise
     */
    private boolean insertStory(Story story) throws SQLException {
        ContentValues contentValues = createContentValues(story);
        return (mDatabase.insertOrThrow(TableStory.TABLE_NAME, null, contentValues) > 1);
    }

    private int updateStory(Story story) throws SQLException {
        ContentValues contentValues = createContentValues(story);
        return (mDatabase.update(TableStory.TABLE_NAME, contentValues, TableStory._ID + "=?",
                new String[] {story.get_id()}));
    }

    private ContentValues createContentValues(Story story) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableStory._ID, story.get_id());
        contentValues.put(TableStory.TITLE, story.getTitle());
        contentValues.put(TableStory.CONTENT, story.getContent());
        return contentValues;
    }
}
