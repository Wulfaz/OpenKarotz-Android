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
 * Response model for ears movement API calls.
 */
public class EarsResponse extends KarotzResponse {
    
    @SerializedName("left")
    private String leftEarPosition;
    
    @SerializedName("right")  
    private String rightEarPosition;
    
    // Constructors
    public EarsResponse() {}
    
    public EarsResponse(String returnCode, String message, String leftEarPosition, String rightEarPosition) {
        super(returnCode, message);
        this.leftEarPosition = leftEarPosition;
        this.rightEarPosition = rightEarPosition;
    }
    
    // Getters and Setters
    public String getLeftEarPosition() {
        return leftEarPosition;
    }
    
    public void setLeftEarPosition(String leftEarPosition) {
        this.leftEarPosition = leftEarPosition;
    }
    
    public String getRightEarPosition() {
        return rightEarPosition;
    }
    
    public void setRightEarPosition(String rightEarPosition) {
        this.rightEarPosition = rightEarPosition;
    }
    
    // Helper methods
    public int getLeftEarPositionInt() {
        try {
            return Integer.parseInt(leftEarPosition);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    public int getRightEarPositionInt() {
        try {
            return Integer.parseInt(rightEarPosition);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    @Override
    public String toString() {
        return "EarsResponse{" +
                "leftEarPosition='" + leftEarPosition + '\'' +
                ", rightEarPosition='" + rightEarPosition + '\'' +
                ", returnCode='" + getReturnCode() + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
