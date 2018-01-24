package com.moocall.moocall.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.OfflineManageHerdActivity;
import com.moocall.moocall.db.SensorDbDao.Properties;
import com.moocall.moocall.service.MoocallAnalyticsApplication;
import org.greenrobot.greendao.query.WhereCondition;

public class Dialog {
    private static AlertDialog alertDialog;
    private static Dialog dialog;

    static class C06051 implements OnClickListener {
        C06051() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    static class C06073 implements OnClickListener {
        C06073() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    static class C06084 implements OnClickListener {
        C06084() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
        }
    }

    private Dialog() {
    }

    public static Dialog getInstance() {
        if (dialog == null) {
            dialog = new Dialog();
        }
        return dialog;
    }

    public static AlertDialog alertNoInternetWithMessage(final Context context) {
        Builder alertDialogBuilder = new Builder(context);
        alertDialogBuilder.setMessage(C0530R.string.no_internet).setTitle(C0530R.string.error);
        alertDialogBuilder.setPositiveButton(C0530R.string.ok, new C06051());
        if (((MoocallAnalyticsApplication) ((Activity) context).getApplication()).getDaoSession().getSensorDbDao().queryBuilder().where(Properties.Type.eq(Integer.valueOf(1)), new WhereCondition[0]).list().size() > 0) {
            alertDialogBuilder.setNegativeButton(C0530R.string.go_to_offline_mode, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, OfflineManageHerdActivity.class);
                    intent.addFlags(67108864);
                    intent.addFlags(268435456);
                    context.startActivity(intent);
                }
            });
        }
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        return alertDialog;
    }

    public static AlertDialog alertClockProblemWithMessage(Context context) {
        Builder alertDialogBuilder = new Builder(context);
        alertDialogBuilder.setMessage(C0530R.string.clock_problem).setTitle(C0530R.string.error);
        alertDialogBuilder.setPositiveButton(C0530R.string.ok, new C06073());
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        return alertDialog;
    }

    public static AlertDialog alertWithMessage(Context context) {
        Builder alertDialogBuilder = new Builder(context);
        alertDialogBuilder.setMessage(context.getString(C0530R.string.server_problem)).setTitle(context.getString(C0530R.string.error));
        alertDialogBuilder.setPositiveButton(context.getString(C0530R.string.ok), new C06084());
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        return alertDialog;
    }

    public static AlertDialog alertWithPositiveCallback(final Context context, final AlertDialogCallback<String> callback) {
        final Activity activity = (Activity) context;
        Builder alertDialogBuilder = new Builder(context);
        alertDialogBuilder.setMessage(C0530R.string.no_internet).setTitle(C0530R.string.error);
        alertDialogBuilder.setPositiveButton(C0530R.string.try_again, new OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                callback.alertDialogCallback("");
            }
        });
        alertDialogBuilder.setNegativeButton(C0530R.string.exit, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        if (((MoocallAnalyticsApplication) ((Activity) context).getApplication()).getDaoSession().getSensorDbDao().queryBuilder().where(Properties.Type.eq(Integer.valueOf(1)), new WhereCondition[0]).list().size() > 0) {
            alertDialogBuilder.setNeutralButton(C0530R.string.go_to_offline_mode, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(context, OfflineManageHerdActivity.class);
                    intent.addFlags(67108864);
                    intent.addFlags(268435456);
                    context.startActivity(intent);
                }
            });
        }
        alertDialogBuilder.setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        return alertDialog;
    }
}
