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

package com.github.wulfaz.android.openkarotz.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Response model for status API calls.
 */
public class StatusResponse extends KarotzResponse {
    
    @SerializedName("sleeping")
    private String sleeping;
    
    @SerializedName("left_ear")
    private String leftEar;
    
    @SerializedName("right_ear")
    private String rightEar;
    
    @SerializedName("ear_mode")
    private String earMode;
    
    @SerializedName("color")
    private String color;
    
    @SerializedName("pulse")
    private String pulse;
    
    // Constructors
    public StatusResponse() {}
    
    // Getters and Setters
    public String getSleeping() {
        return sleeping;
    }
    
    public void setSleeping(String sleeping) {
        this.sleeping = sleeping;
    }
    
    public String getLeftEar() {
        return leftEar;
    }
    
    public void setLeftEar(String leftEar) {
        this.leftEar = leftEar;
    }
    
    public String getRightEar() {
        return rightEar;
    }
    
    public void setRightEar(String rightEar) {
        this.rightEar = rightEar;
    }
    
    public String getEarMode() {
        return earMode;
    }
    
    public void setEarMode(String earMode) {
        this.earMode = earMode;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getPulse() {
        return pulse;
    }
    
    public void setPulse(String pulse) {
        this.pulse = pulse;
    }
    
    // Helper methods
    public boolean isSleeping() {
        return "1".equals(sleeping);
    }
    
    public boolean isAwake() {
        return "0".equals(sleeping);
    }
    
    public boolean areEarsEnabled() {
        return "0".equals(earMode);
    }
    
    public int getLeftEarInt() {
        try {
            return Integer.parseInt(leftEar);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public int getRightEarInt() {
        try {
            return Integer.parseInt(rightEar);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return "StatusResponse{" +
                "sleeping='" + sleeping + '\'' +
                ", leftEar='" + leftEar + '\'' +
                ", rightEar='" + rightEar + '\'' +
                ", earMode='" + earMode + '\'' +
                ", color='" + color + '\'' +
                ", pulse='" + pulse + '\'' +
                ", returnCode='" + getReturnCode() + '\'' +
                '}';
    }
}
