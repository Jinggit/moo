package com.moocall.moocall.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.moocall.moocall.AnimalDetailsActivity;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.domain.Animal;
import com.moocall.moocall.domain.AnimalHistory;
import com.moocall.moocall.domain.AnimalNotes;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.text.DecimalFormat;
import org.apache.commons.lang3.StringUtils;

public class AnimalDetailsAdapter extends BaseAdapter {
    private AnimalDetailsActivity activity;
    private Animal animal;
    private LayoutInflater inflater;
    private Integer subtype;

    class C05721 implements OnClickListener {
        C05721() {
        }

        public void onClick(View view) {
            AnimalDetailsAdapter.this.activity.openMoocallTagOverlay();
        }
    }

    class C05732 implements OnClickListener {
        C05732() {
        }

        public void onClick(View view) {
            AnimalDetailsAdapter.this.activity.editAnimal();
        }
    }

    class C05754 implements OnClickListener {
        C05754() {
        }

        public void onClick(View view) {
            if (AnimalDetailsAdapter.this.animal.getNotes().size() > 5) {
                AnimalDetailsAdapter.this.animal.setShowMoreNotes(Boolean.valueOf(!AnimalDetailsAdapter.this.animal.getShowMoreNotes().booleanValue()));
                AnimalDetailsAdapter.this.notifyDataSetChanged();
            }
        }
    }

    class C05765 implements OnClickListener {
        C05765() {
        }

        public void onClick(View view) {
            if (AnimalDetailsAdapter.this.animal.getNotes().size() > 5) {
                AnimalDetailsAdapter.this.animal.setShowMoreNotes(Boolean.valueOf(!AnimalDetailsAdapter.this.animal.getShowMoreNotes().booleanValue()));
                AnimalDetailsAdapter.this.notifyDataSetChanged();
            }
        }
    }

    class C05776 implements OnClickListener {
        C05776() {
        }

