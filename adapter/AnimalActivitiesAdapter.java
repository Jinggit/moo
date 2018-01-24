package com.moocall.moocall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.domain.AnimalHistory;
import com.moocall.moocall.domain.AnimalHistoryRecent;
import java.util.ArrayList;
import java.util.List;

public class AnimalActivitiesAdapter extends BaseAdapter {
    private List<AnimalHistoryRecent> activities = new ArrayList();

    public void setActivities(List<AnimalHistoryRecent> activities) {
        this.activities = activities;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.activities.size();
    }

    public Object getItem(int i) {
        return this.activities.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (((AnimalHistoryRecent) this.activities.get(i)).getHeader().equals(Boolean.valueOf(true))) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(C0530R.layout.animal_activity_header, viewGroup, false);
            ((TextView) view.findViewById(C0530R.id.activityHeader)).setText(((AnimalHistoryRecent) this.activities.get(i)).getDate());
            return view;
        }
        view = LayoutInflater.from(viewGroup.getContext()).inflate(C0530R.layout.animal_activity_row, viewGroup, false);
        TextView activityDate = (TextView) view.findViewById(C0530R.id.activityDate);
        TextView activityName = (TextView) view.findViewById(C0530R.id.activityName);
        TextView activityTag = (TextView) view.findViewById(C0530R.id.activityTag);
        TextView activityText = (TextView) view.findViewById(C0530R.id.activityText);
        View verticalLineTop = view.findViewById(C0530R.id.verticalLineTop);
        View verticalLineBottom = view.findViewById(C0530R.id.verticalLineBottom);
        AnimalHistory activity = ((AnimalHistoryRecent) this.activities.get(i)).getAnimalHistory();
        activityDate.setText(activity.getTime());
        activityName.setText(activity.getName());
        activityTag.setText(activity.getTagNumber());
        activityText.setText(activity.getText());
        if (((AnimalHistoryRecent) this.activities.get(i)).getFirst().booleanValue()) {
            verticalLineTop.setVisibility(8);
        } else {
            verticalLineTop.setVisibility(0);
        }
        if (((AnimalHistoryRecent) this.activities.get(i)).getLast().booleanValue()) {
            verticalLineBottom.setVisibility(8);
            return view;
        }
        verticalLineBottom.setVisibility(0);
        return view;
    }
}
