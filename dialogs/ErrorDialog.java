package com.moocall.moocall.dialogs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import com.moocall.moocall.C0530R;

public class ErrorDialog {
    private AlertDialog alertDialog;

    class C06121 implements OnClickListener {
        C06121() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    public AlertDialog alert(Context context, String message) {
        Builder alertDialogBuilder = new Builder(context);
        alertDialogBuilder.setMessage(message).setTitle(C0530R.string.error);
        alertDialogBuilder.setPositiveButton(C0530R.string.ok, new C06121());
        alertDialogBuilder.setCancelable(false);
        this.alertDialog = alertDialogBuilder.create();
        return this.alertDialog;
    }
}
