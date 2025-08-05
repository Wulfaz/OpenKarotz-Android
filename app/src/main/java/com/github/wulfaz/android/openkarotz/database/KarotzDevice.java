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

package com.github.wulfaz.android.openkarotz.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

/**
 * Entity representing a Karotz device configuration.
 */
@Entity(tableName = "karotz_devices")
public class KarotzDevice {
    
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    @NonNull
    public String name;
    
    @NonNull
    public String hostname;
    
    public int port = 9123; // Default OpenKarotz port
    
    public boolean isDefault = false;
    
    public boolean isOnline = false;
    
    public String version;
    
    public long lastSeen;
    
    // Color customization
    public String customColor;
    
    // Constructors
    public KarotzDevice() {}
    
    public KarotzDevice(@NonNull String name, @NonNull String hostname) {
        this.name = name;
        this.hostname = hostname;
    }
    
    public KarotzDevice(@NonNull String name, @NonNull String hostname, int port) {
        this.name = name;
        this.hostname = hostname;
        this.port = port;
    }
    
    // Getters and Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    @NonNull
    public String getName() {
        return name;
    }
    
    public void setName(@NonNull String name) {
        this.name = name;
    }
    
    @NonNull
    public String getHostname() {
        return hostname;
    }
    
    public void setHostname(@NonNull String hostname) {
        this.hostname = hostname;
    }
    
    public int getPort() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public boolean isDefault() {
        return isDefault;
    }
    
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    public boolean isOnline() {
        return isOnline;
    }
    
    public void setOnline(boolean online) {
        isOnline = online;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public long getLastSeen() {
        return lastSeen;
    }
    
    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
    
    public String getCustomColor() {
        return customColor;
    }
    
    public void setCustomColor(String customColor) {
        this.customColor = customColor;
    }
    
    // Helper methods
    public String getBaseUrl() {
        return "http://" + hostname + ":" + port;
    }
    
    @Override
    public String toString() {
        return "KarotzDevice{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hostname='" + hostname + '\'' +
                ", port=" + port +
                ", isDefault=" + isDefault +
                ", isOnline=" + isOnline +
                '}';
    }
}
