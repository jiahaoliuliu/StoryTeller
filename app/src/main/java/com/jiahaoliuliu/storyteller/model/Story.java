package com.jiahaoliuliu.storyteller.model;

import android.text.TextUtils;

import com.parse.ParseObject;

/**
 * Story is a model which contains two fields: Title and content. It also contains the key of both
 * fields as public static variable
 * Created by jliu on 21/09/14.
 */
public class Story {

    /**
     * The key for the story. This will be the name of the table in the background (for now Parse)
     */
    public static final String STORY_KEY = "Story";

    /**
     * The id of the story in the Parse table. Parse ensures that the combination of this id
     * along with the name of the class will be unique across the app
     */
    private String _id;

    /**
     * The key for the title of the story. The title cannot be null
     */
    public static final String TITLE_KEY = "Title";
    private String mTitle;

    /**
     * The key for the content of the story. The content cannot be null
     */
    public static final String CONTENT_KEY = "Content";
    private String mContent;

    /**
     * The key for the author of the story. The author cannot be null
     */
    public static final String AUTHOR_KEY = "Author";
    private String mAuthor;

    public Story() {}

    public Story(String _id, String title, String content, String author) {
        this._id = _id;
        this.mTitle = title;
        this.mContent = content;
        this.mAuthor = author;
    }

    public Story(ParseObject parseObject) {
        // Get the id first
        _id = parseObject.getObjectId();
        if (TextUtils.isEmpty(_id)) {
            throw new IllegalArgumentException("The parse object must has the id");
        }

        // Title
        String title = parseObject.getString(TITLE_KEY);
        if (TextUtils.isEmpty(title)) {
            throw new IllegalArgumentException("The parse object must has the title");
        }
        this.mTitle = title;

        // Content
        String content = parseObject.getString(CONTENT_KEY);
        if (TextUtils.isEmpty(content)) {
            throw new IllegalArgumentException("The parse object must has the content");
        }
        this.mContent = content;

        // Author
        String author = parseObject.getString(AUTHOR_KEY);
        if (TextUtils.isEmpty(author)) {
            throw new IllegalArgumentException("The parse object must has the author");
        }
        this.mAuthor = author;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Story story = (Story) o;

        if (_id != null ? !_id.equals(story._id) : story._id != null) return false;
        if (mAuthor != null ? !mAuthor.equals(story.mAuthor) : story.mAuthor != null) return false;
        if (mContent != null ? !mContent.equals(story.mContent) : story.mContent != null)
            return false;
        if (mTitle != null ? !mTitle.equals(story.mTitle) : story.mTitle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mContent != null ? mContent.hashCode() : 0);
        result = 31 * result + (mAuthor != null ? mAuthor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Story{" +
                "_id='" + _id + '\'' +
                ", Title='" + mTitle + '\'' +
                ", Content='" + mContent + '\'' +
                ", Author='" + mAuthor + '\'' +
                '}';
    }
}
