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

package com.github.wulfaz.android.openkarotz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.wulfaz.android.openkarotz.R;
import com.github.wulfaz.android.openkarotz.database.KarotzDevice;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * RecyclerView adapter for displaying Karotz devices.
 */
public class KarotzDeviceAdapter extends RecyclerView.Adapter<KarotzDeviceAdapter.DeviceViewHolder> {

    private List<KarotzDevice> devices = new ArrayList<>();
    private OnDeviceClickListener clickListener;
    private OnDeviceLongClickListener longClickListener;

    public interface OnDeviceClickListener {
        void onDeviceClick(KarotzDevice device);
    }

    public interface OnDeviceLongClickListener {
        void onDeviceLongClick(KarotzDevice device);
    }

    public void setOnDeviceClickListener(OnDeviceClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnDeviceLongClickListener(OnDeviceLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_karotz_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        KarotzDevice device = devices.get(position);
        holder.bind(device);
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public void setDevices(List<KarotzDevice> devices) {
        this.devices = devices != null ? devices : new ArrayList<>();
        notifyDataSetChanged();
    }

    public KarotzDevice getDevice(int position) {
        if (position >= 0 && position < devices.size()) {
            return devices.get(position);
        }
        return null;
    }

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView hostText;
        private final TextView statusText;
        private final TextView lastSeenText;
        private final ImageView statusIcon;
        private final ImageView defaultIcon;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.text_device_name);
            hostText = itemView.findViewById(R.id.text_device_host);
            statusText = itemView.findViewById(R.id.text_device_status);
            lastSeenText = itemView.findViewById(R.id.text_last_seen);
            statusIcon = itemView.findViewById(R.id.icon_status);
            defaultIcon = itemView.findViewById(R.id.icon_default);

            itemView.setOnClickListener(v -> {
                if (clickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    clickListener.onDeviceClick(devices.get(getAdapterPosition()));
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    longClickListener.onDeviceLongClick(devices.get(getAdapterPosition()));
                    return true;
                }
                return false;
            });
        }

        public void bind(KarotzDevice device) {
            nameText.setText(device.getName());
            hostText.setText(device.getHostname() + ":" + device.getPort());

            // Status
            if (device.isOnline()) {
                statusText.setText("Online");
                statusText.setTextColor(itemView.getContext().getColor(android.R.color.holo_green_dark));
                statusIcon.setImageResource(R.drawable.ic_karotz_light);
            } else {
                statusText.setText("Offline");
                statusText.setTextColor(itemView.getContext().getColor(android.R.color.holo_red_dark));
                statusIcon.setImageResource(R.drawable.ic_karotz_dark);
            }

            // Default device indicator
            defaultIcon.setVisibility(device.isDefault() ? View.VISIBLE : View.GONE);

            // Last seen
            if (device.getLastSeen() > 0) {
                Date lastSeenDate = new Date(device.getLastSeen());
                String lastSeenStr = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                        .format(lastSeenDate);
                lastSeenText.setText("Last seen: " + lastSeenStr);
                lastSeenText.setVisibility(View.VISIBLE);
            } else {
                lastSeenText.setVisibility(View.GONE);
            }
        }
    }
}
