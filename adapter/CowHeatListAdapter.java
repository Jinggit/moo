package com.moocall.moocall.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.domain.AnimalHistoryRecent;
import com.moocall.moocall.domain.CowHeat;
import com.moocall.moocall.util.Utils;
import java.util.ArrayList;
import java.util.List;

public class CowHeatListAdapter extends BaseAdapter {
    private Activity activity;
    private List<AnimalHistoryRecent> list = new ArrayList();

    public CowHeatListAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setList(List<AnimalHistoryRecent> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int i) {
        return this.list.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (((AnimalHistoryRecent) this.list.get(i)).getHeader().equals(Boolean.valueOf(true))) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(C0530R.layout.animal_activity_header, viewGroup, false);
            ((TextView) view.findViewById(C0530R.id.activityHeader)).setText(((AnimalHistoryRecent) this.list.get(i)).getDate());
            return view;
        }
        view = LayoutInflater.from(viewGroup.getContext()).inflate(C0530R.layout.cow_heat_row, viewGroup, false);
        TextView tag = (TextView) view.findViewById(C0530R.id.tag);
        TextView name = (TextView) view.findViewById(C0530R.id.name);
        TextView date = (TextView) view.findViewById(C0530R.id.date);
        TextView from = (TextView) view.findViewById(C0530R.id.from);
        TextView to = (TextView) view.findViewById(C0530R.id.to);
        CowHeat cowHeat = ((AnimalHistoryRecent) this.list.get(i)).getCowHeat();
        tag.setText(cowHeat.getTag());
        name.setText(cowHeat.getName());
        date.setText(cowHeat.getDate());
        from.setText(this.activity.getString(C0530R.string.from) + ": " + Utils.calculateTime(cowHeat.getFrom(), "HH:mm"));
        if (cowHeat.getTo().equals("0000-00-00 00:00:00")) {
            to.setText(this.activity.getString(C0530R.string.to) + ": " + this.activity.getString(C0530R.string.ongoing));
            return view;
        }
        to.setText(this.activity.getString(C0530R.string.to) + ": " + Utils.calculateTime(cowHeat.getTo(), "HH:mm"));
        return view;
    }
}
