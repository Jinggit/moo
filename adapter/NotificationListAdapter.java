package com.moocall.moocall.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.domain.Notification;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class NotificationListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Notification> notificationList;

    public NotificationListAdapter(Activity activity, List<Notification> notificationList) {
        this.activity = activity;
        this.notificationList = notificationList;
    }

    public int getCount() {
        return this.notificationList.size();
    }

    public Object getItem(int i) {
        return this.notificationList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (this.inflater == null) {
            this.inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
        }
        if (view == null) {
            view = this.inflater.inflate(C0530R.layout.notification_list_row, null);
        }
        TextView notificationDeviceName = (TextView) view.findViewById(C0530R.id.notificationDeviceName);
        TextView notificationDeviceCode = (TextView) view.findViewById(C0530R.id.notificationDeviceCode);
        TextView notificationType = (TextView) view.findViewById(C0530R.id.notificationType);
        Notification notification = (Notification) this.notificationList.get(i);
        ((TextView) view.findViewById(C0530R.id.notificationTime)).setText(notification.getCalvingTimer());
        notificationDeviceName.setText(notification.getName());
        notificationDeviceCode.setText(notification.getCode());
        String calvingText = "";
        if (notification.getMessageType() == null) {
            if (notification.getMessageTypeBull() != null) {
                if (!notification.getMessageGroup().equals(Integer.valueOf(0))) {
                    if (notification.getMessageGroup().equals(Integer.valueOf(1))) {
                        switch (notification.getMessageTypeBull().intValue()) {
                            case 2:
                                calvingText = this.activity.getResources().getString(C0530R.string.device_charging_begins);
                                break;
                            case 3:
                                calvingText = this.activity.getResources().getString(C0530R.string.turned_on);
                                break;
                            default:
                                break;
                        }
                    }
                }
                switch (notification.getMessageTypeBull().intValue()) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        calvingText = this.activity.getString(C0530R.string.cow_in_heat) + StringUtils.SPACE + notification.getTimeWithBull() + " h! " + this.activity.getString(C0530R.string.cow_id) + ":" + notification.getCowId();
                        break;
                    case 10:
                        calvingText = this.activity.getString(C0530R.string.bull_with_cow) + StringUtils.SPACE + notification.getCowId() + StringUtils.SPACE + this.activity.getString(C0530R.string.one) + " h. " + this.activity.getString(C0530R.string.high_activity) + "!";
                        break;
                    case 11:
                        calvingText = this.activity.getString(C0530R.string.bull_with_cow) + StringUtils.SPACE + notification.getCowId() + StringUtils.SPACE + this.activity.getString(C0530R.string.one) + " h. " + this.activity.getString(C0530R.string.medium_activity) + "!";
                        break;
                    case 12:
                        calvingText = this.activity.getString(C0530R.string.bull_with_cow) + StringUtils.SPACE + notification.getCowId() + StringUtils.SPACE + this.activity.getString(C0530R.string.one) + " h. " + this.activity.getString(C0530R.string.low_activity) + "!";
                        break;
                    case 20:
                        calvingText = this.activity.getString(C0530R.string.cow) + StringUtils.SPACE + notification.getCowId() + StringUtils.SPACE + this.activity.getString(C0530R.string.in_heat) + ". " + this.activity.getString(C0530R.string.high_activity) + "!";
                        break;
                    case 21:
                        calvingText = this.activity.getString(C0530R.string.cow) + StringUtils.SPACE + notification.getCowId() + StringUtils.SPACE + this.activity.getString(C0530R.string.in_heat) + ". " + this.activity.getString(C0530R.string.medium_activity) + "!";
                        break;
                    case 22:
                        calvingText = this.activity.getString(C0530R.string.cow) + StringUtils.SPACE + notification.getCowId() + StringUtils.SPACE + this.activity.getString(C0530R.string.in_heat) + ". " + this.activity.getString(C0530R.string.low_activity) + "!";
                        break;
                    case 50:
                        calvingText = this.activity.getString(C0530R.string.high_bull_activity) + StringUtils.SPACE + this.activity.getString(C0530R.string.from) + StringUtils.SPACE + notification.getFromTime() + "h " + this.activity.getString(C0530R.string.to) + StringUtils.SPACE + notification.getToTime() + "h";
                        break;
                    case 51:
                        calvingText = this.activity.getString(C0530R.string.medium_bull_activity) + StringUtils.SPACE + this.activity.getString(C0530R.string.from) + StringUtils.SPACE + notification.getFromTime() + "h " + this.activity.getString(C0530R.string.to) + StringUtils.SPACE + notification.getToTime() + "h";
                        break;
                    case 100:
                        if (!notification.getBatteryMessage().equals(Integer.valueOf(1))) {
                            calvingText = this.activity.getResources().getString(C0530R.string.turned_off);
                            break;
                        }
                        calvingText = this.activity.getResources().getString(C0530R.string.turned_off_battery_empty);
                        break;
                    case 101:
                        calvingText = this.activity.getResources().getString(C0530R.string.charging_ended);
                        break;
                    case 102:
                    case 103:
                        calvingText = this.activity.getResources().getString(C0530R.string.turned_off);
                        break;
                    case 252:
                        calvingText = this.activity.getString(C0530R.string.device_off_bull);
                        break;
                    case 253:
                        calvingText = this.activity.getResources().getString(C0530R.string.sms_test);
                        break;
                    default:
                        break;
                }
            }
        }
        if (notification.getMessageType().equals(Integer.valueOf(1))) {
            calvingText = this.activity.getResources().getString(C0530R.string.high_activity_1_hour);
        }
        if (notification.getMessageGroup().equals(Integer.valueOf(0)) && notification.getMessageType().equals(Integer.valueOf(2))) {
            calvingText = this.activity.getResources().getString(C0530R.string.high_activity_2_hour);
        }
        if (notification.getMessageType().equals(Integer.valueOf(255))) {
            calvingText = this.activity.getResources().getString(C0530R.string.reset_by_user);
        }
        if (notification.getMessageType().equals(Integer.valueOf(254))) {
            calvingText = this.activity.getResources().getString(C0530R.string.unit_too_long_attached_on_tail);
        }
        if (notification.getMessageType().equals(Integer.valueOf(253))) {
            calvingText = this.activity.getResources().getString(C0530R.string.sms_test);
        }
        if (notification.getMessageType().equals(Integer.valueOf(252))) {
            calvingText = this.activity.getResources().getString(C0530R.string.device_off_tail);
        }
        if (notification.getMessageType().equals(Integer.valueOf(100)) || notification.getMessageType().equals(Integer.valueOf(103)) || notification.getMessageType().equals(Integer.valueOf(102))) {
            calvingText = this.activity.getResources().getString(C0530R.string.turned_off);
        }
        if (notification.getMessageType().equals(Integer.valueOf(100)) && notification.getBatteryMessage().equals(Integer.valueOf(1))) {
            calvingText = this.activity.getResources().getString(C0530R.string.turned_off_battery_empty);
        }
        if (notification.getMessageType().equals(Integer.valueOf(0))) {
            calvingText = this.activity.getResources().getString(C0530R.string.medium_activity_1_hour);
        }
        if (notification.getMessageGroup().equals(Integer.valueOf(1)) && notification.getMessageType().equals(Integer.valueOf(3))) {
            calvingText = this.activity.getResources().getString(C0530R.string.turned_on);
        }
        if (notification.getMessageGroup().equals(Integer.valueOf(1)) && notification.getMessageType().equals(Integer.valueOf(2))) {
            calvingText = this.activity.getResources().getString(C0530R.string.device_charging_begins);
        }
        if (notification.getMessageType().equals(Integer.valueOf(101))) {
            calvingText = this.activity.getResources().getString(C0530R.string.charging_ended);
        }
        notificationType.setText(calvingText);
        return view;
    }
}
