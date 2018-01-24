package com.moocall.moocall.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.moocall.moocall.C0530R;

public class DrawerListAdapter extends BaseAdapter {
    private Activity activity;
    protected String[] drawerList = new String[8];
    private LayoutInflater inflater;

    public DrawerListAdapter(Activity activity) {
        this.activity = activity;
        this.drawerList[0] = activity.getResources().getString(C0530R.string.manage_herd);
        this.drawerList[1] = activity.getResources().getString(C0530R.string.my_moocall);
        this.drawerList[2] = activity.getResources().getString(C0530R.string.scan_moocall_tag);
        this.drawerList[3] = activity.getResources().getString(C0530R.string.ask_question);
        this.drawerList[4] = activity.getResources().getString(C0530R.string.moocall_social);
        this.drawerList[5] = activity.getResources().getString(C0530R.string.action_settings);
        this.drawerList[6] = activity.getResources().getString(C0530R.string.break_text);
        this.drawerList[7] = activity.getResources().getString(C0530R.string.sign_out);
    }

    public int getCount() {
        return this.drawerList.length;
    }

    public Object getItem(int position) {
        return this.drawerList[position];
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        if (this.inflater == null) {
            this.inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
        }
        if (view != null) {
            return view;
        }
        if (position < 6) {
            view = this.inflater.inflate(C0530R.layout.top_drawer_element, null);
            ImageView drawerListIcon = (ImageView) view.findViewById(C0530R.id.drawerElementIcon);
            ((TextView) view.findViewById(C0530R.id.drawerElementName)).setText(this.drawerList[position]);
            switch (position) {
                case 0:
                    drawerListIcon.setImageResource(C0530R.drawable.manage_your_heard_icon);
                    return view;
                case 1:
                    drawerListIcon.setImageResource(C0530R.drawable.my_moocall_icon);
                    return view;
                case 2:
                    drawerListIcon.setImageResource(C0530R.drawable.moocall_scan_icon);
                    return view;
                case 3:
                    drawerListIcon.setImageResource(C0530R.drawable.ask_question_icon);
                    return view;
                case 4:
                    drawerListIcon.setImageResource(C0530R.drawable.moocall_social_icon);
                    return view;
                case 5:
                    drawerListIcon.setImageResource(C0530R.drawable.settings_icon);
                    return view;
                default:
                    return view;
            }
        } else if (position <= 6) {
            return this.inflater.inflate(C0530R.layout.drawer_break, null);
        } else {
            view = this.inflater.inflate(C0530R.layout.bottom_drawer_element, null);
            ((TextView) view.findViewById(C0530R.id.botDrawerName)).setText(this.drawerList[position]);
            return view;
        }
    }
}
