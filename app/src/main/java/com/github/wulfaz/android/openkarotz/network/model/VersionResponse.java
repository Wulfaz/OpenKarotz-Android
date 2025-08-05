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
 * Response model for version API calls.
 */
public class VersionResponse extends KarotzResponse {
    
    @SerializedName("version")
    private String version;

    @SerializedName("patch")
    private String patch;

    @SerializedName("serial")
    private String serial;
    
    @SerializedName("karotz_id")
    private String karotzId;
    
    // Constructors
    public VersionResponse() {}
    
    public VersionResponse(String returnCode, String version, String patch, String serial, String karotzId) {
        super(returnCode, null);
        this.version = version;
        this.patch = patch;
        this.serial = serial;
        this.karotzId = karotzId;
    }
    
    // Getters and Setters
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }

    public String getPatch() {
        return patch;
    }

    public void setPatch(String patch) {
        this.patch = patch;
    }

    public String getSerial() {
        return serial;
    }
    
    public void setSerial(String serial) {
        this.serial = serial;
    }
    
    public String getKarotzId() {
        return karotzId;
    }
    
    public void setKarotzId(String karotzId) {
        this.karotzId = karotzId;
    }
    
    @Override
    public String toString() {
        return "VersionResponse{" +
                "version='" + version + '\'' +
                ", patch='" + patch + '\'' +
                ", serial='" + serial + '\'' +
                ", karotzId='" + karotzId + '\'' +
                ", returnCode='" + getReturnCode() + '\'' +
                '}';
    }
}
