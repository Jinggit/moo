package com.moocall.moocall.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.moocall.moocall.C0530R;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    public static final Integer REFRESH_RATE_ON_PAUSE = Integer.valueOf(240000);
    public static final Integer REFRESH_RATE_ON_RESUME = Integer.valueOf(120000);
    public static final Double WEIGHT_CONST = Double.valueOf(0.45359237d);
    protected static final char[] hexArray = "0123456789abcdef".toCharArray();

    static class GetLocationTask extends AsyncTask<String, Void, JSONObject> {
        private Exception exception;

        GetLocationTask() {
        }

        protected JSONObject doInBackground(String... urls) {
            try {
                HttpGet httpGet = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + urls[0] + "," + urls[1] + "&sensor=true");
                HttpClient client = new DefaultHttpClient();
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    InputStream stream = client.execute(httpGet).getEntity().getContent();
                    while (true) {
                        int b = stream.read();
                        if (b != -1) {
                            stringBuilder.append((char) b);
                        }
                        break;
                    }
                } catch (ClientProtocolException e) {
                } catch (IOException e2) {
                }
                JSONObject jSONObject = new JSONObject();
                try {
                    return new JSONObject(stringBuilder.toString());
                } catch (JSONException e3) {
                    e3.printStackTrace();
                    return jSONObject;
                }
            } catch (Exception e4) {
                this.exception = e4;
                return null;
            }
        }

        protected void onPostExecute(JSONObject object) {
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[(bytes.length * 2)];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[(j * 2) + 1] = hexArray[v & 15];
        }
        return new String(hexChars);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService("input_method");
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        view.setFocusable(false);
        view.setFocusableInTouchMode(true);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String calcHmac(String src, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key.getBytes(), mac.getAlgorithm()));
            return bytesToHex(mac.doFinal(src.getBytes()));
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static long getUnixTs() {
        return System.currentTimeMillis() / 1000;
    }

    public static final String md5(String s) {
        String MD5 = "MD5";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(aMessageDigest & 255);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static final String sha256(String s) {
        String SHA256 = "SHA-256";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(aMessageDigest & 255);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String fromServerToNormal(String datetime) {
        if (datetime == null) {
            try {
                return "N/A";
            } catch (Exception e) {
                e.printStackTrace();
                return "N/A";
            }
        } else if (datetime.equals("0000-00-00")) {
            return "N/A";
        } else {
            Date deviceTime = new SimpleDateFormat("yyyy-MM-dd").parse(datetime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(deviceTime);
            return new SimpleDateFormat("dd-MM-yyyy").format(calendar.getTime());
        }
    }

    public static String fromNormalToServer(String datetime) {
        if (datetime == null) {
            try {
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else if (datetime.equals("00-00-0000")) {
            return "";
        } else {
            Date deviceTime = new SimpleDateFormat("dd-MM-yyyy").parse(datetime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(deviceTime);
            return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        }
    }

    public static String encode(String url) {
        try {
            String urlencoded = URLEncoder.encode(url, "UTF-8");
            Log.d("url encode ", urlencoded);
            return urlencoded;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return url;
        }
    }

    public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] result = MessageDigest.getInstance("SHA1").digest(text.getBytes());
        StringBuffer sb = new StringBuffer();
        for (byte b : result) {
            sb.append(Integer.toString((b & 255) + 256, 16).substring(1));
        }
        return sb.toString();
    }

    public static String calculateTime(String time, String returnFormat) {
        if (time == null) {
            try {
                return "N/A";
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (time.equals("0000-00-00 00:00:00")) {
            return "N/A";
        } else {
            Date deviceTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            boolean isDateSummer = tz.inDaylightTime(new Date());
            long hours = TimeUnit.MILLISECONDS.toHours((long) tz.getRawOffset());
            long minutes = Math.abs(TimeUnit.MILLISECONDS.toMinutes((long) tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours));
            if (hours < 0) {
                minutes *= -1;
            }
            calendar.setTime(deviceTime);
            calendar.add(10, (int) hours);
            if (isDateSummer) {
                calendar.add(10, 1);
            }
            calendar.add(12, (int) minutes);
            return new SimpleDateFormat(returnFormat).format(calendar.getTime());
        }
    }

    public static String calculateCetTime(String time) {
        if (time != null) {
            try {
                if (!(time.equals("") || time.equals("0000-00-00 00:00:00"))) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    format.setTimeZone(TimeZone.getDefault());
                    Date localDate = format.parse(time);
                    format.setTimeZone(TimeZone.getTimeZone("UTC"));
                    return format.format(localDate);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return "";
    }

    public static String calculateDueDate(String time, Integer gestation) {
        if (time != null) {
            try {
                if (!(time.equals("") || time.equals("0000-00-00 00:00:00"))) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = format.parse(time);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(5, gestation.intValue());
                    return format.format(calendar.getTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    public static String calculateShortTime(String time, String returnFormat) {
        if (time == null) {
            try {
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        if (time.equals("0000-00-00")) {
            return "";
        }
        Date deviceTime = new SimpleDateFormat("yyyy-MM-dd").parse(time);
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = TimeZone.getDefault();
        boolean isDateSummer = tz.inDaylightTime(new Date());
        long hours = TimeUnit.MILLISECONDS.toHours((long) tz.getRawOffset());
        long minutes = Math.abs(TimeUnit.MILLISECONDS.toMinutes((long) tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours));
        if (hours < 0) {
            minutes *= -1;
        }
        calendar.setTime(deviceTime);
        DateFormat df = DateFormat.getTimeInstance();
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        String utcTime = df.format(new Date());
        if (utcTime.contains(":")) {
            calendar.set(11, Integer.parseInt(utcTime.split("\\:")[0]));
            calendar.set(12, Integer.parseInt(utcTime.split("\\:")[1]));
        } else {
            calendar.set(11, Integer.parseInt(utcTime.split("\\.")[0]));
            calendar.set(12, Integer.parseInt(utcTime.split("\\.")[1]));
        }
        calendar.add(10, (int) hours);
        if (isDateSummer) {
            calendar.add(10, 1);
        }
        calendar.add(12, (int) minutes);
        return new SimpleDateFormat(returnFormat).format(calendar.getTime());
    }

    public static Bitmap getResizedPostBitmap(Bitmap bm, Context context, Boolean landscape) {
        int land;
        int scaleDimensionHeight;
        int scaleDimensionWidth;
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        int width = bm.getWidth();
        int height = bm.getHeight();
        if (dm.widthPixels > dm.heightPixels) {
            land = dm.heightPixels;
        } else {
            land = dm.widthPixels;
        }
        if (landscape.booleanValue()) {
            scaleDimensionHeight = (int) (((double) land) * 0.4d);
            scaleDimensionWidth = (scaleDimensionHeight * width) / height;
        } else {
            scaleDimensionWidth = (int) (((double) land) * 0.4d);
            scaleDimensionHeight = (scaleDimensionWidth * height) / width;
        }
        float scaleWidth = ((float) scaleDimensionWidth) / ((float) width);
        float scaleHeight = ((float) scaleDimensionHeight) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public static Integer getResizedPostBitmapHeight(int height, int width, Context context) {
        int scaleDimensionHeight;
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        if (dm.widthPixels > dm.heightPixels) {
            scaleDimensionHeight = (int) (((double) dm.heightPixels) * 0.8d);
        } else {
            scaleDimensionHeight = (((int) (((double) dm.widthPixels) * 0.8d)) * height) / width;
        }
        return Integer.valueOf(scaleDimensionHeight);
    }

    public static Bitmap scaleBitmap(String imagePath) {
        Bitmap scaledBitmap = null;
        Options options = new Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float imgRatio = ((float) actualWidth) / ((float) actualHeight);
        float maxRatio = 1280.0f / 1280.0f;
        if (((float) actualHeight) > 1280.0f || ((float) actualWidth) > 1280.0f) {
            if (imgRatio < maxRatio) {
                actualWidth = (int) (((float) actualWidth) * (1280.0f / ((float) actualHeight)));
                actualHeight = (int) 1280.0f;
            } else if (imgRatio > maxRatio) {
                actualHeight = (int) (((float) actualHeight) * (1280.0f / ((float) actualWidth)));
                actualWidth = (int) 1280.0f;
            } else {
                actualHeight = (int) 1280.0f;
                actualWidth = (int) 1280.0f;
            }
        }
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16384];
        try {
            bmp = BitmapFactory.decodeFile(imagePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Config.ARGB_8888);
        } catch (OutOfMemoryError exception2) {
            exception2.printStackTrace();
        }
        float ratioX = ((float) actualWidth) / ((float) options.outWidth);
        float ratioY = ((float) actualHeight) / ((float) options.outHeight);
        float middleX = ((float) actualWidth) / 2.0f;
        float middleY = ((float) actualHeight) / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - ((float) (bmp.getWidth() / 2)), middleY - ((float) (bmp.getHeight() / 2)), new Paint(2));
        try {
            int orientation = new ExifInterface(imagePath).getAttributeInt("Orientation", 1);
            System.out.println("orientation: " + orientation);
            int rotationInDegrees = exifToDegrees(orientation);
            Matrix matrix = new Matrix();
            if (((float) orientation) != 0.0f) {
                matrix.preRotate((float) rotationInDegrees);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return scaledBitmap;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == 6) {
            return 90;
        }
        if (exifOrientation == 3) {
            return 180;
        }
        if (exifOrientation == 8) {
            return 270;
        }
        return 0;
    }

    public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round(((float) height) / ((float) reqHeight));
            int widthRatio = Math.round(((float) width) / ((float) reqWidth));
            if (heightRatio < widthRatio) {
                inSampleSize = heightRatio;
            } else {
                inSampleSize = widthRatio;
            }
        }
        while (((float) (width * height)) / ((float) (inSampleSize * inSampleSize)) > ((float) ((reqWidth * reqHeight) * 2))) {
            inSampleSize++;
        }
        return inSampleSize;
    }

    public static String calculateCurrentCetTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    public static Bitmap getAnimalPlaceholder(Integer type, Context context) {
        switch (type.intValue()) {
            case 1:
                return BitmapFactory.decodeResource(context.getResources(), C0530R.drawable.cow_placeholder);
            case 2:
            case 4:
            case 6:
            case 9:
                return BitmapFactory.decodeResource(context.getResources(), C0530R.drawable.bull_placeholder);
            case 3:
            case 5:
                return BitmapFactory.decodeResource(context.getResources(), C0530R.drawable.heifer_placeholder);
            case 7:
            case 8:
                return BitmapFactory.decodeResource(context.getResources(), C0530R.drawable.calf_placeholder);
            default:
                return null;
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bm, Context context, int dimension) {
        int scaleDimension = Math.round(((float) dimension) * context.getResources().getDisplayMetrics().density);
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) scaleDimension) / ((float) width);
        float scaleHeight = ((float) scaleDimension) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap, Context context, int dimension) {
        if (bitmap == null) {
            return BitmapFactory.decodeResource(context.getResources(), C0530R.drawable.moocall_grey_cow);
        }
        bitmap = getResizedBitmap(bitmap, context, dimension);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int radius = Math.min(h / 2, w / 2);
        Bitmap output = Bitmap.createBitmap(w + 8, h + 8, Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setStyle(Style.FILL);
        canvas.drawCircle((float) ((w / 2) + 4), (float) ((h / 2) + 4), (float) radius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 4.0f, 4.0f, paint);
        paint.setXfermode(null);
        paint.setStyle(Style.STROKE);
        paint.setColor(-1710619);
        paint.setStrokeWidth(3.0f);
        canvas.drawCircle((float) ((w / 2) + 4), (float) ((h / 2) + 4), (float) radius, paint);
        bitmap.recycle();
        return output;
    }

    public static String getTimeFromPost(String time) {
        if (time != null) {
            try {
                if (time.length() != 0) {
                    Date postDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time);
                    Calendar postDay = Calendar.getInstance();
                    postDay.setTime(postDate);
                    long diff = (long) Math.round((float) ((Calendar.getInstance().getTimeInMillis() - postDay.getTimeInMillis()) / 1000));
                    long years = diff / 31536000;
                    long months = diff / 2592000;
                    long days = diff / 86400;
                    long hours = diff / 3600;
                    long minutes = diff / 60;
                    if (years > 0) {
                        return String.valueOf(years) + " y";
                    } else if (months > 0) {
                        return String.valueOf(months) + " m";
                    } else if (days > 0) {
                        return String.valueOf(days) + " d";
                    } else if (minutes <= 30) {
                        return "Now";
                    } else {
                        if (minutes - (60 * hours) > 30) {
                            return String.valueOf(1 + hours) + " h";
                        }
                        return String.valueOf(hours) + " h";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "N/A";
            }
        }
        return "N/A";
    }

    public static String getTimeShorter(String time) {
        if (time != null) {
            try {
                if (!(time.length() == 0 || time == "N/A")) {
                    Date postDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(time);
                    Calendar postDay = Calendar.getInstance();
                    postDay.setTime(postDate);
                    long diff = (long) Math.round((float) ((Calendar.getInstance().getTimeInMillis() - postDay.getTimeInMillis()) / 1000));
                    long years = diff / 31536000;
                    long months = diff / 2592000;
                    long days = diff / 86400;
                    long hours = diff / 3600;
                    long minutes = diff / 60;
                    if (years > 0) {
                        if (years == 1) {
                            return String.valueOf(years) + " year";
                        }
                        return String.valueOf(years) + " years";
                    } else if (months > 0) {
                        if (months == 1) {
                            return String.valueOf(months) + " month";
                        }
                        return String.valueOf(months) + " months";
                    } else if (days <= 0) {
                        return "Few hours";
                    } else {
                        if (days == 1) {
                            return String.valueOf(days) + " day";
                        }
                        return String.valueOf(days) + " days";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "N/A";
            }
        }
        return "N/A";
    }

    public static String getTimeShorterReverse(String time) {
        if (time != null) {
            try {
                if (time.length() != 0) {
                    if (!time.equals("N/A")) {
                        Date postDate = new SimpleDateFormat("yyyy-MM-dd").parse(time);
                        Calendar postDay = Calendar.getInstance();
                        postDay.setTime(postDate);
                        long diff = (long) Math.round((float) ((postDay.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000));
                        long years = diff / 31536000;
                        long months = diff / 2592000;
                        long days = diff / 86400;
                        if (years > 0) {
                            if (years == 1) {
                                return String.valueOf(years) + " year";
                            }
                            return String.valueOf(years) + " years";
                        } else if (months > 0) {
                            if (months == 1) {
                                return String.valueOf(months) + " month";
                            }
                            return String.valueOf(months) + " months";
                        } else if (days > 0) {
                            if (days == 1) {
                                return String.valueOf(days) + " day";
                            }
                            return String.valueOf(days) + " days";
                        } else if (diff < -80000) {
                            return "overdue";
                        } else {
                            return "Few hours";
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "N/A";
            }
        }
        return "N/A";
    }

    public static String getDeviceLeftTime(String time) {
        if (time != null) {
            try {
                if (time.length() != 0) {
                    Date postDate = new SimpleDateFormat("yyyy-MM-dd").parse(time);
                    Calendar postDay = Calendar.getInstance();
                    postDay.setTime(postDate);
                    long days = ((long) Math.round((float) ((postDay.getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / 1000))) / 86400;
                    if (days > 0) {
                        return String.valueOf(days);
                    }
                    return "N/A";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "N/A";
            }
        }
        return "N/A";
    }

    public static String getDateHeader(String time) {
        if (time != null) {
            try {
                if (time.length() != 0) {
                    Date postDate = new SimpleDateFormat("dd-MM-yyyy").parse(time);
                    Calendar postDay = Calendar.getInstance();
                    postDay.setTime(postDate);
                    long days = ((long) Math.round((float) ((Calendar.getInstance().getTimeInMillis() - postDay.getTimeInMillis()) / 1000))) / 86400;
                    if (days == 0) {
                        return "Today";
                    }
                    if (days == 1) {
                        return "Yesterday";
                    }
                    return time;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "N/A";
            }
        }
        return "N/A";
    }

    public static String getWeightText(String weightText, Activity context) {
        Boolean kgs = Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("kgs", true));
        if (weightText != null) {
            weightText = weightText.replaceAll(",", ".");
        }
        if (weightText.equals("0.00") || weightText.equals("0.000000")) {
            weightText = "0";
        }
        if (!weightText.equals("0")) {
            Double weight = Double.valueOf(Double.parseDouble(weightText));
            if (!kgs.booleanValue()) {
                weight = Double.valueOf(weight.doubleValue() / WEIGHT_CONST.doubleValue());
            }
            weightText = new DecimalFormat("##.00").format(weight);
            if (weightText.substring(weightText.length() - 3).equals(".00")) {
                weightText = weightText.substring(0, weightText.length() - 3);
            }
        }
        if (kgs.booleanValue()) {
            return weightText + " kgs";
        }
        return weightText + " lbs";
    }

    public static String getWeightTextNoUnit(String weightText, Activity context) {
        Boolean kgs = Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("kgs", true));
        if (weightText == null) {
            return weightText;
        }
        weightText = weightText.replaceAll(",", ".");
        if (weightText.equals("0.00") || weightText.equals("0.000000")) {
            weightText = "0";
        }
        if (weightText.equals("0") || weightText.isEmpty()) {
            return weightText;
        }
        Double weight = Double.valueOf(Double.parseDouble(weightText));
        if (!kgs.booleanValue()) {
            weight = Double.valueOf(weight.doubleValue() / WEIGHT_CONST.doubleValue());
        }
        weightText = new DecimalFormat("##.00").format(weight);
        if (weightText.substring(weightText.length() - 3).equals(".00")) {
            return weightText.substring(0, weightText.length() - 3);
        }
        return weightText;
    }

    public static String getWeight(String weightText, Activity context) {
        if (weightText != null) {
            weightText = weightText.replaceAll(",", ".");
        }
        if (weightText.length() <= 0) {
            return weightText;
        }
        Double weight = Double.valueOf(Double.parseDouble(weightText));
        if (!Boolean.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("kgs", true)).booleanValue()) {
            weight = Double.valueOf(weight.doubleValue() * WEIGHT_CONST.doubleValue());
        }
        return weight.toString();
    }
}
