package com.jiahaoliuliu.storyteller.database.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jiahaoliuliu.storyteller.database.MainDatabase;
import com.jiahaoliuliu.storyteller.database.MainDatabase.TableStory;
import com.jiahaoliuliu.storyteller.model.Story;

/**
 * Created by jliu on 23/09/14.
 */
public class StoryDao {

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


    /**
     * Insert one story inside of the database
     * @param story The story to be inserted
     * @return True if the story was correctly inserted. False otherwise
     */
    public boolean insertStory(Story story) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableStory._ID, story.get_id());
        contentValues.put(TableStory.TITLE, story.getTitle());
        contentValues.put(TableStory.CONTENT, story.getContent());
        return (mDatabase.insertOrThrow(TableStory.TABLE_NAME, null, contentValues) > 1);
    }
}
