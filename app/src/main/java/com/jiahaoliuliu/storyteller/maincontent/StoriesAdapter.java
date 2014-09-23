package com.jiahaoliuliu.storyteller.maincontent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.model.Story;

import java.util.List;

/**
 * Created by jliu on 21/09/14.
 */
public class StoriesAdapter extends BaseAdapter {

    private List<Story> mAllStories;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    class ViewHolder {
        TextView titleTextView;
        TextView contentTextView;
    }

    public StoriesAdapter(Context context, List<Story> allStories) {
        super();
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mAllStories = allStories;
    }

    @Override
    public int getCount() {
        return mAllStories.size();
    }

    @Override
    public Object getItem(int position) {
        return mAllStories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.story_layout, null);
            viewHolder.titleTextView = (TextView)convertView.findViewById(R.id.title_text_view);
            viewHolder.contentTextView = (TextView)convertView.findViewById(R.id.content_text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Story story = mAllStories.get(position);
        viewHolder.titleTextView.setText(story.getTitle());
        viewHolder.contentTextView.setText(story.getContent());

        return convertView;
    }
}
