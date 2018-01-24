package com.moocall.moocall.exception;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.async.ErrorReportAsyncTask;
import com.moocall.moocall.util.PhoneDetails;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

public class UnCaughtException implements UncaughtExceptionHandler {
    private static Context context1;
    private Context context;

    public UnCaughtException(Context ctx) {
        this.context = ctx;
        context1 = ctx;
    }

    public void uncaughtException(Thread t, Throwable e) {
        try {
            StringBuilder report = new StringBuilder();
            report.append("Error Report collected on : ").append(new Date().toString()).append('\n').append('\n');
            report.append("Informations :").append('\n');
            PhoneDetails.addInformation(report, this.context);
            report.append('\n').append('\n');
            report.append("Stack:\n");
            Writer result = new StringWriter();
            PrintWriter printWriter = new PrintWriter(result);
            e.printStackTrace(printWriter);
            report.append(result.toString());
            printWriter.close();
            report.append('\n');
            report.append("**** End of current Report ***");
            Log.e(UnCaughtException.class.getName(), "Error while sendErrorMail" + report);
            System.out.println(report);
            new ErrorReportAsyncTask(this.context).execute(new String[]{report.toString()});
            sendErrorMail(report);
        } catch (Throwable ignore) {
            Log.e(UnCaughtException.class.getName(), "Error while sending error e-mail", ignore);
        }
    }

    public void sendErrorMail(final StringBuilder errorContent) {
        final Builder builder = new Builder(this.context);
        new Thread() {

            class C06131 implements OnClickListener {
                C06131() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            }

            class C06142 implements OnClickListener {
                C06142() {
                }

                public void onClick(DialogInterface dialog, int which) {
                    Intent sendIntent = new Intent("android.intent.action.SEND");
                    StringBuilder body = new StringBuilder("car4use");
                    body.append('\n').append('\n');
                    body.append(errorContent).append('\n').append('\n');
                    sendIntent.setType("message/rfc822");
                    sendIntent.putExtra("android.intent.extra.EMAIL", new String[]{"danilo.raovic@bitgear.rs"});
                    sendIntent.putExtra("android.intent.extra.TEXT", body.toString());
                    sendIntent.putExtra("android.intent.extra.SUBJECT", "Moocall App crashed! ");
                    sendIntent.setType("message/rfc822");
                    UnCaughtException.context1.startActivity(sendIntent);
                    System.exit(1);
                }
            }

            public void run() {
                Looper.prepare();
                builder.setTitle("Application has stoped");
                builder.create();
                builder.setNegativeButton(UnCaughtException.this.context.getString(C0530R.string.close), new C06131());
                builder.setPositiveButton(UnCaughtException.this.context.getString(C0530R.string.report_problem), new C06142());
                builder.setMessage(UnCaughtException.this.context.getString(C0530R.string.report_problem_text));
                if ((UnCaughtException.context1 instanceof Activity) && !((Activity) UnCaughtException.context1).isFinishing()) {
                    builder.show();
                }
                Looper.loop();
            }
        }.start();
    }
}