        public void onClick(View view) {
            AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(14), null);
        }
    }

    class C05787 implements OnClickListener {
        C05787() {
        }

        public void onClick(View view) {
            if (AnimalDetailsAdapter.this.animal.getNotes().size() > 5) {
                AnimalDetailsAdapter.this.animal.setShowMoreNotes(Boolean.valueOf(!AnimalDetailsAdapter.this.animal.getShowMoreNotes().booleanValue()));
                AnimalDetailsAdapter.this.notifyDataSetChanged();
            }
        }
    }

    class C05809 implements OnClickListener {
        C05809() {
        }

        public void onClick(View view) {
            if (AnimalDetailsAdapter.this.animal.getHistory().size() > 5) {
                AnimalDetailsAdapter.this.animal.setShowMoreHistory(Boolean.valueOf(!AnimalDetailsAdapter.this.animal.getShowMoreHistory().booleanValue()));
                AnimalDetailsAdapter.this.notifyDataSetChanged();
            }
        }
    }

    public AnimalDetailsAdapter(AnimalDetailsActivity context, Integer subtype) {
        this.activity = context;
        this.subtype = subtype;
    }

    public Animal getAnimal() {
        return this.animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public int getCount() {
        if (this.animal == null) {
            return 0;
        }
        if (this.animal.getType().intValue() == 4 || this.animal.getType().intValue() < 3) {
            return 6;
        }
        return 5;
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (this.inflater == null) {
            AnimalDetailsActivity animalDetailsActivity = this.activity;
            AnimalDetailsActivity animalDetailsActivity2 = this.activity;
            this.inflater = (LayoutInflater) animalDetailsActivity.getSystemService("layout_inflater");
        }
        switch (i) {
            case 0:
                return addBasic();
            case 1:
                if (this.animal.getType().intValue() == 4 || this.animal.getType().intValue() < 3) {
                    return addCalvingDifficulty();
                }
                return addAdvanced();
            case 2:
                if (this.animal.getType().intValue() == 4 || this.animal.getType().intValue() < 3) {
                    return addAdvanced();
                }
                return addNotes();
            case 3:
                if (this.animal.getType().intValue() == 4 || this.animal.getType().intValue() < 3) {
                    return addNotes();
                }
                return addHistory();
            case 4:
                if (this.animal.getType().intValue() == 4 || this.animal.getType().intValue() < 3) {
                    return addHistory();
                }
                return addEvents();
            case 5:
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
        if (!(this.animal.getMoocallTagNumber() == null || this.animal.getMoocallTagNumber().isEmpty())) {
            animalMoocallTagIcon.setVisibility(0);
            animalMoocallTagIcon.setOnClickListener(new C05721());
        }
        animalImage.setImageBitmap(Utils.getAnimalPlaceholder(this.animal.getType(), this.activity));
        animalName.setText(this.animal.getName());
        animalTag.setText(this.animal.getTagNumber());
        animalBreedText.setText(this.animal.getBreed());
        animalTemperamentText.setText(this.animal.getTemperament());
        animalAgeText.setText(Utils.getTimeShorter(Utils.calculateTime(this.animal.getBirthDateServer() + " 00:00:00", "yyyy-MM-dd HH:mm")));
        animalWeightText.setText(Utils.getWeightText(this.animal.getWeight(), this.activity));
        animalVendorText.setText(this.animal.getVendor());
        animalEditIcon.setOnClickListener(new C05732());
        if (StorageContainer.getAnimalImageMemoryCache().get(this.animal.getTagNumber()) != null) {
            if (this.animal.getImagePath() == null || this.animal.getImagePath().isEmpty()) {
                StorageContainer.getAnimalImageMemoryCache().remove(this.animal.getTagNumber());
                animalImage.setImageBitmap(Utils.getAnimalPlaceholder(this.animal.getType(), this.activity));
            } else {
                animalImage.setImageBitmap((Bitmap) StorageContainer.getAnimalImageMemoryCache().get(this.animal.getTagNumber()));
            }
        }
        return view;
    }

    private View addCalvingDifficulty() {
        View view = this.inflater.inflate(C0530R.layout.calving_difficulty, null);
        ProgressBar herdDifficultyProgress = (ProgressBar) view.findViewById(C0530R.id.herdDifficultyProgress);
        RatingBar herdDifficultyRating = (RatingBar) view.findViewById(C0530R.id.herdDifficultyRating);
        TextView herdDifficultyText = (TextView) view.findViewById(C0530R.id.herdDifficultyText);
        Float difficulty = Float.valueOf(0.8333333f * ((float) this.animal.getDifficulty().intValue()));
        herdDifficultyProgress.setProgress(Math.round(difficulty.floatValue() * 20.0f));
        herdDifficultyRating.setRating(difficulty.floatValue());
        if (this.animal.getDifficulty().intValue() < 3) {
            herdDifficultyText.setText(this.activity.getString(C0530R.string.easy_calver));
        } else if (this.animal.getDifficulty().intValue() < 5) {
            herdDifficultyText.setText(this.activity.getString(C0530R.string.moderate_calver));
        } else {
            herdDifficultyText.setText(this.activity.getString(C0530R.string.difficult_calver));
        }
        return view;
    }

    private View addAdvanced() {
        View view = this.inflater.inflate(C0530R.layout.animal_details_advanced, null);
        RelativeLayout animalAdvancedFirstLayout = (RelativeLayout) view.findViewById(C0530R.id.animalAdvancedFirstLayout);
        TextView animalAdvancedFirstHeadline = (TextView) view.findViewById(C0530R.id.animalAdvancedFirstHeadline);
        TextView animalAdvancedFirstText = (TextView) view.findViewById(C0530R.id.animalAdvancedFirstText);
        RelativeLayout animalAdvancedSecondLayout = (RelativeLayout) view.findViewById(C0530R.id.animalAdvancedSecondLayout);
        TextView animalAdvancedSecondHeadline = (TextView) view.findViewById(C0530R.id.animalAdvancedSecondHeadline);
        TextView animalAdvancedSecondText = (TextView) view.findViewById(C0530R.id.animalAdvancedSecondText);
        RelativeLayout animalAdvancedThirdLayout = (RelativeLayout) view.findViewById(C0530R.id.animalAdvancedThirdLayout);
        TextView animalAdvancedThirdHeadline = (TextView) view.findViewById(C0530R.id.animalAdvancedThirdHeadline);
        TextView animalAdvancedThirdText = (TextView) view.findViewById(C0530R.id.animalAdvancedThirdText);
        RelativeLayout animalAdvancedFourthLayout = (RelativeLayout) view.findViewById(C0530R.id.animalAdvancedFourthLayout);
        TextView animalAdvancedFourthHeadline = (TextView) view.findViewById(C0530R.id.animalAdvancedFourthHeadline);
        TextView animalAdvancedFourthText = (TextView) view.findViewById(C0530R.id.animalAdvancedFourthText);
        RelativeLayout animalAdvancedFifthLayout = (RelativeLayout) view.findViewById(C0530R.id.animalAdvancedFifthLayout);
        TextView animalAdvancedFifthHeadline = (TextView) view.findViewById(C0530R.id.animalAdvancedFifthHeadline);
        TextView animalAdvancedFifthText = (TextView) view.findViewById(C0530R.id.animalAdvancedFifthText);
        RelativeLayout animalAdvancedSixthLayout = (RelativeLayout) view.findViewById(C0530R.id.animalAdvancedSixthLayout);
        TextView animalAdvancedSixthHeadline = (TextView) view.findViewById(C0530R.id.animalAdvancedSixthHeadline);
        TextView animalAdvancedSixthText = (TextView) view.findViewById(C0530R.id.animalAdvancedSixthText);
        RelativeLayout animalAdvancedSeventhLayout = (RelativeLayout) view.findViewById(C0530R.id.animalAdvancedSeventhLayout);
        TextView animalAdvancedSeventhHeadline = (TextView) view.findViewById(C0530R.id.animalAdvancedSeventhHeadline);
        TextView animalAdvancedSeventhText = (TextView) view.findViewById(C0530R.id.animalAdvancedSeventhText);
        RelativeLayout animalAdvancedEightLayout = (RelativeLayout) view.findViewById(C0530R.id.animalAdvancedEightLayout);
        TextView animalAdvancedEightHeadline = (TextView) view.findViewById(C0530R.id.animalAdvancedEightHeadline);
        TextView animalAdvancedEightText = (TextView) view.findViewById(C0530R.id.animalAdvancedEightText);
        animalAdvancedFirstLayout.setVisibility(8);
        animalAdvancedSecondLayout.setVisibility(8);
        animalAdvancedThirdLayout.setVisibility(8);
        animalAdvancedFourthLayout.setVisibility(8);
        animalAdvancedFifthLayout.setVisibility(8);
        animalAdvancedSixthLayout.setVisibility(8);
        animalAdvancedSeventhLayout.setVisibility(8);
        animalAdvancedEightLayout.setVisibility(8);
        String difficulty = new DecimalFormat("#.##").format(Double.valueOf(0.8333333333333334d * ((double) this.animal.getDifficulty().intValue())));
        switch (this.animal.getType().intValue()) {
            case 1:
            case 3:
                switch (this.subtype.intValue()) {
                    case 1:
                        animalAdvancedFirstLayout.setVisibility(0);
                        animalAdvancedFirstHeadline.setText(this.activity.getString(C0530R.string.insemination_date) + ":");
                        animalAdvancedFirstText.setText(Utils.calculateTime(this.animal.getInseminationDate(), "dd-MM-yyyy"));
                        animalAdvancedSecondLayout.setVisibility(0);
                        animalAdvancedSecondHeadline.setText(this.activity.getString(C0530R.string.days_in_calf) + ":");
                        animalAdvancedSecondText.setText(this.animal.getDaysInCalf().toString());
                        animalAdvancedThirdLayout.setVisibility(0);
                        animalAdvancedThirdHeadline.setText(this.activity.getString(C0530R.string.due_date) + ":");
                        animalAdvancedThirdText.setText(this.animal.getDueDate());
                        animalAdvancedFourthLayout.setVisibility(0);
                        animalAdvancedFourthHeadline.setText(this.activity.getString(C0530R.string.gestation) + ":");
                        animalAdvancedFourthText.setText(this.animal.getGestation().toString());
                        animalAdvancedFifthLayout.setVisibility(0);
                        animalAdvancedFifthHeadline.setText(this.activity.getString(C0530R.string.average_gestation_length) + ":");
                        animalAdvancedFifthText.setText(this.animal.getAvgGestatationLength() + StringUtils.SPACE + this.activity.getString(C0530R.string.days));
                        animalAdvancedSixthLayout.setVisibility(0);
                        animalAdvancedSixthHeadline.setText(this.activity.getString(C0530R.string.number_calves_date) + ":");
                        animalAdvancedSixthText.setText(this.animal.getCalvesNumber().toString());
                        animalAdvancedSeventhLayout.setVisibility(0);
                        animalAdvancedSeventhHeadline.setText(this.activity.getString(C0530R.string.average_birth_weight_calves) + ":");
                        animalAdvancedSeventhText.setText(Utils.getWeightText(this.animal.getAvgBirthWeight().toString(), this.activity));
                        break;
                    case 2:
                    case 3:
                        animalAdvancedFirstLayout.setVisibility(0);
                        animalAdvancedFirstHeadline.setText(this.activity.getString(C0530R.string.last_cycle) + ":");
                        animalAdvancedFirstText.setText(Utils.calculateTime(this.animal.getCycleDate(), "dd-MM-yyyy"));
                        animalAdvancedSecondLayout.setVisibility(0);
                        animalAdvancedSecondHeadline.setText(this.activity.getString(C0530R.string.days_since_cycle) + ":");
                        animalAdvancedSecondText.setText(this.animal.getDaysSinceCycle().toString());
                        animalAdvancedThirdLayout.setVisibility(0);
                        animalAdvancedThirdHeadline.setText(this.activity.getString(C0530R.string.number_of_cycles) + ":");
                        animalAdvancedThirdText.setText(this.animal.getCyclesNumber().toString());
                        animalAdvancedFourthLayout.setVisibility(0);
                        animalAdvancedFourthHeadline.setText(this.activity.getString(C0530R.string.last_calving_date) + ":");
                        animalAdvancedFourthText.setText(Utils.calculateTime(this.animal.getLastCalving(), "dd-MM-yyyy"));
                        break;
                    case 4:
                        animalAdvancedFirstLayout.setVisibility(0);
                        animalAdvancedFirstHeadline.setText(this.activity.getString(C0530R.string.last_in_heat) + ":");
                        animalAdvancedFirstText.setText(Utils.calculateTime(this.animal.getInHeatDate(), "dd-MM-yyyy"));
                        animalAdvancedSecondLayout.setVisibility(0);
                        animalAdvancedSecondHeadline.setText(this.activity.getString(C0530R.string.last_insemination) + ":");
                        animalAdvancedSecondText.setText(Utils.calculateTime(this.animal.getInseminationDate(), "dd-MM-yyyy"));
                        animalAdvancedThirdLayout.setVisibility(0);
                        animalAdvancedThirdHeadline.setText(this.activity.getString(C0530R.string.bull) + " (" + this.activity.getString(C0530R.string.inseminated_by) + "):");
                        animalAdvancedThirdText.setText(this.animal.getInseminationBull());
                        animalAdvancedFourthLayout.setVisibility(0);
                        animalAdvancedFourthHeadline.setText(this.activity.getString(C0530R.string.days_since_last_insemination) + ":");
                        animalAdvancedFourthText.setText(this.animal.getDaysSinceInsemination().toString());
                        animalAdvancedFifthLayout.setVisibility(0);
                        animalAdvancedFifthHeadline.setText(this.activity.getString(C0530R.string.number_insemination_since_calving) + ":");
                        animalAdvancedFifthText.setText(this.animal.getInseminationNumber().toString());
                        break;
                    default:
                        animalAdvancedFirstLayout.setVisibility(0);
                        animalAdvancedFirstHeadline.setText(this.activity.getString(C0530R.string.birth_date) + ":");
                        animalAdvancedFirstText.setText(this.animal.getBirthDateText());
                        animalAdvancedSecondLayout.setVisibility(0);
                        animalAdvancedSecondHeadline.setText(this.activity.getString(C0530R.string.keep_as_replacement) + ":");
                        animalAdvancedSecondText.setText(this.animal.getReplacement());
                        animalAdvancedThirdLayout.setVisibility(0);
                        animalAdvancedThirdHeadline.setText(this.activity.getString(C0530R.string.number_of_calves_to_date) + ":");
                        animalAdvancedThirdText.setText(this.animal.getCalvesNumber().toString());
                        animalAdvancedFourthLayout.setVisibility(0);
                        animalAdvancedFourthHeadline.setText(this.activity.getString(C0530R.string.average_birth_weight_of_calves) + ":");
                        animalAdvancedFourthText.setText(Utils.getWeightText(this.animal.getAvgBirthWeight().toString(), this.activity));
                        break;
                }
            case 2:
                animalAdvancedFirstLayout.setVisibility(0);
                animalAdvancedFirstHeadline.setText(this.activity.getString(C0530R.string.calves_sired) + ":");
                animalAdvancedFirstText.setText(this.animal.getCalvesSired().toString());
                animalAdvancedSecondLayout.setVisibility(0);
                animalAdvancedSecondHeadline.setText(this.activity.getString(C0530R.string.average_calving_difficulty) + ":");
                animalAdvancedSecondText.setText(difficulty + "/5");
                animalAdvancedThirdLayout.setVisibility(0);
                animalAdvancedThirdHeadline.setText(this.activity.getString(C0530R.string.average_offspring_birth_weight) + ":");
                animalAdvancedThirdText.setText(Utils.getWeightText(this.animal.getAvgOffspringWeight().toString(), this.activity));
                animalAdvancedFourthLayout.setVisibility(0);
                animalAdvancedFourthHeadline.setText(this.activity.getString(C0530R.string.average_gestation_length) + ":");
                animalAdvancedFourthText.setText(this.animal.getAvgGestatationLength() + StringUtils.SPACE + this.activity.getString(C0530R.string.days));
                break;
            case 4:
                animalAdvancedFirstLayout.setVisibility(0);
                animalAdvancedFirstHeadline.setText(this.activity.getString(C0530R.string.birth_date) + ":");
                animalAdvancedFirstText.setText(this.animal.getBirthDateText());
                animalAdvancedSecondLayout.setVisibility(0);
                animalAdvancedSecondHeadline.setText(this.activity.getString(C0530R.string.calves_sired) + ":");
                animalAdvancedSecondText.setText(this.animal.getCalvesSired().toString());
                animalAdvancedThirdLayout.setVisibility(0);
                animalAdvancedThirdHeadline.setText(this.activity.getString(C0530R.string.average_calving_difficulty) + ":");
                animalAdvancedThirdText.setText(difficulty + "/5");
                animalAdvancedFourthLayout.setVisibility(0);
                animalAdvancedFourthHeadline.setText(this.activity.getString(C0530R.string.average_offspring_birth_weight) + ":");
                animalAdvancedFourthText.setText(Utils.getWeightText(this.animal.getAvgOffspringWeight().toString(), this.activity));
                animalAdvancedFifthLayout.setVisibility(0);
                animalAdvancedFifthHeadline.setText(this.activity.getString(C0530R.string.average_gestation_length) + ":");
                animalAdvancedFifthText.setText(this.animal.getAvgGestatationLength() + StringUtils.SPACE + this.activity.getString(C0530R.string.days));
                break;
            case 5:
            case 6:
                animalAdvancedFirstLayout.setVisibility(0);
                animalAdvancedFirstHeadline.setText(this.activity.getString(C0530R.string.birth_date) + ":");
                animalAdvancedFirstText.setText(this.animal.getBirthDateText());
                animalAdvancedSecondLayout.setVisibility(0);
                animalAdvancedSecondHeadline.setText(this.activity.getString(C0530R.string.tagged_dehorned) + ":");
                animalAdvancedSecondText.setText(this.animal.getTagged());
                animalAdvancedThirdLayout.setVisibility(0);
                animalAdvancedThirdHeadline.setText(this.activity.getString(C0530R.string.average_weight_gain_per_day) + ":");
                animalAdvancedThirdText.setText(this.animal.getAvgWeightGainDay());
                animalAdvancedFourthLayout.setVisibility(0);
                animalAdvancedFourthHeadline.setText(this.activity.getString(C0530R.string.keep_as_replacement) + ":");
                animalAdvancedFourthText.setText(this.animal.getReplacement());
                break;
            case 7:
            case 8:
                animalAdvancedFirstLayout.setVisibility(0);
                animalAdvancedFirstHeadline.setText(this.activity.getString(C0530R.string.birth_date) + ":");
                animalAdvancedFirstText.setText(this.animal.getBirthDateText());
                animalAdvancedSecondLayout.setVisibility(0);
                animalAdvancedSecondHeadline.setText(this.activity.getString(C0530R.string.calving_status) + ":");
                animalAdvancedSecondText.setText(this.animal.getCalvingStatus());
                animalAdvancedThirdLayout.setVisibility(0);
                animalAdvancedThirdHeadline.setText(this.activity.getString(C0530R.string.condition_at_birth) + ":");
                animalAdvancedThirdText.setText(this.animal.getConditionAtBirth());
                animalAdvancedFourthLayout.setVisibility(0);
                animalAdvancedFourthHeadline.setText(this.activity.getString(C0530R.string.dam) + ":");
                animalAdvancedFourthText.setText(this.animal.getDam());
                animalAdvancedFifthLayout.setVisibility(0);
                animalAdvancedFifthHeadline.setText(this.activity.getString(C0530R.string.sire) + ":");
                animalAdvancedFifthText.setText(this.animal.getSire());
                animalAdvancedSixthLayout.setVisibility(0);
                animalAdvancedSixthHeadline.setText(this.activity.getString(C0530R.string.tagged_dehorned) + ":");
                animalAdvancedSixthText.setText(this.animal.getTagged());
                animalAdvancedSeventhLayout.setVisibility(0);
                animalAdvancedSeventhHeadline.setText(this.activity.getString(C0530R.string.average_weight_gain_per_day) + ":");
                animalAdvancedSeventhText.setText(this.animal.getAvgWeightGainDay());
                animalAdvancedEightLayout.setVisibility(0);
                animalAdvancedEightHeadline.setText(this.activity.getString(C0530R.string.keep_as_replacement) + ":");
                animalAdvancedEightText.setText(this.animal.getReplacement());
                break;
            case 9:
                animalAdvancedFirstLayout.setVisibility(0);
                animalAdvancedFirstHeadline.setText(this.activity.getString(C0530R.string.birth_date) + ":");
                animalAdvancedFirstText.setText(this.animal.getBirthDateText());
                animalAdvancedSecondLayout.setVisibility(0);
                animalAdvancedSecondHeadline.setText(this.activity.getString(C0530R.string.castrated) + ":");
                animalAdvancedSecondText.setText(this.animal.getCastrated());
                animalAdvancedThirdLayout.setVisibility(0);
                animalAdvancedThirdHeadline.setText(this.activity.getString(C0530R.string.keep_as_replacement) + ":");
                animalAdvancedThirdText.setText(this.animal.getReplacement());
                break;
        }
        return view;
    }

    private View addNotes() {
        View view = this.inflater.inflate(C0530R.layout.animal_details_notes, null);
        Integer count = Integer.valueOf(this.animal.getNotes().size());
        LinearLayout notesLess = (LinearLayout) view.findViewById(C0530R.id.notesLess);
        ((TextView) view.findViewById(C0530R.id.numNotesItems)).setText(count.toString());
        ImageView addNewNote = (ImageView) view.findViewById(C0530R.id.addNewNote);
        if (this.animal.getNotes().size() > 5 && !this.animal.getShowMoreNotes().booleanValue()) {
            count = Integer.valueOf(5);
        }
        if (this.animal.getNotes().size() <= 5 || !this.animal.getShowMoreNotes().booleanValue()) {
            notesLess.setVisibility(8);
        } else {
            notesLess.setVisibility(0);
        }
        LinearLayout notesRows = (LinearLayout) view.findViewById(C0530R.id.notesRows);
        for (int i = 0; i < count.intValue(); i++) {
            View notesRow = this.activity.getLayoutInflater().inflate(C0530R.layout.notes_row, null);
            TextView notesText = (TextView) notesRow.findViewById(C0530R.id.notesText);
            notesRows.addView(notesRow);
            final AnimalNotes note = (AnimalNotes) this.animal.getNotes().get(i);
            notesText.setText(note.getShowText());
            notesRow.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    AnimalDetailsAdapter.this.activity.noteClicked(note);
                }
            });
        }
        view.setOnClickListener(new C05754());
        notesLess.setOnClickListener(new C05765());
        addNewNote.setOnClickListener(new C05776());
        return view;
    }

    private View addHistory() {
        View view = this.inflater.inflate(C0530R.layout.animal_details_history, null);
        Integer count = Integer.valueOf(this.animal.getHistory().size());
        LinearLayout notesMore = (LinearLayout) view.findViewById(C0530R.id.notesMore);
        if (this.animal.getNotes().size() <= 5 || this.animal.getShowMoreNotes().booleanValue()) {
            notesMore.setVisibility(8);
        } else {
            notesMore.setVisibility(0);
        }
        notesMore.setOnClickListener(new C05787());
        LinearLayout historyLess = (LinearLayout) view.findViewById(C0530R.id.historyLess);
        ((TextView) view.findViewById(C0530R.id.numHistoryItems)).setText(count.toString());
        if (this.animal.getHistory().size() > 5 && !this.animal.getShowMoreHistory().booleanValue()) {
            count = Integer.valueOf(5);
        }
        if (this.animal.getHistory().size() <= 5 || !this.animal.getShowMoreHistory().booleanValue()) {
            historyLess.setVisibility(8);
        } else {
            historyLess.setVisibility(0);
        }
        LinearLayout historyRows = (LinearLayout) view.findViewById(C0530R.id.historyRows);
        for (int i = 0; i < count.intValue(); i++) {
            View historyRow = this.activity.getLayoutInflater().inflate(C0530R.layout.history_row, null);
            TextView historyTime = (TextView) historyRow.findViewById(C0530R.id.historyTime);
            TextView historyText = (TextView) historyRow.findViewById(C0530R.id.historyText);
            final AnimalHistory history = (AnimalHistory) this.animal.getHistory().get(i);
            ((TextView) historyRow.findViewById(C0530R.id.historyName)).setText(history.getName());
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
                    AnimalDetailsAdapter.this.activity.historyClicked(history);
                }
            });
        }
        view.setOnClickListener(new C05809());
        historyLess.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnimalDetailsAdapter.this.animal.getHistory().size() > 5) {
                    AnimalDetailsAdapter.this.animal.setShowMoreHistory(Boolean.valueOf(!AnimalDetailsAdapter.this.animal.getShowMoreHistory().booleanValue()));
                    AnimalDetailsAdapter.this.notifyDataSetChanged();
                }
            }
        });
        return view;
    }

    private View addEvents() {
        View view = this.inflater.inflate(C0530R.layout.animal_details_events, null);
        LinearLayout historyMore = (LinearLayout) view.findViewById(C0530R.id.historyMore);
        if (this.animal.getHistory().size() <= 5 || this.animal.getShowMoreHistory().booleanValue()) {
            historyMore.setVisibility(8);
        } else {
            historyMore.setVisibility(0);
        }
        historyMore.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnimalDetailsAdapter.this.animal.getHistory().size() > 5) {
                    AnimalDetailsAdapter.this.animal.setShowMoreHistory(Boolean.valueOf(!AnimalDetailsAdapter.this.animal.getShowMoreHistory().booleanValue()));
                    AnimalDetailsAdapter.this.notifyDataSetChanged();
                }
            }
        });
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
        addWeightAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(2), null);
            }
        });
        addCalvingAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnimalDetailsAdapter.this.activity.addCalving();
            }
        });
        addHeatAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(3), null);
            }
        });
        addInseminationAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (AnimalDetailsAdapter.this.animal.getType().intValue() == 2 || AnimalDetailsAdapter.this.animal.getType().intValue() == 4) {
                    AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(15), null);
                } else {
                    AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(4), null);
                }
            }
        });
        addWeaningAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(6), null);
            }
        });
        addDehornedAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(12), null);
            }
        });
        addCastrationAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(13), null);
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
                AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(7), null);
            }
        });
        deceasedAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(8), null);
            }
        });
        soldAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(9), null);
            }
        });
        markAsReplacementAction.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(5), null);
            }
        });
        assumeInCalf.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                AnimalDetailsAdapter.this.activity.showPreActionOverview(Integer.valueOf(10), null);
            }
        });
        return view;
    }
}
