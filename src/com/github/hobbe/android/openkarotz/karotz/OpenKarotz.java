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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.util.Log;

import com.github.wulfaz.android.openkarotz.network.OpenKarotzApi;
import com.github.wulfaz.android.openkarotz.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * OpenKarotz implementation.
 */
public class OpenKarotz implements IKarotz {

    /**
     * Initialize a new OpenKarotz instance.
     * @param hostname the hostname or IP
     */
    public OpenKarotz(String hostname) {

        this.hostname = hostname;

        this.api = ServiceGenerator.createService(OpenKarotzApi.class, "http://" + hostname + ":9123");
    }

    @Override
    public EarPosition[] ears(EarPosition left, EarPosition right) throws IOException {

        // Default position
        EarPosition[] newPositions = new EarPosition[] {
                EarPosition.POSITION_1, EarPosition.POSITION_1
        };

        // Current position, if available
        if (state != null) {
            newPositions = new EarPosition[] {
                    state.getLeftEarPosition(), state.getRightEarPosition()
            };
        }

        Call<EarsResponse> call = api.moveEars(left.toIntValue(), right.toIntValue(), 1);
        call.enqueue(new Callback<EarsResponse>() {
            @Override
            public void onResponse(Call<EarsResponse> call, Response<EarsResponse> response) {
                EarsResponse earsResponse = response.body();
                if (earsResponse != null && earsResponse.isSuccess()) {
                    newPositions[0] = EarPosition.fromIntValue(earsResponse.getLeftEarPositionInt());
                    newPositions[1] = EarPosition.fromIntValue(earsResponse.getRightEarPositionInt());
                } else {
                    Log.e(LOG_TAG, "API Error: " + (earsResponse != null ? earsResponse.getMessage() : "Unknown error"));
                }
            }

            @Override
            public void onFailure(Call<EarsResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Cannot move Karotz ears: " + t.getMessage(), t);
            }
        });

        if (state != null) {
            state.setLeftEarPosition(newPositions[0]);
            state.setRightEarPosition(newPositions[1]);
        }

        return newPositions;
    }

    @Override
    public EarMode earsMode(EarMode mode) throws IOException {
        EarMode currentMode = getEarMode();
        if (currentMode == mode) {
            // No change
            Log.d(LOG_TAG, "No change in ear mode");
            return mode;
        }

        Call<EarsModeResponse> call = api.setEarsMode(mode.isEnabled() ? 0 : 1);
        call.enqueue(new Callback<EarsModeResponse>() {
            @Override
            public void onResponse(Call<EarsModeResponse> call, Response<EarsModeResponse> response) {
                EarsModeResponse earsModeResponse = response.body();
                if (earsModeResponse != null && earsModeResponse.isSuccess()) {
                    EarMode newMode = earsModeResponse.isEarsEnabled() ? EarMode.ENABLED : EarMode.DISABLED;
                    state.setEarMode(newMode);
                } else {
                    Log.e(LOG_TAG, "API Error: " + (earsModeResponse != null ? earsModeResponse.getMessage() : "Unknown error"));
                }
            }

            @Override
            public void onFailure(Call<EarsModeResponse> call, Throwable t) {
                Log.e(LOG_TAG, "Cannot en/disable Karotz ears: " + t.getMessage(), t);
            }
        });

        return currentMode;
    }

