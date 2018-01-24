package com.moocall.moocall.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.domain.ClientSettings;
import java.util.List;

public class EmailListAdapter extends BaseAdapter {
    private Activity activity;
    private ClientSettings cs;
    private List<String> emailList;
    private LayoutInflater inflater;

    public ClientSettings getCs() {
        return this.cs;
    }

    public void setCs(ClientSettings cs) {
        this.cs = cs;
    }

    public EmailListAdapter(Activity activity, List<String> emailList) {
        this.activity = activity;
        this.emailList = emailList;
    }

    public int getCount() {
        return this.emailList.size();
    }

    public Object getItem(int i) {
        return this.emailList.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (this.inflater == null) {
            this.inflater = (LayoutInflater) this.activity.getSystemService("layout_inflater");
        }
        if (view == null) {
            view = this.inflater.inflate(C0530R.layout.email_list_row, null);
        }
        final int position = i;
        EditText notificationEmail = (EditText) view.findViewById(C0530R.id.notificationEmail);
        notificationEmail.setText((CharSequence) this.emailList.get(i));
        if (!Account.getMyMoocall().booleanValue()) {
            notificationEmail.setEnabled(false);
            notificationEmail.setFocusable(false);
        }
        notificationEmail.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String email = ((EditText) v).getText().toString();
                    if (EmailListAdapter.this.cs != null) {
                        EmailListAdapter.this.emailList.set(position, email);
                        switch (position) {
                            case 0:
                                EmailListAdapter.this.cs.setEmail1(email);
                                break;
                            case 1:
                                EmailListAdapter.this.cs.setEmail2(email);
                                break;
                            case 2:
                                EmailListAdapter.this.cs.setEmail3(email);
                                break;
                        }
                        EmailListAdapter.this.cs.setChanged(Boolean.valueOf(true));
                    }
                }
            }
        });
        view.setOnClickListener(null);
        return view;
    }
}
