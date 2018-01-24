package com.moocall.moocall.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.domain.Device;
import java.util.List;

public class DeviceListAdapter extends BaseAdapter {
    private Activity activity;
    private List<Device> deviceList;
    private LayoutInflater inflater;

    public DeviceListAdapter(Activity activity, List<Device> deviceList) {
        this.activity = activity;
        this.deviceList = deviceList;
    }

    public int getCount() {
        return this.deviceList.size();
    }

    public Object getItem(int i) {
        return this.deviceList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (this.inflater == null) {
            this.inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
        }
        if (view == null) {
            view = this.inflater.inflate(C0530R.layout.devices_list_row, null);
        }
        TextView deviceCode = (TextView) view.findViewById(C0530R.id.deviceCode);
        TextView deviceBattery = (TextView) view.findViewById(C0530R.id.deviceBattery);
        TextView deviceLastTime = (TextView) view.findViewById(C0530R.id.deviceLastTime);
        ImageView deviceBatteryIcon = (ImageView) view.findViewById(C0530R.id.deviceBatteryIcon);
        ImageView firmwareIcon = (ImageView) view.findViewById(C0530R.id.firmwareIcon);
        TextView firmwareVersion = (TextView) view.findViewById(C0530R.id.firmwareVersion);
        Device device = (Device) this.deviceList.get(i);
        ((TextView) view.findViewById(C0530R.id.deviceName)).setText(device.getName());
        deviceCode.setText(device.getCode());
        deviceBattery.setText(device.getBattery().toString() + "%");
        deviceLastTime.setText(device.getLastTime());
        firmwareVersion.setText(device.getFwversion());
        deviceBatteryIcon.setImageResource(C0530R.drawable.battery_icon_gray);
        if (device.getBattery().intValue() > 15) {
            deviceBattery.setTextColor(view.getResources().getColor(C0530R.color.darker_gray));
        } else {
            deviceBattery.setTextColor(view.getResources().getColor(C0530R.color.orange_mh));
        }
        if (device.getFwstatus().equals(Integer.valueOf(1))) {
            firmwareIcon.setImageResource(C0530R.drawable.firmware_update);
        } else {
            firmwareIcon.setImageResource(C0530R.drawable.firmware_ok);
        }
        return view;
    }
}
