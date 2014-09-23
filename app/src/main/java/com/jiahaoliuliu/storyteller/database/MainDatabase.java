package com.jiahaoliuliu.storyteller.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jiahaoliuliu.storyteller.MainApplication;

/**
 * The main database. This class contains the basic data of each tables, which allow the
 * coder to create relations between the tables easily.
 *
 * It also contains the main variable data such as the version of the database or the name
 * of the database. There should be one class like this per each database.
 * Created by jliu on 22/09/14.
 */
public class MainDatabase {

    private static final String TAG = "MainDatabase";

    /**
     * The name of this database
     */
    public static final String DB_NAME = "MainDatabase";

    /**
     * The actual version of the database.
     */
    public static final int VERSION = 1;

    private static class SingletonHolder {
        private static final OpenDbHelper INSTANCE = new OpenDbHelper(MainApplication.getsInstance());
    }

    public static OpenDbHelper getDbHelper() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * The table story, which contains the title and the content of each one of stories.
     * It saves one story per row
     */
    public interface TableStory {
        /**
         * The name of the table
         */
        public static final String TABLE_NAME = "Story";

        /**
         * The unique id of the story. This data is retrieved from parse, which ensures that
         * the combination of this id and the class name is unique across the app.
         */
        public static final String _ID = "Id";

        /**
         * The title of the story.
         */
        public static final String TITLE = "Title";

        /**
         * The content of the story
         */
        public static final String CONTENT = "Content";

        public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "( " +
                _ID + " TEXT PRIMARY KEY NOT NULL, " +
                TITLE + " TEXT NOT NULL, " +
                CONTENT + " TEXT NOT NULL " +
                ");";
        /**
         * The list of columns of this table. Note that the column id is not included because
         * this list is going to be use used along with the SimpleCursorAdapter, which maps
         * each column with the text or image to be shown. Since the id is not going to be shown
         * there is not sense to include its columns in the list
         */
        public static final String[] COLUMNS = {TITLE, CONTENT};

        public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static void deleteTablesContent() {
        SQLiteDatabase sqLiteDatabase = SingletonHolder.INSTANCE.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from " + TableStory.TABLE_NAME);
    }

    public static class OpenDbHelper extends SQLiteOpenHelper {
        private OpenDbHelper(Context context) {
            super(context, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            createTablesIfNeeded(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop actual tables
            db.execSQL(TableStory.DROP);

            // Create all the dat again
            onCreate(db);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            // Create the tables if they not exists
            createTablesIfNeeded(db);
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

        private void createTablesIfNeeded(SQLiteDatabase db) {
            db.execSQL(TableStory.CREATE);
        }
    }
}