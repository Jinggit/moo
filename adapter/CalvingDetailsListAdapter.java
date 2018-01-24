package com.moocall.moocall.adapter;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.domain.Calving;
import com.moocall.moocall.domain.CalvingDetails;
import com.moocall.moocall.util.Utils;
import java.util.List;

public class CalvingDetailsListAdapter extends BaseAdapter {
    private Activity activity;
    private Calving calving;
    private List<CalvingDetails> calvingDetailsList;
    private LayoutInflater inflater;

    public CalvingDetailsListAdapter(Activity activity, List<CalvingDetails> calvingDetailsList, Calving calving) {
        this.activity = activity;
        this.calvingDetailsList = calvingDetailsList;
        this.calving = calving;
    }

    public int getCount() {
        return this.calvingDetailsList.size();
    }

    public Object getItem(int position) {
        return this.calvingDetailsList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.inflater == null) {
            this.inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
        }
        if (convertView == null) {
            convertView = this.inflater.inflate(C0530R.layout.calf_details_row, null);
        }
        final CalvingDetails calvingDetails = (CalvingDetails) this.calvingDetailsList.get(position);
        TextView calfNumber = (TextView) convertView.findViewById(C0530R.id.calfNumber);
        EditText calvingWeight = (EditText) convertView.findViewById(C0530R.id.calvingWeight);
        TextView calfGenderSelect = (TextView) convertView.findViewById(C0530R.id.calfGenderSelect);
        TextView calfPostnatalStatusSelect = (TextView) convertView.findViewById(C0530R.id.calfPostnatalStatusSelect);
        LinearLayout deceaseDateLayout = (LinearLayout) convertView.findViewById(C0530R.id.deceaseDateLayout);
        TextView deceaseDate = (TextView) convertView.findViewById(C0530R.id.deceaseDate);
        ((TextView) convertView.findViewById(C0530R.id.calvingProcessSelect)).setText(calvingDetails.getCalfProcessText());
        calfGenderSelect.setText(calvingDetails.getCalfGenderText());
        calfPostnatalStatusSelect.setText(calvingDetails.getCalfStatusText());
        calvingWeight.setText(Utils.getWeightTextNoUnit(calvingDetails.getWeight(), this.activity));
        if (calvingDetails.getCalfStatusNumber() == null || !calvingDetails.getCalfStatusNumber().equals(Integer.valueOf(4))) {
            deceaseDateLayout.setVisibility(8);
        } else {
            deceaseDateLayout.setVisibility(0);
            if (calvingDetails.getDeceasedDateText() != null) {
                deceaseDate.setText(calvingDetails.getDeceasedDateText());
            }
        }
        if (this.calvingDetailsList.size() > 1) {
            calfNumber.setText(calvingDetails.getTitle());
        }
        calvingWeight.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && keyEvent.getKeyCode() == 66) || i == 6) {
                    calvingDetails.setWeight(textView.getText().toString());
                }
                return false;
            }
        });
        calvingWeight.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    calvingDetails.setWeight(((EditText) v).getText().toString());
                }
            }
        });
        return convertView;
    }
}
