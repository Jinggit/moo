package com.moocall.moocall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.OfflineAnimalDetailsActivity;
import com.moocall.moocall.db.AnimalActionDb;
import com.moocall.moocall.db.AnimalActionDbDao.Properties;
import com.moocall.moocall.db.AnimalDb;
import com.moocall.moocall.domain.AnimalHistory;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import com.moocall.moocall.util.Utils;
import java.util.List;

public class OfflineAnimalDetailsAdapter extends BaseAdapter {
    private OfflineAnimalDetailsActivity activity;
    private AnimalDb animal;
    private LayoutInflater inflater;

    class C05851 implements OnClickListener {
        C05851() {
        }

        public void onClick(View view) {
            OfflineAnimalDetailsAdapter.this.activity.openMoocallTagOverlay();
        }
    }

    class C05862 implements OnClickListener {
        C05862() {
        }

        public void onClick(View view) {
            OfflineAnimalDetailsAdapter.this.activity.editAnimal();
        }
    }

    class C05884 implements OnClickListener {
        C05884() {
        }

        public void onClick(View view) {
            OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(14), null);
        }
    }

    class C05906 implements OnClickListener {
        C05906() {
        }

        public void onClick(View view) {
            OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(2), null);
        }
    }

    class C05917 implements OnClickListener {
        C05917() {
        }

        public void onClick(View view) {
            OfflineAnimalDetailsAdapter.this.activity.addCalving();
        }
    }

    class C05928 implements OnClickListener {
        C05928() {
        }

        public void onClick(View view) {
            OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(3), null);
        }
    }

    class C05939 implements OnClickListener {
        C05939() {
        }

        public void onClick(View view) {
            if (OfflineAnimalDetailsAdapter.this.animal.getType().intValue() == 2 || OfflineAnimalDetailsAdapter.this.animal.getType().intValue() == 4) {
                OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(15), null);
            } else {
                OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(4), null);
            }
        }
    }

    public OfflineAnimalDetailsAdapter(OfflineAnimalDetailsActivity context) {
        this.activity = context;
    }

    public AnimalDb getAnimal() {
        return this.animal;
    }

    public void setAnimal(AnimalDb animal) {
        this.animal = animal;
        notifyDataSetChanged();
    }

    public int getCount() {
        if (this.animal == null) {
            return 0;
        }
        return 4;
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (this.inflater == null) {
            OfflineAnimalDetailsActivity offlineAnimalDetailsActivity = this.activity;
            OfflineAnimalDetailsActivity offlineAnimalDetailsActivity2 = this.activity;
            this.inflater = (LayoutInflater) offlineAnimalDetailsActivity.getSystemService("layout_inflater");
        }
        switch (i) {
            case 0:
                return addBasic();
            case 1:
                return addNotes();
            case 2:
                return addHistory();
            case 3:
                return addEvents();
            default:
                return this.inflater.inflate(C0530R.layout.smart_list_row, null);
        }
    }

    private View addBasic() {
        View view = this.inflater.inflate(C0530R.layout.animal_details_basic, null);
        ImageView animalImage = (ImageView) view.findViewById(C0530R.id.animalImage);
        ImageView animalEditIcon = (ImageView) view.findViewById(C0530R.id.animalEditIcon);
        TextView animalName = (TextView) view.findViewById(C0530R.id.animalName);
        TextView animalTag = (TextView) view.findViewById(C0530R.id.animalTag);
        LinearLayout animalBreedLayout = (LinearLayout) view.findViewById(C0530R.id.animalBreedLayout);
        TextView animalBreedText = (TextView) view.findViewById(C0530R.id.animalBreedText);
        LinearLayout animalTemperamentLayout = (LinearLayout) view.findViewById(C0530R.id.animalTemperamentLayout);
        TextView animalTemperamentText = (TextView) view.findViewById(C0530R.id.animalTemperamentText);
        LinearLayout animalAgeLayout = (LinearLayout) view.findViewById(C0530R.id.animalAgeLayout);
        TextView animalAgeText = (TextView) view.findViewById(C0530R.id.animalAgeText);
        LinearLayout animalWeightLayout = (LinearLayout) view.findViewById(C0530R.id.animalWeightLayout);
        TextView animalWeightText = (TextView) view.findViewById(C0530R.id.animalWeightText);
        LinearLayout animalVendorLayout = (LinearLayout) view.findViewById(C0530R.id.animalVendorLayout);
        TextView animalVendorText = (TextView) view.findViewById(C0530R.id.animalVendorText);
        animalVendorLayout.setVisibility(8);
        if (this.animal.getType().intValue() == 2) {
            animalVendorLayout.setVisibility(0);
        }
        LinearLayout sensorView = (LinearLayout) view.findViewById(C0530R.id.sensorView);
        sensorView.setVisibility(8);
        TextView sensorText = (TextView) view.findViewById(C0530R.id.sensorText);
        if (this.animal.getSensor() != null && this.animal.getSensor().length() > 0) {
            sensorText.setText(this.animal.getSensor());
            sensorView.setVisibility(0);
        }
        ImageView animalMoocallTagIcon = (ImageView) view.findViewById(C0530R.id.animalMoocallTagIcon);
        animalMoocallTagIcon.setVisibility(8);
        if (!(this.animal.getMoocall_tag_number() == null || this.animal.getMoocall_tag_number().isEmpty())) {
            animalMoocallTagIcon.setVisibility(0);
            animalMoocallTagIcon.setOnClickListener(new C05851());
        }
        animalImage.setImageBitmap(Utils.getAnimalPlaceholder(this.animal.getType(), this.activity));
        animalName.setText(this.animal.getName());
        animalTag.setText(this.animal.getTag_number());
        animalBreedText.setText(this.animal.getBreed());
        animalTemperamentText.setText(this.animal.getTemperament());
        animalAgeText.setText(Utils.getTimeShorter(Utils.calculateTime(this.animal.getBirth_date() + " 00:00:00", "yyyy-MM-dd HH:mm")));
        animalWeightText.setText(Utils.getWeightText(this.animal.getWeight(), this.activity));
        animalVendorText.setText(this.animal.getVendor());
        animalEditIcon.setOnClickListener(new C05862());
        return view;
    }

    private View addNotes() {
        View view = this.inflater.inflate(C0530R.layout.animal_details_notes, null);
        List<AnimalActionDb> animalActionList = ((MoocallAnalyticsApplication) this.activity.getApplication()).getDaoSession().getAnimalActionDbDao().queryBuilder().where(Properties.Animal_tag_number.eq(this.animal.getTag_number()), Properties.Action.eq(Integer.valueOf(14))).orderDesc(Properties.Datetime).list();
        TextView numNotesItems = (TextView) view.findViewById(C0530R.id.numNotesItems);
        LinearLayout notesLess = (LinearLayout) view.findViewById(C0530R.id.notesLess);
        numNotesItems.setText(Integer.valueOf(animalActionList.size()).toString());
        notesLess.setVisibility(8);
        ImageView addNewNote = (ImageView) view.findViewById(C0530R.id.addNewNote);
        LinearLayout notesRows = (LinearLayout) view.findViewById(C0530R.id.notesRows);
        for (final AnimalActionDb note : animalActionList) {
            View notesRow = this.activity.getLayoutInflater().inflate(C0530R.layout.notes_row, null);
            TextView notesText = (TextView) notesRow.findViewById(C0530R.id.notesText);
            notesRows.addView(notesRow);
            notesText.setText(note.getShowText());
            notesRow.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OfflineAnimalDetailsAdapter.this.activity.historyClicked(note);
                }
            });
        }
        addNewNote.setOnClickListener(new C05884());
        return view;
    }

    private View addHistory() {
        View view = this.inflater.inflate(C0530R.layout.animal_details_history, null);
        List<AnimalActionDb> animalActionList = ((MoocallAnalyticsApplication) this.activity.getApplication()).getDaoSession().getAnimalActionDbDao().queryBuilder().where(Properties.Animal_tag_number.eq(this.animal.getTag_number()), Properties.Action.notEq(Integer.valueOf(14)), Properties.Action.notEq(Integer.valueOf(16))).orderDesc(Properties.Datetime).list();
        Integer count = Integer.valueOf(animalActionList.size());
        ((LinearLayout) view.findViewById(C0530R.id.notesMore)).setVisibility(8);
        TextView numHistoryItems = (TextView) view.findViewById(C0530R.id.numHistoryItems);
        ((LinearLayout) view.findViewById(C0530R.id.historyLess)).setVisibility(8);
        numHistoryItems.setText(count.toString());
        LinearLayout historyRows = (LinearLayout) view.findViewById(C0530R.id.historyRows);
        for (int i = 0; i < count.intValue(); i++) {
            View historyRow = this.activity.getLayoutInflater().inflate(C0530R.layout.history_row, null);
            TextView historyName = (TextView) historyRow.findViewById(C0530R.id.historyName);
            TextView historyTime = (TextView) historyRow.findViewById(C0530R.id.historyTime);
            TextView historyText = (TextView) historyRow.findViewById(C0530R.id.historyText);
            final AnimalActionDb historyDb = (AnimalActionDb) animalActionList.get(i);
            System.out.println(historyDb.getAction());
            AnimalHistory history = new AnimalHistory(historyDb, this.activity);
            System.out.println(history.getType());
            historyName.setText(history.getName());
            historyTime.setText(history.getDate());
            historyText.setText(history.getText());
            View verticalLineBottom = historyRow.findViewById(C0530R.id.verticalLineBottom);
            View verticalLineTop = historyRow.findViewById(C0530R.id.verticalLineTop);
            View verticalCircle = historyRow.findViewById(C0530R.id.verticalCircle);
            if (i == 0) {
                verticalLineTop.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.white));
            } else {
                verticalLineTop.setBackgroundColor(this.activity.getResources().getColor(C0530R.color.mid_gray));
            }
            if (i == count.intValue() - 1) {
                verticalLineBottom.setVisibility(8);
            } else {
                verticalLineBottom.setVisibility(0);
            }
            historyRows.addView(historyRow);
            historyRow.getLayoutParams().height = (verticalLineBottom.getLayoutParams().height + verticalCircle.getLayoutParams().height) + historyRow.getLayoutParams().height;
            historyRow.requestLayout();
            historyRow.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    OfflineAnimalDetailsAdapter.this.activity.historyClicked(historyDb);
                }
            });
        }
        return view;
    }

    private View addEvents() {
        View view = this.inflater.inflate(C0530R.layout.animal_details_events, null);
        ((LinearLayout) view.findViewById(C0530R.id.historyMore)).setVisibility(8);
        LinearLayout addWeightAction = (LinearLayout) view.findViewById(C0530R.id.addWeightAction);
        LinearLayout addOtherAction = (LinearLayout) view.findViewById(C0530R.id.addOtherAction);
        final ImageView otherIcon = (ImageView) view.findViewById(C0530R.id.otherIcon);
        final LinearLayout slaughteredAction = (LinearLayout) view.findViewById(C0530R.id.slaughteredAction);
        final LinearLayout deceasedAction = (LinearLayout) view.findViewById(C0530R.id.deceasedAction);
        final LinearLayout soldAction = (LinearLayout) view.findViewById(C0530R.id.soldAction);
        slaughteredAction.setVisibility(8);
        deceasedAction.setVisibility(8);
        soldAction.setVisibility(8);
        otherIcon.setVisibility(0);
        LinearLayout addCalvingAction = (LinearLayout) view.findViewById(C0530R.id.addCalvingAction);
        LinearLayout addHeatAction = (LinearLayout) view.findViewById(C0530R.id.addHeatAction);
        LinearLayout addInseminationAction = (LinearLayout) view.findViewById(C0530R.id.addInseminationAction);
        LinearLayout addWeaningAction = (LinearLayout) view.findViewById(C0530R.id.addWeanningAction);
        LinearLayout addDehornedAction = (LinearLayout) view.findViewById(C0530R.id.addDehornedAction);
        LinearLayout addCastrationAction = (LinearLayout) view.findViewById(C0530R.id.addCastrationAction);
        LinearLayout markAsReplacementAction = (LinearLayout) view.findViewById(C0530R.id.markAsReplacementAction);
        LinearLayout assumeInCalf = (LinearLayout) view.findViewById(C0530R.id.assumeInCalf);
        addCalvingAction.setVisibility(8);
        addHeatAction.setVisibility(8);
        addInseminationAction.setVisibility(8);
        addWeaningAction.setVisibility(8);
        addDehornedAction.setVisibility(8);
        addCastrationAction.setVisibility(8);
        markAsReplacementAction.setVisibility(8);
        assumeInCalf.setVisibility(8);
        switch (this.animal.getType().intValue()) {
            case 1:
            case 3:
                addCalvingAction.setVisibility(0);
                addHeatAction.setVisibility(0);
                addInseminationAction.setVisibility(0);
                assumeInCalf.setVisibility(0);
                break;
            case 2:
            case 4:
                addInseminationAction.setVisibility(0);
                break;
            case 5:
                markAsReplacementAction.setVisibility(0);
                break;
            case 6:
                markAsReplacementAction.setVisibility(0);
                addCastrationAction.setVisibility(0);
                break;
            case 7:
                addWeaningAction.setVisibility(0);
                markAsReplacementAction.setVisibility(0);
                addDehornedAction.setVisibility(0);
                addCalvingAction.setVisibility(0);
                addHeatAction.setVisibility(0);
                break;
            case 8:
                addWeaningAction.setVisibility(0);
                markAsReplacementAction.setVisibility(0);
                addDehornedAction.setVisibility(0);
                addCalvingAction.setVisibility(0);
                addCastrationAction.setVisibility(0);
                break;
        }
        addWeightAction.setOnClickListener(new C05906());
        addCalvingAction.setOnClickListener(new C05917());
        addHeatAction.setOnClickListener(new C05928());
        addInseminationAction.setOnClickListener(new C05939());
        addWeaningAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(6), null);
            }
        });
        addDehornedAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(12), null);
            }
        });
        addCastrationAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(13), null);
            }
        });
        addOtherAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (slaughteredAction.getVisibility() == 0) {
                    slaughteredAction.setVisibility(8);
                    deceasedAction.setVisibility(8);
                    soldAction.setVisibility(8);
                    otherIcon.setVisibility(0);
                    return;
                }
                slaughteredAction.setVisibility(0);
                deceasedAction.setVisibility(0);
                soldAction.setVisibility(0);
                otherIcon.setVisibility(8);
            }
        });
        slaughteredAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(7), null);
            }
        });
        deceasedAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(8), null);
            }
        });
        soldAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(9), null);
            }
        });
        markAsReplacementAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(5), null);
            }
        });
        assumeInCalf.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                OfflineAnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(10), null);
            }
        });
        return view;
    }
}
