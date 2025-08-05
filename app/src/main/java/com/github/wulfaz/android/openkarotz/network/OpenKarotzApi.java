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

package com.github.wulfaz.android.openkarotz.network;

import com.github.wulfaz.android.openkarotz.network.model.EarsResponse;
import com.github.wulfaz.android.openkarotz.network.model.EarsModeResponse;
import com.github.wulfaz.android.openkarotz.network.model.KarotzResponse;
import com.github.wulfaz.android.openkarotz.network.model.StatusResponse;
import com.github.wulfaz.android.openkarotz.network.model.VersionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit API interface for OpenKarotz REST endpoints.
 */
public interface OpenKarotzApi {

    // Status and Version
    @GET("cgi-bin/status")
    Call<StatusResponse> getStatus();

    @GET("cgi-bin/version")
    Call<VersionResponse> getVersion();

    // LED Control
    @GET("cgi-bin/led")
    Call<KarotzResponse> setLedColor(
            @Query("color") String color,
            @Query("pulse") Integer pulse
    );

    @GET("cgi-bin/led")
    Call<KarotzResponse> setLedOff();

    // Ears Control
    @GET("cgi-bin/ears")
    Call<EarsResponse> moveEars(
            @Query("left") int left,
            @Query("right") int right,
            @Query("noreset") int noreset
    );

    @GET("cgi-bin/ears_random")
    Call<EarsResponse> randomEars();

    @GET("cgi-bin/ears_reset")
    Call<KarotzResponse> resetEars();

    @GET("cgi-bin/ears_mode")
    Call<EarsModeResponse> setEarsMode(@Query("disable") int disable);

    @GET("cgi-bin/ears_mode")
    Call<EarsModeResponse> getEarsMode();

    // Sound Control
    @GET("cgi-bin/sound")
    Call<KarotzResponse> playSound(@Query("url") String url);

    @GET("cgi-bin/sound")
    Call<KarotzResponse> stopSound(@Query("cmd") String cmd);

    @GET("cgi-bin/sound")
    Call<KarotzResponse> pauseSound(@Query("cmd") String cmd);

    @GET("cgi-bin/sound")
    Call<KarotzResponse> resumeSound(@Query("cmd") String cmd);

    // System Control  
    @GET("cgi-bin/sleep")
    Call<KarotzResponse> sleep();

    @GET("cgi-bin/wakeup")
    Call<KarotzResponse> wakeup();

    // TTS (Text-to-Speech) - if supported
    @GET("cgi-bin/tts")
    Call<KarotzResponse> speak(
            @Query("text") String text,
            @Query("lang") String language
    );

    // Custom API calls for extensibility
    @GET("cgi-bin/custom")
    Call<KarotzResponse> customCommand(
            @Query("cmd") String command,
            @Query("param1") String param1,
            @Query("param2") String param2
    );
}
