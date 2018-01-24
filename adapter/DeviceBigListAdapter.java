package com.moocall.moocall.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.domain.Device;
import com.moocall.moocall.util.Utils;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class DeviceBigListAdapter extends BaseAdapter {
    private Activity activity;
    private List<Device> deviceList;
    private LayoutInflater inflater;

    public DeviceBigListAdapter(Activity activity, List<Device> deviceList) {
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
            view = this.inflater.inflate(C0530R.layout.device_list_big_row, null);
        }
        ImageView location = (ImageView) view.findViewById(C0530R.id.location);
        TextView deviceCode = (TextView) view.findViewById(C0530R.id.deviceCode);
        TextView deviceBattery = (TextView) view.findViewById(C0530R.id.deviceBattery);
        TextView deviceLastTime = (TextView) view.findViewById(C0530R.id.deviceLastTime);
        ImageView deviceBatteryIcon = (ImageView) view.findViewById(C0530R.id.deviceBatteryIcon);
        ImageView firmwareIcon = (ImageView) view.findViewById(C0530R.id.firmwareIcon);
        TextView firmwareVersion = (TextView) view.findViewById(C0530R.id.firmwareVersion);
        ImageView deviceIndicator = (ImageView) view.findViewById(C0530R.id.deviceIndicator);
        RelativeLayout deviceSuspendLayout = (RelativeLayout) view.findViewById(C0530R.id.deviceSuspendLayout);
        LinearLayout deviceProblemLayout = (LinearLayout) view.findViewById(C0530R.id.deviceProblemLayout);
        LinearLayout noPhonesConnectedLayout = (LinearLayout) view.findViewById(C0530R.id.noPhonesConnectedLayout);
        LinearLayout noDeviceNotificationLayout = (LinearLayout) view.findViewById(C0530R.id.noDeviceNotificationLayout);
        LinearLayout newSoftwareUpdateLayout = (LinearLayout) view.findViewById(C0530R.id.newSoftwareUpdateLayout);
        RelativeLayout deviceProblemIndicator = (RelativeLayout) view.findViewById(C0530R.id.deviceProblemIndicator);
        TextView warrantyExpires = (TextView) view.findViewById(C0530R.id.warrantyExpires);
        TextView serviceExpires = (TextView) view.findViewById(C0530R.id.serviceExpires);
        TextView warrantyText = (TextView) view.findViewById(C0530R.id.warrantyText);
        TextView warrantyText2 = (TextView) view.findViewById(C0530R.id.warrantyText2);
        TextView serviceText = (TextView) view.findViewById(C0530R.id.serviceText);
        TextView warrantyExpiresDate = (TextView) view.findViewById(C0530R.id.warrantyExpiresDate);
        TextView serviceExpiresDate = (TextView) view.findViewById(C0530R.id.serviceExpiresDate);
        LinearLayout deviceHeader = (LinearLayout) view.findViewById(C0530R.id.deviceHeader);
        TextView deviceSuspendMessage = (TextView) view.findViewById(C0530R.id.deviceSuspendMessage);
        TextView deviceSuspendMessage2 = (TextView) view.findViewById(C0530R.id.deviceSuspendMessage2);
        Device device = (Device) this.deviceList.get(i);
        ((TextView) view.findViewById(C0530R.id.deviceName)).setText(device.getName());
        location.setVisibility(8);
        deviceCode.setText(device.getCode());
        deviceBattery.setText(device.getBattery().toString() + "%");
        deviceLastTime.setText(device.getLastTime());
        firmwareVersion.setText(device.getFwversion());
        deviceBatteryIcon.setImageResource(C0530R.drawable.battery_icon_gray);
        warrantyExpires.setText(device.getWarranty());
        serviceExpires.setText(device.getRenewal());
        warrantyExpiresDate.setText(device.getWarranty());
        serviceExpiresDate.setText(device.getRenewal());
        if (device.getBattery().intValue() > 15) {
            deviceBattery.setTextColor(view.getResources().getColor(C0530R.color.darker_gray));
        } else {
            deviceBattery.setTextColor(view.getResources().getColor(C0530R.color.orange_mh));
        }
        if (device.getFwstatus().equals(Integer.valueOf(1))) {
            firmwareIcon.setImageResource(C0530R.drawable.firmware_update);
            newSoftwareUpdateLayout.setVisibility(0);
        } else {
            firmwareIcon.setImageResource(C0530R.drawable.firmware_ok);
            newSoftwareUpdateLayout.setVisibility(8);
        }
        if (device.getDeviceMessages().getWarrantyPast().booleanValue()) {
            warrantyText.setText(this.activity.getString(C0530R.string.warranty_expired));
            warrantyText2.setText(this.activity.getString(C0530R.string.warranty_expired));
        } else if (device.getDeviceMessages().getWarrantyExp().booleanValue()) {
            warrantyText.setText(this.activity.getString(C0530R.string.warranty_expires_text) + StringUtils.SPACE + Utils.getDeviceLeftTime(device.getWarranty()) + StringUtils.SPACE + this.activity.getString(C0530R.string.days));
            warrantyText2.setText(this.activity.getString(C0530R.string.warranty_expires_text) + StringUtils.SPACE + Utils.getDeviceLeftTime(device.getWarranty()) + StringUtils.SPACE + this.activity.getString(C0530R.string.days));
        } else {
            warrantyText.setText(this.activity.getString(C0530R.string.warranty_expires));
            warrantyText2.setText(this.activity.getString(C0530R.string.warranty_expires));
        }
        if (device.getDeviceMessages().getRenewalPast().booleanValue()) {
            serviceText.setText(this.activity.getString(C0530R.string.service_expired));
        } else if (device.getDeviceMessages().getRenewalExp().booleanValue()) {
            serviceText.setText(this.activity.getString(C0530R.string.service_expires_text) + StringUtils.SPACE + Utils.getDeviceLeftTime(device.getRenewal()) + StringUtils.SPACE + this.activity.getString(C0530R.string.days));
        } else {
            serviceText.setText(this.activity.getString(C0530R.string.service_expires));
        }
        if (device.getDeviceMessages().getRenewalPast().booleanValue() || device.getDeviceMessages().getRenewalExp().booleanValue() || device.getDeviceMessages().getBlocked().booleanValue()) {
            deviceProblemLayout.setVisibility(0);
            noDeviceNotificationLayout.setVisibility(8);
            deviceProblemIndicator.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.device_problem));
            deviceHeader.setBackgroundResource(C0530R.drawable.device_problem_background);
        } else {
            noDeviceNotificationLayout.setVisibility(0);
            deviceProblemLayout.setVisibility(8);
            deviceProblemIndicator.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.device_ok));
            deviceHeader.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.white));
        }
        if (device.getDeviceMessages().getBlocked().booleanValue()) {
            deviceSuspendMessage.setVisibility(0);
            deviceSuspendMessage2.setVisibility(0);
            deviceSuspendLayout.setVisibility(0);
        } else {
            deviceSuspendMessage.setVisibility(8);
            deviceSuspendMessage2.setVisibility(8);
            deviceSuspendLayout.setVisibility(8);
        }
        if (device.getPhones().size() > 0) {
            noPhonesConnectedLayout.setVisibility(8);
        } else {
            noPhonesConnectedLayout.setVisibility(0);
        }
        if (device.getInactive().booleanValue()) {
            deviceIndicator.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.device_problem));
        } else {
            deviceIndicator.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.device_ok));
        }
        return view;
    }
}
