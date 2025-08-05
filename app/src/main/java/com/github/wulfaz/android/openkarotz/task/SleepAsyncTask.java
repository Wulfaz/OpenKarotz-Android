/*
 * OpenKarotz-Android
 * http://github.com/hobbe/OpenKarotz-Android
 *
 * Copyright (c) 2014 Olivier Bagot (http://github.com/hobbe)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 * http://opensource.org/licenses/MIT
 *
 */

package com.github.wulfaz.android.openkarotz.task;

import java.io.IOException;

import android.app.Activity;
import android.util.Log;

import com.github.wulfaz.android.openkarotz.karotz.Karotz;

/**
 * Task to put Karotz to sleep in the background.
 */
public abstract class SleepAsyncTask extends KarotzAsyncTask {

    /**
     * Initialize a new task.
     * 
     * @param activity the calling activity
     */
    public SleepAsyncTask(Activity activity) {
        super(activity);
    }

    /**
     * This tasks returns {@code Boolean#TRUE true} if action was successful, else {@code Boolean#FALSE false}.
     */
    @Override
    protected Boolean doInBackground(Object... params) {

        try {
            return Boolean.valueOf(Karotz.getInstance().sleep());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Cannot put Karotz to sleep: " + e.getMessage(), e);
            return Boolean.FALSE;
        }
    }


    // Log tag
    private static final String LOG_TAG = SleepAsyncTask.class.getSimpleName();
}
