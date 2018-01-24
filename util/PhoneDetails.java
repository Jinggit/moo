package com.moocall.moocall.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import java.util.Locale;

public class PhoneDetails {
    public static void addInformation(StringBuilder message, Context context) {
        message.append("Locale: ").append(Locale.getDefault()).append('\n');
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            message.append("Version: ").append(pi.versionName).append('\n');
            message.append("Package: ").append(pi.packageName).append('\n');
        } catch (Exception e) {
            Log.e("CustomExceptionHandler", "Error", e);
            message.append("Could not get Version information for ").append(context.getPackageName());
        }
        message.append("Phone Model ").append(Build.MODEL).append('\n');
        message.append("Android Version : ").append(VERSION.RELEASE).append('\n');
        message.append("Board: ").append(Build.BOARD).append('\n');
        message.append("Brand: ").append(Build.BRAND).append('\n');
        message.append("Device: ").append(Build.DEVICE).append('\n');
        message.append("Host: ").append(Build.HOST).append('\n');
        message.append("ID: ").append(Build.ID).append('\n');
        message.append("Model: ").append(Build.MODEL).append('\n');
        message.append("Product: ").append(Build.PRODUCT).append('\n');
        message.append("Type: ").append(Build.TYPE).append('\n');
        StatFs stat = getStatFs();
        message.append("Total Internal memory: ").append(getTotalInternalMemorySize(stat)).append('\n');
        message.append("Available Internal memory: ").append(getAvailableInternalMemorySize(stat)).append('\n');
    }

    private static StatFs getStatFs() {
        return new StatFs(Environment.getDataDirectory().getPath());
    }

    private static long getAvailableInternalMemorySize(StatFs stat) {
        return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
    }

    private static long getTotalInternalMemorySize(StatFs stat) {
        return ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
    }
}
