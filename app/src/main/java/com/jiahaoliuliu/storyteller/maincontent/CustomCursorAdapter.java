package com.jiahaoliuliu.storyteller.maincontent;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;

import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.interfaces.OnShareStoryRequestedListener;
import com.jiahaoliuliu.storyteller.model.Story;

/**
 * Created by jliu on 27/09/14.
 */
public class CustomCursorAdapter extends SimpleCursorAdapter{
    private static final String TAG = "CustumCursorAdapter";

    private LayoutInflater mLayoutInflater;
    private OnShareStoryRequestedListener mOnShareStoryRequestedListener;
    private Cursor mCursor;

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags,
                               OnShareStoryRequestedListener onShareStoryRequestedListener) {
        super(context, layout, c, from, to, flags);
        mLayoutInflater = LayoutInflater.from(context);
        this.mOnShareStoryRequestedListener = onShareStoryRequestedListener;
        this.mCursor = c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.story_layout, parent, false);
        bindView(view, context, cursor);
        return view;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        super.bindView(view, context, cursor);
        final ImageButton shareImageButton = (ImageButton) view.findViewById(R.id.share_image_button);
        shareImageButton.setOnClickListener(new OnItemClickListener(cursor.getPosition()));
    }

    private class OnItemClickListener implements View.OnClickListener {
        private int mPosition;

        public OnItemClickListener(int position) {
            super();
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            int originalPosition = mCursor.getPosition();
            mCursor.moveToPosition(mPosition);
            Story storyToBeShared = new Story(mCursor);
            mCursor.move(originalPosition);
            mOnShareStoryRequestedListener.requestShareStory(storyToBeShared);
        }
    }
}
