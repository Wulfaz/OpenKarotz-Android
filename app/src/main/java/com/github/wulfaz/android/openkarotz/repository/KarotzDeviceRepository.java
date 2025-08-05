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

package com.github.wulfaz.android.openkarotz.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.github.wulfaz.android.openkarotz.database.AppDatabase;
import com.github.wulfaz.android.openkarotz.database.KarotzDevice;
import com.github.wulfaz.android.openkarotz.database.KarotzDeviceDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Repository for managing Karotz device data.
 */
public class KarotzDeviceRepository {
    
    private static final String LOG_TAG = "KarotzDeviceRepository";
    
    private final KarotzDeviceDao deviceDao;
    private final ExecutorService executor;
    
    private static KarotzDeviceRepository INSTANCE;
    
    private KarotzDeviceRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        deviceDao = database.karotzDeviceDao();
        executor = Executors.newFixedThreadPool(4);
    }
    
    public static KarotzDeviceRepository getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (KarotzDeviceRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new KarotzDeviceRepository(context);
                }
            }
        }
        return INSTANCE;
    }
    
    // LiveData methods for UI observing
    public LiveData<List<KarotzDevice>> getAllDevices() {
        return deviceDao.getAllDevices();
    }
    
    public LiveData<KarotzDevice> getDeviceById(long id) {
        return deviceDao.getDeviceById(id);
    }
    
    public LiveData<KarotzDevice> getDefaultDevice() {
        return deviceDao.getDefaultDevice();
    }
    
    public LiveData<List<KarotzDevice>> getOnlineDevices() {
        return deviceDao.getOnlineDevices();
    }
    
    // Async operations
    public void insertDevice(KarotzDevice device, OnDeviceInsertedCallback callback) {
        executor.execute(() -> {
            try {
                long id = deviceDao.insertDevice(device);
                device.setId(id);
                Log.d(LOG_TAG, "Device inserted with ID: " + id);
                if (callback != null) {
                    callback.onDeviceInserted(device);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error inserting device", e);
                if (callback != null) {
                    callback.onError(e);
                }
            }
        });
    }
    
    public void updateDevice(KarotzDevice device) {
        executor.execute(() -> {
            try {
                deviceDao.updateDevice(device);
                Log.d(LOG_TAG, "Device updated: " + device.getName());
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error updating device", e);
            }
        });
    }
    
    public void deleteDevice(KarotzDevice device) {
        executor.execute(() -> {
            try {
                deviceDao.deleteDevice(device);
                Log.d(LOG_TAG, "Device deleted: " + device.getName());
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error deleting device", e);
            }
        });
    }
    
    public void setAsDefault(long deviceId) {
        executor.execute(() -> {
            try {
                deviceDao.clearAllDefaults();
                deviceDao.setAsDefault(deviceId);
                Log.d(LOG_TAG, "Device set as default: " + deviceId);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error setting device as default", e);
            }
        });
    }
    
    public void updateOnlineStatus(long deviceId, boolean isOnline) {
        executor.execute(() -> {
            try {
                long currentTime = System.currentTimeMillis();
                deviceDao.updateOnlineStatus(deviceId, isOnline, currentTime);
                Log.d(LOG_TAG, "Online status updated for device " + deviceId + ": " + isOnline);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error updating online status", e);
            }
        });
    }
    
    public void updateVersion(long deviceId, String version) {
        executor.execute(() -> {
            try {
                deviceDao.updateVersion(deviceId, version);
                Log.d(LOG_TAG, "Version updated for device " + deviceId + ": " + version);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error updating version", e);
            }
        });
    }
    
    // Synchronous methods for specific use cases
    public KarotzDevice getDefaultDeviceSync() {
        return deviceDao.getDefaultDeviceSync();
    }
    
    public KarotzDevice getDeviceByIdSync(long id) {
        return deviceDao.getDeviceByIdSync(id);
    }
    
    public List<KarotzDevice> getAllDevicesSync() {
        return deviceDao.getAllDevicesSync();
    }
    
    public int getDeviceCount() {
        return deviceDao.getDeviceCount();
    }
    
    // Callback interfaces
    public interface OnDeviceInsertedCallback {
        void onDeviceInserted(KarotzDevice device);
        void onError(Exception error);
    }
}
