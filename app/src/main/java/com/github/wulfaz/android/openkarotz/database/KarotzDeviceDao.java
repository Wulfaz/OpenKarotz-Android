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

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * Data Access Object for Karotz device operations.
 */
@Dao
public interface KarotzDeviceDao {
    
    @Query("SELECT * FROM karotz_devices ORDER BY name ASC")
    LiveData<List<KarotzDevice>> getAllDevices();
    
    @Query("SELECT * FROM karotz_devices ORDER BY name ASC")
    List<KarotzDevice> getAllDevicesSync();
    
    @Query("SELECT * FROM karotz_devices WHERE id = :id")
    LiveData<KarotzDevice> getDeviceById(long id);
    
    @Query("SELECT * FROM karotz_devices WHERE id = :id")
    KarotzDevice getDeviceByIdSync(long id);
    
    @Query("SELECT * FROM karotz_devices WHERE isDefault = 1 LIMIT 1")
    LiveData<KarotzDevice> getDefaultDevice();
    
    @Query("SELECT * FROM karotz_devices WHERE isDefault = 1 LIMIT 1")
    KarotzDevice getDefaultDeviceSync();
    
    @Query("SELECT * FROM karotz_devices WHERE hostname = :hostname AND port = :port LIMIT 1")
    KarotzDevice findByHostnameAndPort(String hostname, int port);
    
    @Query("SELECT * FROM karotz_devices WHERE isOnline = 1 ORDER BY name ASC")
    LiveData<List<KarotzDevice>> getOnlineDevices();
    
    @Insert
    long insertDevice(KarotzDevice device);
    
    @Update
    void updateDevice(KarotzDevice device);
    
    @Delete
    void deleteDevice(KarotzDevice device);
    
    @Query("DELETE FROM karotz_devices WHERE id = :id")
    void deleteDeviceById(long id);
    
    @Query("UPDATE karotz_devices SET isDefault = 0")
    void clearAllDefaults();
    
    @Query("UPDATE karotz_devices SET isDefault = 1 WHERE id = :id")
    void setAsDefault(long id);
    
    @Query("UPDATE karotz_devices SET isOnline = :isOnline, lastSeen = :lastSeen WHERE id = :id")
    void updateOnlineStatus(long id, boolean isOnline, long lastSeen);
    
    @Query("UPDATE karotz_devices SET version = :version WHERE id = :id")
    void updateVersion(long id, String version);
    
    @Query("SELECT COUNT(*) FROM karotz_devices")
    int getDeviceCount();
}
