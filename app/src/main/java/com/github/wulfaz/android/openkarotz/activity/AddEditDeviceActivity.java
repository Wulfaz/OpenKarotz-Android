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

package com.github.wulfaz.android.openkarotz.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.github.wulfaz.android.openkarotz.R;
import com.github.wulfaz.android.openkarotz.database.KarotzDevice;
import com.github.wulfaz.android.openkarotz.viewmodel.DeviceManagementViewModel;

/**
 * Activity for adding or editing a Karotz device.
 */
public class AddEditDeviceActivity extends AppCompatActivity {

    private DeviceManagementViewModel viewModel;
    private EditText editTextName;
    private EditText editTextHost;
    private EditText editTextPort;
    private EditText editTextDescription;
    
    private boolean isEditMode = false;
    private long deviceId = -1;
    private KarotzDevice currentDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_device);

        setupToolbar();
        setupViews();
        setupViewModel();
        
        // Check if we're editing an existing device
        deviceId = getIntent().getLongExtra("device_id", -1);
        if (deviceId != -1) {
            isEditMode = true;
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Edit Device");
            }
            loadDevice();
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Add Device");
            }
        }
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupViews() {
        editTextName = findViewById(R.id.edit_text_name);
        editTextHost = findViewById(R.id.edit_text_host);
        editTextPort = findViewById(R.id.edit_text_port);
        editTextDescription = findViewById(R.id.edit_text_description);
        
        // Set default port
        editTextPort.setText("80");
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DeviceManagementViewModel.class);
    }

    private void loadDevice() {
        viewModel.getDeviceById(deviceId).observe(this, device -> {
            if (device != null) {
                currentDevice = device;
                populateFields(device);
            }
        });
    }

    private void populateFields(KarotzDevice device) {
        editTextName.setText(device.getName());
        editTextHost.setText(device.getHostname());
        editTextPort.setText(String.valueOf(device.getPort()));
        editTextDescription.setText(""); // KarotzDevice doesn't have description field
    }

    private boolean validateInput() {
        String name = editTextName.getText().toString().trim();
        String host = editTextHost.getText().toString().trim();
        String portStr = editTextPort.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(host)) {
            editTextHost.setError("Host is required");
            editTextHost.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(portStr)) {
            editTextPort.setError("Port is required");
            editTextPort.requestFocus();
            return false;
        }

        try {
            int port = Integer.parseInt(portStr);
            if (port < 1 || port > 65535) {
                editTextPort.setError("Port must be between 1 and 65535");
                editTextPort.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            editTextPort.setError("Invalid port number");
            editTextPort.requestFocus();
            return false;
        }

        return true;
    }

    private void saveDevice() {
        if (!validateInput()) {
            return;
        }

        String name = editTextName.getText().toString().trim();
        String host = editTextHost.getText().toString().trim();
        int port = Integer.parseInt(editTextPort.getText().toString().trim());
        String description = editTextDescription.getText().toString().trim();

        if (isEditMode && currentDevice != null) {
            // Update existing device
            currentDevice.setName(name);
            currentDevice.setHostname(host);
            currentDevice.setPort(port);
            // Note: KarotzDevice doesn't have description field
            viewModel.updateDevice(currentDevice);
        } else {
            // Create new device
            KarotzDevice newDevice = new KarotzDevice(name, host, port);
            viewModel.insertDevice(newDevice);
        }

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.action_save) {
            saveDevice();
            return true;
        } else if (itemId == R.id.action_test_connection) {
            testConnection();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void testConnection() {
        if (!validateInput()) {
            return;
        }

        String host = editTextHost.getText().toString().trim();
        int port = Integer.parseInt(editTextPort.getText().toString().trim());

        // TODO: Implement actual connection test
        Toast.makeText(this, "Testing connection to " + host + ":" + port + "...", Toast.LENGTH_SHORT).show();
        
        // For now, just show a placeholder message
        Toast.makeText(this, "Connection test not yet implemented", Toast.LENGTH_SHORT).show();
    }
}
