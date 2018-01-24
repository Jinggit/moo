package com.moocall.moocall.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.fragment.DevicesFragment;
import com.moocall.moocall.fragment.NotificationsFragment;
import com.moocall.moocall.fragment.PhonesFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    Fragment deviceFragment;
    Fragment notificationFragment;
    Fragment phonesFragment;
    private String[] tabNames = new String[3];

    public TabsPagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        this.tabNames[0] = activity.getResources().getString(C0530R.string.devices);
        this.tabNames[1] = activity.getResources().getString(C0530R.string.notifications);
        this.tabNames[2] = activity.getResources().getString(C0530R.string.phones);
    }

    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                if (this.deviceFragment == null) {
                    this.deviceFragment = new DevicesFragment();
                }
                return this.deviceFragment;
            case 1:
                if (this.notificationFragment == null) {
                    this.notificationFragment = new NotificationsFragment();
                }
                return this.notificationFragment;
            case 2:
                if (this.phonesFragment == null) {
                    this.phonesFragment = new PhonesFragment();
                }
                return this.phonesFragment;
            default:
                return null;
        }
    }

    public CharSequence getPageTitle(int position) {
        return this.tabNames[position];
    }

    public int getCount() {
        return this.tabNames.length;
    }
}
