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
import com.moocall.moocall.domain.Device;
import com.moocall.moocall.domain.Phone;
import com.moocall.moocall.fragment.PhonesFragment;
import com.rd.animation.ScaleAnimation;
import java.util.List;

public class PhoneListAdapter extends BaseAdapter {
    private Activity activity;
    private ImageView assignButton;
    private Device device;
    private LayoutInflater inflater;
    private PhonesFragment listener;
    private List<Phone> phoneList;

    class C05981 implements OnClickListener {
        C05981() {
        }

        public void onClick(View view) {
            PhoneListAdapter.this.listener.onAssignButtonClick(view);
        }
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public PhoneListAdapter(Activity activity, List<Phone> phoneList, PhonesFragment listener) {
        this.activity = activity;
        this.phoneList = phoneList;
        this.listener = listener;
    }

    public int getCount() {
        return this.phoneList.size();
    }

    public Object getItem(int i) {
        return this.phoneList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (this.inflater == null) {
            this.inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
        }
        if (view == null) {
            view = this.inflater.inflate(C0530R.layout.phone_list_row, null);
        }
        TextView phoneName = (TextView) view.findViewById(C0530R.id.phoneName);
        TextView phoneNumber = (TextView) view.findViewById(C0530R.id.phoneNumber);
        this.assignButton = (ImageView) view.findViewById(C0530R.id.assignButton);
        TextView phoneAssignText = (TextView) view.findViewById(C0530R.id.phoneAssignText);
        Phone phone = (Phone) this.phoneList.get(i);
        phoneName.setText(phone.getName());
        phoneNumber.setText(phone.getPhoneNumber());
        Boolean assign = Boolean.valueOf(false);
        if (this.device != null) {
            phoneAssignText.setVisibility(0);
            this.assignButton.setVisibility(0);
            if (phone.getDeviceIds() != null) {
                for (int j = 0; j < phone.getDeviceIds().size(); j++) {
                    if (this.device.getId().equals(Integer.valueOf(((Integer) phone.getDeviceIds().get(j)).intValue()))) {
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
        if (phone.getDeviceIds().size() > 0) {
            view.setAlpha(ScaleAnimation.MAX_SCALE_FACTOR);
        } else {
            view.setAlpha(ScaleAnimation.MIN_SCALE_FACTOR);
        }
        return view;
    }

    public void implementListener() {
        this.assignButton.setOnClickListener(new C05981());
    }
}
