package com.jiahaoliuliu.storyteller.maincontent;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jiahaoliuliu.storyteller.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftFragment extends Fragment {
    private static final String TAG = "LeftFragment";

    private EditText titleEditText;
    private EditText contentEditText;
    private InputMethodManager mImm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_left, container, false);
        titleEditText = (EditText) rootView.findViewById(R.id.title_edit_text);
        contentEditText = (EditText) rootView.findViewById(R.id.content_edit_text);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set the focus on the title edit text and show the keyboard
        titleEditText.requestFocus();
        mImm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Clear focus and hide the soft keyboard
        titleEditText.clearFocus();
        contentEditText.clearFocus();
        mImm.hideSoftInputFromWindow(contentEditText.getWindowToken(), 0);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mImm = null;
    }

    public void createStory() {
        // TODO: Implement this
        Log.v(TAG, "Create the story");
    }

    public void cancelStory() {
        // TODO: Implement this
        Log.v(TAG, "Cancel the story");
    }
}