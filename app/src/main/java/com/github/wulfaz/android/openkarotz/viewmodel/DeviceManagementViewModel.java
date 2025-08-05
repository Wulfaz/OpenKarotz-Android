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

package com.github.wulfaz.android.openkarotz.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.wulfaz.android.openkarotz.database.KarotzDevice;
import com.github.wulfaz.android.openkarotz.repository.KarotzDeviceRepository;

import java.util.List;

/**
 * ViewModel for device management operations.
 */
public class DeviceManagementViewModel extends AndroidViewModel {

    private final KarotzDeviceRepository repository;
    private final LiveData<List<KarotzDevice>> allDevices;
    private final LiveData<KarotzDevice> defaultDevice;

    public DeviceManagementViewModel(@NonNull Application application) {
        super(application);
        repository = KarotzDeviceRepository.getInstance(application);
        allDevices = repository.getAllDevices();
        defaultDevice = repository.getDefaultDevice();
    }

    public LiveData<List<KarotzDevice>> getAllDevices() {
        return allDevices;
    }

    public LiveData<KarotzDevice> getDefaultDevice() {
        return defaultDevice;
    }

    public LiveData<KarotzDevice> getDeviceById(long id) {
        return repository.getDeviceById(id);
    }

    public LiveData<List<KarotzDevice>> getOnlineDevices() {
        return repository.getOnlineDevices();
    }

    public void insertDevice(KarotzDevice device) {
        repository.insertDevice(device, null);
    }

    public void updateDevice(KarotzDevice device) {
        repository.updateDevice(device);
    }

    public void deleteDevice(KarotzDevice device) {
        repository.deleteDevice(device);
    }

    public void setAsDefault(long deviceId) {
        repository.setAsDefault(deviceId);
    }

    public void updateOnlineStatus(long deviceId, boolean isOnline) {
        repository.updateOnlineStatus(deviceId, isOnline);
    }

    public void updateVersion(long deviceId, String version) {
        repository.updateVersion(deviceId, version);
    }
}
