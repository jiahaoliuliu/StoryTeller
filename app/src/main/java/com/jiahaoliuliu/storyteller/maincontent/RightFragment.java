package com.jiahaoliuliu.storyteller.maincontent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.interfaces.OnExitRequestedListener;
import com.jiahaoliuliu.storyteller.interfaces.OnSessionRequestedListener;
import com.jiahaoliuliu.storyteller.interfaces.OnSetProgressBarIndeterminateRequested;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RightFragment extends ListFragment {
    private static final String TAG = "RightFragment";

    private OnExitRequestedListener onExitRequestedListener;
    private OnSessionRequestedListener onSessionRequestedListener;
    private Session mSession;
    private OnSetProgressBarIndeterminateRequested onSetProgressBarIndeterminateRequested;

    // Layouts
    private View mListHeaderView;
    private ImageView mUserProfileImageView;
    private TextView mUsernameTextView;

    private enum RightFragmentListItem {
        MY_STORIES(R.string.item_my_stories, true),
        FAVOURITES(R.string.item_favourite, false),
        LOGOUT(R.string.item_logout, true);

        private int mTitleResId;
        private boolean mIsEnabled;

        private RightFragmentListItem(int titleResId, boolean isEnabled) {
            this.mTitleResId = titleResId;
            this.mIsEnabled = isEnabled;
        }

        private int getTitleResId() {
            return mTitleResId;
        }

        private boolean isEnabled() {
            return mIsEnabled;
        }

        static List<String> getTitles(Context context) {
            if (context == null) {
                throw new NullPointerException(
                        "The context used to retrieve the list of titles cannot be null");
            }

            List<String> titles = new ArrayList<String>();
            for (RightFragmentListItem listTitle : values()) {
                titles.add(context.getString(listTitle.getTitleResId()));
            }

            return titles;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onExitRequestedListener = (OnExitRequestedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnExitRequestedListener");
        }

        try {
            onSessionRequestedListener = (OnSessionRequestedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSessionRequestedListener");
        }

        try {
            onSetProgressBarIndeterminateRequested = (OnSetProgressBarIndeterminateRequested) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnSetProgressBarIndeterminateRequested");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mListHeaderView = inflater.inflate(R.layout.fragment_right_header, null);
        mUserProfileImageView = (ImageView) mListHeaderView.findViewById(R.id.user_profile_image_view);
        mUsernameTextView = (TextView) mListHeaderView.findViewById(R.id.user_name_text_view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSession = onSessionRequestedListener.requestSession();
        // Request the user name
        onSetProgressBarIndeterminateRequested.setProgressBar(true);
        Request.newMeRequest(mSession, new Request.GraphUserCallback() {
            @Override
            public void onCompleted(GraphUser user, Response response) {
                if (user != null) {
                    Picasso.with(getActivity())
                            .load("https://graph.facebook.com/" + user.getId() + "/picture?type=large")
                            .into(mUserProfileImageView);
                    mUsernameTextView.setText(user.getName());
                } else {
                    Log.e(TAG, "Error on request facebook user " + response);
                }
                onSetProgressBarIndeterminateRequested.setProgressBar(false);
            }
        }).executeAsync();

        // The header must be added before set the adapter
        getListView().addHeaderView(mListHeaderView);

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        RightFragmentListItem.getTitles(getActivity()));
        setListAdapter(arrayAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Fix the position by discount the header count
        int rightPosition = position - getListView().getHeaderViewsCount();
        // If the user has clicked on the header or on any other place which does not exists
        // in the list, then not do anything
        if (rightPosition < 0 || rightPosition >= RightFragmentListItem.values().length) {
            return;
        }

        RightFragmentListItem itemClicked = RightFragmentListItem.values()[rightPosition];
        // If the item has not been enabled, do not do anything
        if (!itemClicked.isEnabled()) {
            return;
        }
        switch (itemClicked) {
            case MY_STORIES:
                Intent startMyStoriesActivityIntent = new Intent(getActivity(), MyStoriesActivity.class);
                getActivity().startActivity(startMyStoriesActivityIntent);
                break;
            case FAVOURITES:
                // TODO: Go to Favourites activity
                break;
            case LOGOUT:
                onExitRequestedListener.requestExit();
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
