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
    private String title;

    /**
     * The key for the content of the story. The content cannot be null
     */
    public static final String CONTENT_KEY = "Content";
    private String content;

    public Story() {}

    public Story(String _id, String title, String content) {
        this._id = _id;
        this.title = title;
        this.content = content;
    }

    public Story(ParseObject parseObject) {
        // Get the id first
        _id = parseObject.getObjectId();

        String title = parseObject.getString(TITLE_KEY);
        if (TextUtils.isEmpty(title)) {
            throw new IllegalArgumentException("The parse object must has the title");
        }
        this.title = title;

        String content = parseObject.getString(CONTENT_KEY);
        if (TextUtils.isEmpty(content)) {
            throw new IllegalArgumentException("The parse object must has the content");
        }
        this.content = content;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Story story = (Story) o;

        if (_id != null ? !_id.equals(story._id) : story._id != null) return false;
        if (content != null ? !content.equals(story.content) : story.content != null) return false;
        if (title != null ? !title.equals(story.title) : story.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Story{" +
                "_id='" + _id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