    @Override
    public EarPosition[] earsRandom() throws IOException {

        // Default position
        EarPosition[] newPositions = new EarPosition[] {
                EarPosition.POSITION_1, EarPosition.POSITION_1
        };

        // Current position, if available
        if (state != null) {
            newPositions = new EarPosition[] {
                    state.getLeftEarPosition(), state.getRightEarPosition()
            };
        }

        URL url = new URL(api, CGI_BIN + "/ears_random");
        Log.d(LOG_TAG, url.toString());

        String result = NetUtils.downloadUrl(url);
        Log.d(LOG_TAG, result);

        // Answer: {"left":"0","right":"0","return":"0"}
        // Answer: {"return":"1","msg":"Unable to perform action, rabbit is sleeping."}
        // Answer: {"return":"1","msg":"Unable to perform action, ears disabled."}
        try {
            JSONObject json = new JSONObject(result);
            boolean ok = "0".equals(json.getString("return"));

            if (ok) {
                newPositions[0] = EarPosition.fromIntValue(Integer.valueOf(json.getString("left")).intValue());
                newPositions[1] = EarPosition.fromIntValue(Integer.valueOf(json.getString("right")).intValue());
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot put Karotz ears in random position: " + e.getMessage(), e);
        }

        if (state != null) {
            state.setLeftEarPosition(newPositions[0]);
            state.setRightEarPosition(newPositions[1]);
        }

        return newPositions;
    }

    @Override
    public void earsReset() throws IOException {
        URL url = new URL(api, CGI_BIN + "/ears_reset");
        Log.d(LOG_TAG, url.toString());

        String result = NetUtils.downloadUrl(url);
        Log.d(LOG_TAG, result);

        // Answer: {"return":"0"}
        // Answer: {"return":"1","msg":"Unable to perform action, rabbit is sleeping."}
        // Answer: {"return":"1","msg":"Unable to perform action, ears disabled."}
        try {
            JSONObject json = new JSONObject(result);
            boolean ok = "0".equals(json.getString("return"));

            if (ok) {
                if (state != null) {
                    state.setLeftEarPosition(EarPosition.POSITION_1);
                    state.setRightEarPosition(EarPosition.POSITION_1);
                }
                return;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot reset Karotz ears: " + e.getMessage(), e);
        }
    }

    @Override
    public int getColor() throws IOException {
        if (state == null) {
            status();
        }
        return state.getLedColor();
    }

    @Override
    public EarMode getEarMode() throws IOException {
        if (isOffline()) {
            // Re-check state
            status();
        }
        return state.getEarMode();
    }

    @Override
    public EarPosition[] getEarPositions() throws IOException {
        if (isOffline()) {
            // Re-check state
            status();
        }

        // Default position
        EarPosition[] positions = new EarPosition[] {
                EarPosition.POSITION_1, EarPosition.POSITION_1
        };

        if (state != null) {
            positions = new EarPosition[] {
                    state.getLeftEarPosition(), state.getRightEarPosition()
            };
        }

        return positions;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public KarotzStatus getStatus() throws IOException {
        if (isOffline()) {
            // Re-check state
            status();
        }
        return state.getStatus();
    }

    @Override
    public String getVersion() throws IOException {
        if (isOffline()) {
            // Re-check state
            status();
        }
        return state.getVersion();
    }

    @Override
    public boolean isPulsing() throws IOException {
        if (isOffline()) {
            // Re-check state
            status();
        }
        return state.isPulsing();
    }

    @Override
    public void led(int color, boolean pulse) throws IOException {
        int rgb = color & 0x00FFFFFF;

        if (pulse == state.isPulsing() && rgb == state.getLedColor()) {
            // No change
            return;
        }

        String c = toColorCode(rgb);

        URL url = new URL(api, CGI_BIN + "/leds?color=" + c + (pulse ? "&pulse=1" : ""));
        Log.d(LOG_TAG, url.toString());

        String result = NetUtils.downloadUrl(url);
        Log.d(LOG_TAG, result);

        // Answer: {"color":"0000FF","secondary_color":"000000","pulse":"0","no_memory":"0","speed":"700","return":"0"}
        // Answer: {"return":"1","msg":"Unable to perform action, rabbit is sleeping."}
        try {
            JSONObject json = new JSONObject(result);
            boolean ok = "0".equals(json.getString("return"));

            if (ok) {
                state.setLedColor(Color.parseColor("#" + json.getString("color")));
                state.setPulsing("1".equals(json.getString("pulse")));
                return;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot change LED on Karotz: " + e.getMessage(), e);
        }

        // Not OK, set back to previous values
        state.setLedColor(rgb);
        state.setPulsing(pulse);
    }

    @Override
    public boolean sleep() throws IOException {
        if (isSleeping()) {
            // No change
            Log.d(LOG_TAG, "Already sleeping, no need to go to sleep");
            return true;
        }

        URL url = new URL(api, CGI_BIN + "/sleep");
        Log.d(LOG_TAG, url.toString());

        String result = NetUtils.downloadUrl(url);
        Log.d(LOG_TAG, result);

        // Answer: {"return":"0"}
        // Answer: {"return":"1","msg":"Unable to perform action, rabbit is already sleeping."}
        try {
            JSONObject json = new JSONObject(result);
            state.setStatus("0".equals(json.getString("return")) ? KarotzStatus.SLEEPING : KarotzStatus.AWAKE);
            return true;
        } catch (JSONException e) {
            state.setStatus(KarotzStatus.UNKNOWN);
            return false;
        }

    }

    @Override
    public boolean sound(String soundUrl) throws IOException {
        if (soundUrl == null || soundUrl.length() <= 0) {
            return true;
        }

        URL url = new URL(api, CGI_BIN + "/sound?url=" + soundUrl);
        Log.d(LOG_TAG, url.toString());

        String result = NetUtils.downloadUrl(url);
        Log.d(LOG_TAG, result);

        // Answer: {"return":"0"}
        // Answer: {"return":"1","msg":"Unable to perform action, rabbit is sleeping."}
        try {
            JSONObject json = new JSONObject(result);
            boolean ok = "0".equals(json.getString("return"));

            if (ok) {
                Log.i(LOG_TAG, "Karotz is playing sound");
                return true;
            }
            Log.e(LOG_TAG, "Karotz cannot play the sound");
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot make Karotz play a sound: " + e.getMessage(), e);
        }

        return false;
    }

    @Override
    public boolean soundControl(SoundControlCommand command) throws IOException {
        URL url = new URL(api, CGI_BIN + "/sound_control?cmd=" + command.toString());
        Log.d(LOG_TAG, url.toString());

        String result = NetUtils.downloadUrl(url);
        Log.d(LOG_TAG, result);

        // Answer: {"return":"0"}
        // Answer: {"return":"1","msg":"No sound currently playing."}
        // Answer: {"return":"1","msg":"Unable to perform action, rabbit is already sleeping."}
        try {
            JSONObject json = new JSONObject(result);
            return ("0".equals(json.getString("return")));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot call sound control on Karotz: " + e.getMessage(), e);
        }

        return false;
    }

    @Override
    public boolean wakeup(boolean silent) throws IOException {
        if (isAwake()) {
            // No change
            Log.d(LOG_TAG, "Already awake, no need to wake up");
            return true;
        }

        URL url = new URL(api, CGI_BIN + "/wakeup" + (silent ? "?silent=1" : ""));
        Log.d(LOG_TAG, url.toString());

        String result = NetUtils.downloadUrl(url);
        Log.d(LOG_TAG, result);

        // Answer: {"return":"0","silent":"1"}
        try {
            JSONObject json = new JSONObject(result);
            state.setStatus("0".equals(json.getString("return")) ? KarotzStatus.AWAKE : KarotzStatus.UNKNOWN);
        } catch (JSONException e) {
            state.setStatus(KarotzStatus.UNKNOWN);
        }

        return (state.getStatus() == KarotzStatus.AWAKE);
    }

    private boolean isAwake() {
        return (state != null && state.getStatus() == KarotzStatus.AWAKE);
    }

    private boolean isOffline() {
        return (state == null || state.getStatus().isOffline());
    }

    private boolean isSleeping() {
        return (state != null && state.getStatus() == KarotzStatus.SLEEPING);
    }

    private void status() throws IOException {
        URL url = new URL(api, CGI_BIN + "/status");
        Log.d(LOG_TAG, url.toString());

        String result = NetUtils.downloadUrl(url);
        Log.d(LOG_TAG, result);

        state = new OpenKarotzState(result);
        Log.d(LOG_TAG, state.toString());
    }

    private static String toColorCode(int c) {
        String cc = Integer.toHexString(c);
        while (cc.length() < 6) {
            cc = '0' + cc;
        }
        return cc;
    }


    private final String hostname;

    private URL api = null;

    private OpenKarotzState state = null;

    private static final String PROTOCOL = "http";

    private static final String CGI_BIN = "/cgi-bin";

    private static final int PORT = 80;

    private static final String LOG_TAG = OpenKarotz.class.getSimpleName();
}
