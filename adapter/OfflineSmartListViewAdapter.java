package com.moocall.moocall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.SwipeLayout.DragEdge;
import com.daimajia.swipe.SwipeLayout.ShowMode;
import com.daimajia.swipe.SwipeLayout.Status;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.OfflineSmartListActivity;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.util.Utils;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class OfflineSmartListViewAdapter extends BaseAdapter {
    private OfflineSmartListActivity activity;
    private List<AnimalDb> animals;
    private Animation animation;
    private Integer animationColor;
    private Integer animationItem;
    private Integer filter;
    private LayoutInflater inflater;
    public SwipeLayout swipeLayout;
    private Integer type;

    class C05974 implements AnimationListener {
        C05974() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            OfflineSmartListViewAdapter.this.activity.reloadAnimals();
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    public OfflineSmartListViewAdapter(OfflineSmartListActivity context, List<AnimalDb> objects, Integer type, Integer filter) {
        this.activity = context;
        this.animals = objects;
        this.type = type;
        this.filter = filter;
    }

    public void setAnimationColor(Integer color) {
        this.animationColor = color;
    }

    public void setAnimationItem(Integer item) {
        this.animationItem = item;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public Animation getAnimation() {
        return this.animation;
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
        if (this.inflater == null) {
            OfflineSmartListActivity offlineSmartListActivity = this.activity;
            OfflineSmartListActivity offlineSmartListActivity2 = this.activity;
            this.inflater = (LayoutInflater) offlineSmartListActivity.getSystemService("layout_inflater");
        }
        if (view == null) {
            view = this.inflater.inflate(C0530R.layout.smart_list_row, null);
        }
        this.swipeLayout = (SwipeLayout) view.findViewById(C0530R.id.swipe_layout);
        View rightSwipeLayout = (RelativeLayout) view.findViewById(C0530R.id.rightSwipeLayout);
        View leftSwipeLayout = (RelativeLayout) view.findViewById(C0530R.id.leftSwipeLayout);
        ImageView leftSwipeIcon = (ImageView) view.findViewById(C0530R.id.leftSwipeIcon);
        TextView leftSwipeText = (TextView) view.findViewById(C0530R.id.leftSwipeText);
        ImageView rightSwipeIcon = (ImageView) view.findViewById(C0530R.id.rightSwipeIcon);
        TextView rightSwipeText = (TextView) view.findViewById(C0530R.id.rightSwipeText);
        ImageView animalImage = (ImageView) view.findViewById(C0530R.id.animalImage);
        TextView animalName = (TextView) view.findViewById(C0530R.id.animalName);
        TextView animalCode = (TextView) view.findViewById(C0530R.id.animalCode);
        TextView animalBottomLeft = (TextView) view.findViewById(C0530R.id.animalBottomLeft);
        TextView animalTopRight = (TextView) view.findViewById(C0530R.id.animalTopRight);
        TextView animalMiddleRight = (TextView) view.findViewById(C0530R.id.animalMiddleRight);
        TextView animalBottomRight = (TextView) view.findViewById(C0530R.id.animalBottomRight);
        LinearLayout sensorView = (LinearLayout) view.findViewById(C0530R.id.sensorView);
        sensorView.setVisibility(8);
        TextView sensorText = (TextView) view.findViewById(C0530R.id.sensorText);
        final AnimalDb animal = (AnimalDb) this.animals.get(i);
        if (!(animal.getSensor() == null || animal.getSensor().equals("null") || animal.getSensor().length() <= 0)) {
            sensorText.setText(animal.getSensor());
            sensorView.setVisibility(0);
        }
        animalName.setText(animal.getName());
        animalCode.setText(animal.getTag_number());
        animalImage.setImageBitmap(Utils.getAnimalPlaceholder(animal.getType(), this.activity));
        switch (this.type.intValue()) {
            case 1:
                animalName.setTextColor(this.activity.getResources().getColor(C0530R.color.green_mh));
                animalBottomRight.setTextColor(this.activity.getResources().getColor(C0530R.color.green_mh));
                animalBottomLeft.setVisibility(8);
                switch (this.filter.intValue()) {
                    case 3:
                        leftSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.red_mh));
                        rightSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.red_mh));
                        leftSwipeText.setText(this.activity.getString(C0530R.string.heat_detected));
                        rightSwipeText.setText(this.activity.getString(C0530R.string.heat_detected));
                        leftSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.heat_detected_icon));
                        rightSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.heat_detected_icon));
                        animalTopRight.setText(this.activity.getString(C0530R.string.gave_birth_on) + ":");
                        animalMiddleRight.setText(Utils.fromServerToNormal(animal.getLast_calving_date()));
                        animalBottomRight.setText(this.activity.getString(C0530R.string.calved) + StringUtils.SPACE + Utils.getTimeShorter(animal.getLast_calving_date() + " 00:00") + StringUtils.SPACE + this.activity.getString(C0530R.string.ago));
                        break;
                    default:
                        leftSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.dark_gray_mh));
                        rightSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.dark_gray_mh));
                        leftSwipeText.setText(this.activity.getString(C0530R.string.add_calving_sensor));
                        rightSwipeText.setText(this.activity.getString(C0530R.string.add_calving));
                        leftSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.add_sensor_icon));
                        rightSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.add_calving_icon));
                        animalTopRight.setText(this.activity.getString(C0530R.string.due_date).toUpperCase() + ":");
                        animalMiddleRight.setText(Utils.fromServerToNormal(animal.getDue_date()));
                        String dueDate = Utils.getTimeShorterReverse(animal.getDue_date());
                        String dueDateText = "";
                        if (dueDate.equals("overdue")) {
                            dueDateText = this.activity.getString(C0530R.string.overdue) + StringUtils.SPACE + Utils.getTimeShorter(animal.getDue_date() + " 00:00");
                        } else {
                            dueDateText = this.activity.getString(C0530R.string.due_in) + StringUtils.SPACE + dueDate;
                        }
                        animalBottomRight.setText(dueDateText);
                        break;
                }
            case 2:
                leftSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.red_mh));
                rightSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.blue_mh));
                leftSwipeText.setText(this.activity.getString(C0530R.string.heat_detected));
                rightSwipeText.setText(this.activity.getString(C0530R.string.add_insemination));
                leftSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.heat_detected_icon));
                rightSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.add_calving_icon));
                animalName.setTextColor(this.activity.getResources().getColor(C0530R.color.orange_mh));
                animalBottomRight.setTextColor(this.activity.getResources().getColor(C0530R.color.orange_mh));
                animalBottomLeft.setVisibility(0);
                animalBottomLeft.setText(this.activity.getString(C0530R.string.last_heat_detected) + ":");
                animalTopRight.setText(this.activity.getString(C0530R.string.last_cycle).toUpperCase() + ":");
                animalMiddleRight.setText(Utils.getTimeShorter(Utils.calculateTime(animal.getCycle_date(), "yyyy-MM-dd HH:mm")) + StringUtils.SPACE + this.activity.getString(C0530R.string.ago));
                animalBottomRight.setText(Utils.calculateTime(animal.getIn_heat_date(), "dd-MM-yyyy HH:mm"));
                break;
            case 3:
                leftSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.blue_mh));
                rightSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.blue_mh));
                leftSwipeText.setText(this.activity.getString(C0530R.string.add_insemination));
                rightSwipeText.setText(this.activity.getString(C0530R.string.add_insemination));
                leftSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.add_sensor_icon));
                rightSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.add_calving_icon));
                animalName.setTextColor(this.activity.getResources().getColor(C0530R.color.red_mh));
                animalBottomRight.setTextColor(this.activity.getResources().getColor(C0530R.color.red_mh));
                animalBottomLeft.setVisibility(8);
                animalTopRight.setText(this.activity.getString(C0530R.string.standing_heat_at) + ":");
                animalMiddleRight.setText(Utils.calculateTime(animal.getIn_heat_date(), "dd-MM-yyyy HH:mm"));
                animalBottomRight.setText(this.activity.getString(C0530R.string.heat_detected));
                break;
            case 4:
                leftSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.red_mh));
                rightSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.green_mh));
                leftSwipeText.setText(this.activity.getString(C0530R.string.heat_detected));
                rightSwipeText.setText(this.activity.getString(C0530R.string.assume_in_calf));
                leftSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.heat_detected_icon));
                rightSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.assume_in_calf_icon));
                animalName.setTextColor(this.activity.getResources().getColor(C0530R.color.blue_mh));
                animalBottomRight.setTextColor(this.activity.getResources().getColor(C0530R.color.blue_mh));
                animalBottomLeft.setVisibility(8);
                animalTopRight.setText(this.activity.getString(C0530R.string.last_inseminated) + ":");
                animalMiddleRight.setText(Utils.calculateTime(animal.getInsemination_date(), "dd-MM-yyyy HH:mm"));
                animalBottomRight.setText(Utils.getTimeShorter(Utils.calculateTime(animal.getInsemination_date(), "yyyy-MM-dd HH:mm")) + StringUtils.SPACE + this.activity.getString(C0530R.string.ago));
                break;
            case 5:
                leftSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.dark_gray_mh));
                rightSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.blue_mh));
                leftSwipeText.setText(this.activity.getString(C0530R.string.add_calving_sensor));
                rightSwipeText.setText(this.activity.getString(C0530R.string.add_insemination));
                leftSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.add_sensor_icon));
                rightSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.add_calving_icon));
                animalName.setTextColor(this.activity.getResources().getColor(C0530R.color.dark_green));
                animalBottomRight.setTextColor(this.activity.getResources().getColor(C0530R.color.dark_green));
                animalBottomLeft.setVisibility(8);
                if (animal.getBull_sensor() != null && animal.getBull_sensor().length() > 0) {
                    animalBottomLeft.setVisibility(0);
                    animalBottomLeft.setText(this.activity.getString(C0530R.string.sensor) + ": " + animal.getBull_sensor());
                }
                animalTopRight.setText(this.activity.getString(C0530R.string.age) + ":");
                animalMiddleRight.setText(Utils.getTimeShorter(Utils.calculateTime(animal.getBirth_date() + " 00:00:00", "yyyy-MM-dd HH:mm")));
                animalBottomRight.setText(Utils.getWeightText(animal.getWeight(), this.activity));
                break;
            case 6:
                leftSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.red_mh));
                rightSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.red_mh));
                leftSwipeText.setText(this.activity.getString(C0530R.string.mark_as_replacement));
                rightSwipeText.setText(this.activity.getString(C0530R.string.sell));
                leftSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.mark_as_replacement_icon));
                rightSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.sell_icon));
                animalName.setTextColor(this.activity.getResources().getColor(C0530R.color.dark_green));
                animalBottomRight.setTextColor(this.activity.getResources().getColor(C0530R.color.dark_green));
                animalBottomLeft.setVisibility(8);
                animalTopRight.setText(this.activity.getString(C0530R.string.age) + ":");
                animalMiddleRight.setText(Utils.getTimeShorter(Utils.calculateTime(animal.getBirth_date() + " 00:00:00", "yyyy-MM-dd HH:mm")));
                animalBottomRight.setText(Utils.getWeightText(animal.getWeight(), this.activity));
                break;
            case 7:
                leftSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.red_mh));
                rightSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.dark_green));
                leftSwipeText.setText(this.activity.getString(C0530R.string.sell));
                rightSwipeText.setText(this.activity.getString(C0530R.string.add_weight));
                leftSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.sell_icon));
                rightSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.add_calving_icon));
                animalName.setTextColor(this.activity.getResources().getColor(C0530R.color.dark_green));
                animalBottomRight.setTextColor(this.activity.getResources().getColor(C0530R.color.dark_green));
                animalBottomLeft.setVisibility(8);
                animalTopRight.setText(this.activity.getString(C0530R.string.age) + ":");
                animalMiddleRight.setText(Utils.getTimeShorter(Utils.calculateTime(animal.getBirth_date() + " 00:00:00", "yyyy-MM-dd HH:mm")));
                animalBottomRight.setText(Utils.getWeightText(animal.getWeight(), this.activity));
                break;
            case 8:
                leftSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.dark_gray_mh));
                rightSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.dark_gray_mh));
                leftSwipeText.setText(this.activity.getString(C0530R.string.undo));
                rightSwipeText.setText(this.activity.getString(C0530R.string.undo));
                leftSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.undo_l));
                rightSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.undo_r));
                animalName.setTextColor(this.activity.getResources().getColor(C0530R.color.dark_green));
                animalBottomRight.setTextColor(this.activity.getResources().getColor(C0530R.color.dark_green));
                animalBottomLeft.setVisibility(8);
                animalTopRight.setText(this.activity.getString(C0530R.string.age) + ":");
                animalMiddleRight.setText(Utils.getTimeShorter(Utils.calculateTime(animal.getBirth_date() + " 00:00:00", "yyyy-MM-dd HH:mm")));
                animalBottomRight.setText(Utils.getWeightText(animal.getWeight(), this.activity));
                break;
            case 9:
                leftSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.blue_mh));
                rightSwipeLayout.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.dark_gray_mh));
                leftSwipeText.setText(this.activity.getString(C0530R.string.add_insemination));
                rightSwipeText.setText(this.activity.getString(C0530R.string.add_calving));
                leftSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.add_sensor_icon));
                rightSwipeIcon.setImageDrawable(this.activity.getResources().getDrawable(C0530R.drawable.add_calving_icon));
                animalName.setTextColor(this.activity.getResources().getColor(C0530R.color.dark_green));
                animalBottomRight.setTextColor(this.activity.getResources().getColor(C0530R.color.dark_green));
                animalBottomLeft.setVisibility(8);
                animalTopRight.setText(this.activity.getString(C0530R.string.age) + ":");
                animalMiddleRight.setText(Utils.getTimeShorter(Utils.calculateTime(animal.getBirth_date() + " 00:00:00", "yyyy-MM-dd HH:mm")));
                animalBottomRight.setText(Utils.getWeightText(animal.getWeight(), this.activity));
                break;
        }
        this.swipeLayout.setShowMode(ShowMode.LayDown);
        this.swipeLayout.addDrag(DragEdge.Left, leftSwipeLayout);
        this.swipeLayout.addDrag(DragEdge.Right, rightSwipeLayout);
        this.swipeLayout.getSurfaceView().setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (OfflineSmartListViewAdapter.this.swipeLayout.getOpenStatus() == Status.Close) {
                    OfflineSmartListViewAdapter.this.activity.openAnimalDetails(animal);
                }
            }
        });
        final int i2 = i;
        rightSwipeLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineSmartListViewAdapter.this.activity.clickedAction(Integer.valueOf(i2), Boolean.valueOf(true));
            }
        });
        i2 = i;
        leftSwipeLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineSmartListViewAdapter.this.activity.clickedAction(Integer.valueOf(i2), Boolean.valueOf(false));
            }
        });
        if (this.animationItem != null && i == this.animationItem.intValue()) {
            animalName.setTextColor(this.animationColor.intValue());
            animalBottomRight.setTextColor(this.animationColor.intValue());
            Animation animation = new TranslateAnimation(0.0f, (float) (-view.getWidth()), 0.0f, 0.0f);
            animation.setDuration(1500);
            animation.setFillAfter(true);
            animation.setAnimationListener(new C05974());
            view.startAnimation(animation);
            viewGroup.setBackgroundColor(this.animationColor.intValue());
            setAnimation(animation);
        } else if (this.animationColor == null) {
            viewGroup.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.white));
        }
        return view;
    }

    public void setFilter(Integer filter) {
        this.filter = filter;
        notifyDataSetChanged();
    }
}
