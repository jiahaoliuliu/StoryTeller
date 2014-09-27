package com.jiahaoliuliu.storyteller.maincontent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.interfaces.OnExitRequestedListener;
import com.jiahaoliuliu.storyteller.interfaces.OnSessionRequestedListener;
import com.jiahaoliuliu.storyteller.interfaces.OnSetProgressBarIndeterminateRequested;

import java.io.IOException;
import java.net.URL;

public class RightFragment extends Fragment {
    private static final String TAG = "RightFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnExitRequestedListener onExitRequestedListener;
    private OnSessionRequestedListener onSessionRequestedListener;
    private Session mSession;
    private OnSetProgressBarIndeterminateRequested onSetProgressBarIndeterminateRequested;

    // Layouts
    private ImageView mUserProfileImageView;
    private TextView mUsernameTextView;
    private Button mFacebookLogoutButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RightFragment newInstance(String param1, String param2) {
        RightFragment fragment = new RightFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public RightFragment() {
        // Required empty public constructor
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        mUserProfileImageView = (ImageView)view.findViewById(R.id.user_profile_image_view);
        mUsernameTextView = (TextView)view.findViewById(R.id.user_name_text_view);
        mFacebookLogoutButton = (Button)view.findViewById(R.id.facebook_logout_button);
        mFacebookLogoutButton.setOnClickListener(onclickListener);

        // Inflate the layout for this fragment
        return view;
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
                    try {
                        URL userImageUrl = new URL("https://graph.facebook.com/" + user.getId() + "/picture");
                        new GetProfilePictureTask().execute(new URL[]{userImageUrl});
                    } catch (Exception e) {
                        Log.e(TAG, "Error getting the user profile picture", e);
                    }
                    mUsernameTextView.setText(user.getName());
                } else {
                    Log.e(TAG, "Error on request facebook user " + response);
                }
                onSetProgressBarIndeterminateRequested.setProgressBar(false);
            }
        }).executeAsync();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private View.OnClickListener onclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.facebook_logout_button:
                    onExitRequestedListener.requestExit();
                    break;
            }
        }
    };

    private class GetProfilePictureTask extends AsyncTask<URL, Integer, Void> {
        private Bitmap mImageBitMap;

        @Override
        protected Void doInBackground(URL... urls) {
            try {
                mImageBitMap = BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
            } catch (IOException e) {
                Log.e(TAG, "Error getting the user profile picture", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mImageBitMap != null) {
                mUserProfileImageView.setImageBitmap(mImageBitMap);
            }
        }
    }
}
