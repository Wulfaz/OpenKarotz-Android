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

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.wulfaz.android.openkarotz.R;
import com.github.wulfaz.android.openkarotz.adapter.KarotzDeviceAdapter;
import com.github.wulfaz.android.openkarotz.database.KarotzDevice;
import com.github.wulfaz.android.openkarotz.viewmodel.DeviceManagementViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Activity for managing Karotz devices (list, add, edit, delete).
 */
public class DeviceManagementActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_DEVICE = 1;
    private static final int REQUEST_EDIT_DEVICE = 2;

    private DeviceManagementViewModel viewModel;
    private KarotzDeviceAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAddDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_management);

        setupToolbar();
        setupViews();
        setupViewModel();
        setupRecyclerView();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Manage Devices");
        }
    }

    private void setupViews() {
        recyclerView = findViewById(R.id.recycler_view_devices);
        fabAddDevice = findViewById(R.id.fab_add_device);

        fabAddDevice.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditDeviceActivity.class);
            startActivityForResult(intent, REQUEST_ADD_DEVICE);
        });
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(DeviceManagementViewModel.class);
        
        viewModel.getAllDevices().observe(this, devices -> {
            adapter.setDevices(devices);
        });
    }

    private void setupRecyclerView() {
        adapter = new KarotzDeviceAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnDeviceClickListener(device -> {
            Intent intent = new Intent(this, AddEditDeviceActivity.class);
            intent.putExtra("device_id", device.getId());
            startActivityForResult(intent, REQUEST_EDIT_DEVICE);
        });

        adapter.setOnDeviceLongClickListener(device -> {
            showDeviceOptionsDialog(device);
        });
    }

    private void showDeviceOptionsDialog(KarotzDevice device) {
        String[] options = {"Set as Default", "Edit", "Delete"};
        
        new AlertDialog.Builder(this)
                .setTitle(device.getName())
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0: // Set as default
                            viewModel.setAsDefault(device.getId());
                            Toast.makeText(this, device.getName() + " set as default", Toast.LENGTH_SHORT).show();
                            break;
                        case 1: // Edit
                            Intent intent = new Intent(this, AddEditDeviceActivity.class);
                            intent.putExtra("device_id", device.getId());
                            startActivityForResult(intent, REQUEST_EDIT_DEVICE);
                            break;
                        case 2: // Delete
                            showDeleteConfirmationDialog(device);
                            break;
                    }
                })
                .show();
    }

    private void showDeleteConfirmationDialog(KarotzDevice device) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Device")
                .setMessage("Are you sure you want to delete " + device.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteDevice(device);
                    Toast.makeText(this, device.getName() + " deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_DEVICE) {
                Toast.makeText(this, "Device added successfully", Toast.LENGTH_SHORT).show();
            } else if (requestCode == REQUEST_EDIT_DEVICE) {
                Toast.makeText(this, "Device updated successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.action_scan_devices) {
            // TODO: Implement device discovery
            Toast.makeText(this, "Device scanning not yet implemented", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
