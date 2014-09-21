package com.jiahaoliuliu.storyteller.model;

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

    public Story(String title, String content) {
        this.title = title;
        this.content = content;
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

        if (content != null ? !content.equals(story.content) : story.content != null) return false;
        if (title != null ? !title.equals(story.title) : story.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Story{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
