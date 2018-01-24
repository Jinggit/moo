package com.moocall.moocall.async;

import android.content.Context;
import android.os.AsyncTask;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.exception.ErrorException;
import com.moocall.moocall.util.HttpGetContent;
import com.moocall.moocall.util.PhoneDetails;
import com.moocall.moocall.util.StorageContainer;
import com.moocall.moocall.util.Utils;
import java.io.IOException;
import java.util.Date;

public class ErrorReportAsyncTask extends AsyncTask<String, String, String> {
    private Context context;
    private Boolean isPhoneDetails = Boolean.valueOf(false);

    public ErrorReportAsyncTask(Context context) {
        this.context = context;
    }

    public ErrorReportAsyncTask(Context context, Boolean isPhoneDetails) {
        this.context = context;
        this.isPhoneDetails = isPhoneDetails;
    }

    public ErrorReportAsyncTask(Context context, Exception e) {
        this.context = context;
    }

    protected String doInBackground(String... params) {
        String stringUnsignedPartCreateOffer = "/mobile-api/index/index/model/audit/method/send-log/ts/" + Utils.getUnixTs() + "/key/" + Account.getUsername();
        String signatureBalance = Utils.calcHmac(stringUnsignedPartCreateOffer, Account.getPassword());
        String url = StorageContainer.host + stringUnsignedPartCreateOffer;
        String responseString = null;
        if (this.isPhoneDetails.booleanValue()) {
            params[0] = errorTrace(params[0]).toString();
        }
        try {
            responseString = new HttpGetContent(this.context).postContent(url, signatureBalance, params[0]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ErrorException e2) {
            e2.printStackTrace();
        }
        return responseString;
    }

    private StringBuilder errorTrace(String message) {
        StringBuilder report = new StringBuilder();
        report.append("Error Report collected on : ").append(new Date().toString()).append('\n').append('\n');
        report.append("Informations :").append('\n');
        PhoneDetails.addInformation(report, this.context);
        report.append('\n').append('\n');
        report.append("Stack:\n");
        report.append(message);
        report.append('\n');
        report.append("**** End of current Report ***");
        return report;
    }
}
