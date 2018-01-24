package com.moocall.moocall.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.PhoneDetailsActivity;
import com.moocall.moocall.domain.Device;
import com.moocall.moocall.domain.Phone;
import java.util.List;

public class DevicePhoneListAdapter extends BaseAdapter {
    private Activity activity;
    private ImageView assignButton;
    private List<Device> deviceList;
    private LayoutInflater inflater;
    private PhoneDetailsActivity listener;
    private Phone phone;

    class C05831 implements OnClickListener {
        C05831() {
        }

        public void onClick(View view) {
            DevicePhoneListAdapter.this.listener.onAssignButtonClick(view);
        }
    }

    public Phone getPhone() {
        return this.phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public DevicePhoneListAdapter(Activity activity, List<Device> deviceList, PhoneDetailsActivity listener) {
        this.activity = activity;
        this.deviceList = deviceList;
        this.listener = listener;
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
            view = this.inflater.inflate(C0530R.layout.device_phone_list_row, null);
        }
        TextView deviceName = (TextView) view.findViewById(C0530R.id.deviceName);
        TextView deviceCode = (TextView) view.findViewById(C0530R.id.deviceCode);
        this.assignButton = (ImageView) view.findViewById(C0530R.id.assignButton);
        TextView phoneAssignText = (TextView) view.findViewById(C0530R.id.phoneAssignText);
        Device device = (Device) this.deviceList.get(i);
        deviceName.setText(device.getName());
        deviceCode.setText(device.getCode());
        Boolean assign = Boolean.valueOf(false);
        if (this.phone != null) {
            phoneAssignText.setVisibility(0);
            this.assignButton.setVisibility(0);
            if (this.phone.getDeviceIds() != null) {
                for (int j = 0; j < this.phone.getDeviceIds().size(); j++) {
                    if (device.getId().equals(Integer.valueOf(((Integer) this.phone.getDeviceIds().get(j)).intValue()))) {
                        assign = Boolean.valueOf(true);
                    }
                }
            }
            implementListener();
        } else {
            phoneAssignText.setVisibility(4);
            this.assignButton.setVisibility(4);
        }
        if (assign.booleanValue()) {
            this.assignButton.setImageResource(C0530R.drawable.checkbox_green_check);
        } else {
            this.assignButton.setImageResource(C0530R.drawable.checkbox_green_unchecked);
        }
        return view;
    }

    public void implementListener() {
        this.assignButton.setOnClickListener(new C05831());
    }
}
