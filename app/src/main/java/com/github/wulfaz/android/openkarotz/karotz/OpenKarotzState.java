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

package com.github.wulfaz.android.openkarotz.karotz;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.util.Log;

import com.github.wulfaz.android.openkarotz.karotz.IKarotz.EarMode;
import com.github.wulfaz.android.openkarotz.karotz.IKarotz.EarPosition;
import com.github.wulfaz.android.openkarotz.karotz.IKarotz.KarotzStatus;

/**
 * Status for OpenKarotz.
 */
public class OpenKarotzState {

    /**
     * Initialize a new status.
     */
    public OpenKarotzState() {
        // Nothing to do
    }

    /**
     * Initialize a new status from a JSON input.
     *
     * @param json the JSON string
     */
    public OpenKarotzState(String json) {
        // Answer:
        // {"version":"200","ears_disabled":"0","sleep":"0","sleep_time":"0","led_color":"0000FF","led_pulse":"1","tts_cache_size":"4","usb_free_space":"-1","karotz_free_space":"148.4M","eth_mac":"00:00:00:00:00:00","wlan_mac":"01:23:45:67:89:AB","nb_tags":"4","nb_moods":"305","nb_sounds":"14","nb_stories":"0","karotz_percent_used_space":"37","usb_percent_used_space":""}
        // {"version":"210","patch":"310","ears_disabled":"0","sleep":"1","sleep_time":"1754001236","led_color":"000000","led_pulse":"0","tts_cache_size":"1","usb_free_space":"-1","karotz_free_space":"147.3M","eth_mac":"00:00:00:00:00:00","wlan_mac":"01:23:45:67:89:AB","nb_tags":"6","nb_moods":"305","nb_sounds":"14","nb_stories":"0","karotz_percent_used_space":"37","usb_percent_used_space":"","data_dir":"/usr/openkarotz"}
        if (json != null) {
            try {
                JSONObject jo = new JSONObject(json);
                version = new IKarotz.KarotzVersion(jo.getString(KEY_VERSION), jo.getString(KEY_PATCH));
                status = ("1".equals(jo.getString(KEY_SLEEP)) ? KarotzStatus.SLEEPING : KarotzStatus.AWAKE);
                ledColor = Color.parseColor("#" + jo.getString(KEY_LED_COLOR));
                pulsing = ("1".equals(jo.getString(KEY_LED_PULSE)));
                earMode = ("1".equals(jo.getString(KEY_EARS_DISABLED)) ? EarMode.DISABLED : EarMode.ENABLED);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Cannot parse status answer: " + json);
                status = KarotzStatus.UNKNOWN;
            }

        } else {
            status = KarotzStatus.UNKNOWN;
        }
    }

    /**
     * Get the ear mode.
     *
     * @return the ear mode
     */
    public EarMode getEarMode() {
        return earMode;
    }

    /**
     * Get the LED color.
     *
     * @return the LED color
     */
    public int getLedColor() {
        return ledColor & 0x00FFFFFF;
    }

    /**
     * Get the left ear position.
     * @return the left ear position
     */
    public EarPosition getLeftEarPosition() {
        return leftEarPosition;
    }

    /**
     * Get the right ear position.
     * @return the right ear position
     */
    public EarPosition getRightEarPosition() {
        return rightEarPosition;
    }

    /**
     * Get the status.
     *
     * @return the status
     */
    public KarotzStatus getStatus() {
        return status;
    }

    /**
     * Get the version.
     *
     * @return the version
     */
    public IKarotz.KarotzVersion getVersion() {
        return version;
    }

    /**
     * Check if LED is pulsing.
     *
     * @return the {@code true} if LED is pulsing, else {@code false}
     */
    public boolean isPulsing() {
        return pulsing;
    }

    /**
     * Set the ear mode.
     *
     * @param mode the ear mode
     */
    public void setEarMode(EarMode mode) {
        this.earMode = mode;
    }

    /**
     * Set the LED color.
     *
     * @param color the color to set
     */
    public void setLedColor(int color) {
        this.ledColor = color & 0x00FFFFFF;
    }

    /**
     * Set the left ear position.
     * @param position the left ear position to set
     */
    public void setLeftEarPosition(EarPosition position) {
        this.leftEarPosition = position;
    }

    /**
     * Set the LED pulsing state.
     *
     * @param pulsing the pulsing state to set
     */
    public void setPulsing(boolean pulsing) {
        this.pulsing = pulsing;
    }

    /**
     * Set the right ear position.
     * @param position the right ear position to set
     */
    public void setRightEarPosition(EarPosition position) {
        this.rightEarPosition = position;
    }

    /**
     * Set the Karotz status.
     *
     * @param status the status to set
     */
    public void setStatus(KarotzStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String sb = "OpenKarotzState { \"version\": \"" +
                version.toString() +
                "\", \"status\": \"" +
                status.name() +
                "\", \"color\": \"" +
                Integer.toHexString(ledColor) +
                "\", \"pulse\": \"" +
                (pulsing ? "1" : "0") +
                "\", \"ears_disabled\": \"" +
                (earMode.isDisabled() ? "1" : "0") +
                "\", \"left_ear\": \"" +
                leftEarPosition.toString() +
                "\", \"right_ear\": \"" +
                rightEarPosition.toString() +
                "\" }";
        return sb;
    }


    private IKarotz.KarotzVersion version = null;

    private KarotzStatus status = KarotzStatus.UNKNOWN;

    private int ledColor = Color.GREEN;
    private boolean pulsing = true;

    private EarMode earMode = EarMode.ENABLED;
    private EarPosition leftEarPosition = EarPosition.POSITION_1;
    private EarPosition rightEarPosition = EarPosition.POSITION_1;

    private static final String KEY_VERSION = "version";

    private static final String KEY_PATCH = "patch";

    private static final String KEY_SLEEP = "sleep";

    private static final String KEY_LED_COLOR = "led_color";

    private static final String KEY_LED_PULSE = "led_pulse";

    private static final String KEY_EARS_DISABLED = "ears_disabled";

    private static final String LOG_TAG = OpenKarotzState.class.getSimpleName();
}
