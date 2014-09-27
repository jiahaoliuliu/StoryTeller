package com.jiahaoliuliu.storyteller.maincontent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jiahaoliuliu.storyteller.R;
import com.jiahaoliuliu.storyteller.interfaces.OnCreateStoryRequestedListener;
import com.jiahaoliuliu.storyteller.model.Story;
import com.jiahaoliuliu.storyteller.utils.Preferences;

/**
 * A simple {@link Fragment} subclass.
 */
public class LeftFragment extends Fragment {
    private static final String TAG = "LeftFragment";

    private EditText titleEditText;
    private EditText contentEditText;

    // System
    private InputMethodManager mImm;

    private OnCreateStoryRequestedListener onCreateStoryRequestedListener;

    private Preferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onCreateStoryRequestedListener = (OnCreateStoryRequestedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnCreateStoryRequestedListener");
        }
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        preferences = Preferences.SingletonHolder.INSTANCE;
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

    public boolean createStory() {
        Log.v(TAG, "Create the story");

        // Check the title
        if (TextUtils.isEmpty(titleEditText.getText())) {
            Toast.makeText(getActivity(), R.string.warning_title_empty, Toast.LENGTH_LONG).show();
            return false;
        }

        String title = titleEditText.getText().toString();

        // Check the content
        if (TextUtils.isEmpty(contentEditText.getText())) {
            Toast.makeText(getActivity(), R.string.warning_content_empty, Toast.LENGTH_LONG).show();
            return false;
        }

        String content = contentEditText.getText().toString();

        Story newStory = new Story();
        newStory.setTitle(title);
        newStory.setContent(content);
        newStory.setAuthor(preferences.getString(Preferences.StringId.PARSE_USER_NAME));
        onCreateStoryRequestedListener.requestCreateStory(newStory);
        return true;
    }

    /**
     * Special method called when the story has been successfully created.
     * This is useful to do some clean stuffs
     */
    public void onStorySuccessfulCreated() {
        titleEditText.setText("");
        contentEditText.setText("");
    }

    public void cancelStory() {
        Log.v(TAG, "Cancel the story");

        // Clear all the fields
        titleEditText.setText("");
        contentEditText.setText("");
    }
}