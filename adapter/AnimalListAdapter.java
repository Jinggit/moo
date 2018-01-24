package com.moocall.moocall.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.db.AnimalDb;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class AnimalListAdapter extends BaseAdapter {
    private Activity activity;
    private List<AnimalDb> animals = new ArrayList();
    private List<String> typeList;

    public AnimalListAdapter(Activity activity) {
        this.activity = activity;
        setTypeList();
    }

    public void setAnimals(List<AnimalDb> animals) {
        this.animals = animals;
        notifyDataSetChanged();
    }

    public List<AnimalDb> getAnimals() {
        return this.animals;
    }

    private void setTypeList() {
        this.typeList = new ArrayList();
        this.typeList.add(this.activity.getString(C0530R.string.cow));
        this.typeList.add(this.activity.getString(C0530R.string.ai_straw) + StringUtils.SPACE + this.activity.getString(C0530R.string.bull));
        this.typeList.add(this.activity.getString(C0530R.string.breeding) + StringUtils.SPACE + this.activity.getString(C0530R.string.heifer));
        this.typeList.add(this.activity.getString(C0530R.string.breeding) + StringUtils.SPACE + this.activity.getString(C0530R.string.bull));
        this.typeList.add(this.activity.getString(C0530R.string.beef) + StringUtils.SPACE + this.activity.getString(C0530R.string.heifer));
        this.typeList.add(this.activity.getString(C0530R.string.beef) + StringUtils.SPACE + this.activity.getString(C0530R.string.bull));
        this.typeList.add(this.activity.getString(C0530R.string.calf) + StringUtils.SPACE + this.activity.getString(C0530R.string.heifer));
        this.typeList.add(this.activity.getString(C0530R.string.calf) + StringUtils.SPACE + this.activity.getString(C0530R.string.bull));
        this.typeList.add(this.activity.getString(C0530R.string.bullock));
        this.typeList.add(this.activity.getString(C0530R.string.vasectomized) + StringUtils.SPACE + this.activity.getString(C0530R.string.bull));
    }

    public int getCount() {
        return this.animals.size();
    }

    public Object getItem(int i) {
        return this.animals.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(C0530R.layout.animal_list_row, viewGroup, false);
        TextView animalName = (TextView) view.findViewById(C0530R.id.animalName);
        TextView animalType = (TextView) view.findViewById(C0530R.id.animalType);
        AnimalDb animal = (AnimalDb) this.animals.get(i);
        ((TextView) view.findViewById(C0530R.id.animalTag)).setText(animal.getTag_number());
        animalName.setText(animal.getName());
        animalType.setText((CharSequence) this.typeList.get(animal.getType().intValue() - 1));
        return view;
    }
}
