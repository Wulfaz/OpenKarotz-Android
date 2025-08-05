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
 * Response model for ears mode API calls.
 */
public class EarsModeResponse extends KarotzResponse {
    
    @SerializedName("disabled")
    private String disabled;
    
    // Constructors
    public EarsModeResponse() {}
    
    public EarsModeResponse(String returnCode, String message, String disabled) {
        super(returnCode, message);
        this.disabled = disabled;
    }
    
    // Getters and Setters
    public String getDisabled() {
        return disabled;
    }
    
    public void setDisabled(String disabled) {
        this.disabled = disabled;
    }
    
    // Helper methods
    public boolean isEarsEnabled() {
        return "0".equals(disabled);
    }
    
    public boolean isEarsDisabled() {
        return "1".equals(disabled);
    }
    
    @Override
    public String toString() {
        return "EarsModeResponse{" +
                "disabled='" + disabled + '\'' +
                ", returnCode='" + getReturnCode() + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
