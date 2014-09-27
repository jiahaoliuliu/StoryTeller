package com.jiahaoliuliu.storyteller.maincontent;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;

import com.jiahaoliuliu.storyteller.R;

/**
 * Created by jliu on 27/09/14.
 */
public class CustomCursorAdapter extends SimpleCursorAdapter{
    private static final String TAG = "CustumCursorAdapter";

    private LayoutInflater mLayoutInflater;

    public CustomCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.story_layout, parent, false);
        bindView(view, context, cursor);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);
        final String rowId = cursor.getString(cursor.getColumnIndex("_id"));
        final ImageButton shareImageButton = (ImageButton) view.findViewById(R.id.share_image_button);
        shareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Share button clicked for the row " + rowId);
            }
        });
    }
}
