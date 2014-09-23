package com.jiahaoliuliu.storyteller.utils;

/*
 * Copyright 2012 CodeSlap - Cristian Castiblanco
 * Modified by Jiahao Liu jiahaoliuliu@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Used to write apps that run on platforms prior to Android 3.0.  When running
 * on Android 3.0 or above, this implementation is still used; it does not try
 * to switch to the framework's implementation.  See the framework SDK
 * documentation for a class overview.
 *
 * This was based on the CursorLoader class
 *
 * @author cristian
 */
public abstract class SimpleCursorLoader extends AsyncTaskLoader<Cursor> {
    private Cursor mCursor;

    public SimpleCursorLoader(Context context) {
        // Loaders may be used across multiple Activities (assuming they aren't
        // bound to the LoaderManager), so NEVER hold a reference to the context
        // directly. Doing so will cause you to leak an entire Activity's context.
        // The superclass constructor will store a reference to the Application
        // Context instead, and can be retrieved with a call to getContext().
        super(context);
    }

    /* Runs on a worker thread */
    @Override
    public abstract Cursor loadInBackground();

    /* Runs on the UI thread */
    /**
     * Called when there is new data to deliver to the client. The superclass will
     * deliver it to the registered listener (i.e. the LoaderManager), which will
     * forward the results to the client through a call to onLoadFinished.
     */
    @Override
    public void deliverResult(Cursor cursor) {
        if (isReset()) {
            // The loader has been reset; ignore the result and invalidate the data.
            // This can happen when the Loader is reset while an asynchronous query
            // is working in the background. That is, when the background thread
            // finishes its work and attempts to deliver the results to the client,
            // it will see here that the Loader has been reset and discard any
            // resources associated with the new data as necessary
            releaseResource(cursor);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if (isStarted()) {
            // If the loader is in a started state, have the superclass deliver the
            // result to the client.
            super.deliverResult(cursor);
        }

        // Invalidate the old data as we don't need it anymore
        if (oldCursor != null && oldCursor != cursor) {
            releaseResource(oldCursor);
        }
    }

    /**
     * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     * <p/>
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }

        // If the method onContentchanged() of the Loader is called, it will cause
        // the next call to takeContentChanged() to return true. If this is ever
        // the case (or if the current data is null), we force a new load.
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // The Loader has been put in a stopped state, so we should attempt to
        // cancel the current load (if there is one).
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources
        releaseResource(mCursor);
        mCursor = null;
    }

    @Override
    public void onCanceled(Cursor cursor) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(cursor);

        // The load has been canceled, so we should release the resources
        releaseResource(cursor);
    }

    /**
     * Helper method to take care of releasing resources.
     */
    private void releaseResource(Cursor cursor) {
        if (cursor != null && cursor.isClosed()) {
            cursor.close();
        }
    }
}