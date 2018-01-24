package com.moocall.moocall.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v4.util.LruCache;
import com.google.android.gms.iid.InstanceID;
import com.moocall.moocall.C0530R;
import com.moocall.moocall.LoginActivity;
import com.moocall.moocall.domain.Account;
import com.moocall.moocall.domain.Animal;
import com.moocall.moocall.domain.ClientSettings;
import com.moocall.moocall.domain.User;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class StorageContainer {
    private static Account account;
    private static LruCache<String, Bitmap> animalImageMemoryCache;
    public static List<Animal> beefByAgeAllSmartList;
    public static List<Animal> beefByAgeBullsSmartList;
    public static List<Animal> beefByAgeHeifersSmartList;
    public static List<Animal> beefByTagAllSmartList;
    public static List<Animal> beefByTagBullsSmartList;
    public static List<Animal> beefByTagHeifersSmartList;
    public static List<Animal> beefByWeightAllSmartList;
    public static List<Animal> beefByWeightBullsSmartList;
    public static List<Animal> beefByWeightHeifersSmartList;
    public static List<Animal> breedingByAgeAllSmartList;
    public static List<Animal> breedingByAgeCowsSmartList;
    public static List<Animal> breedingByAgeHeifersSmartList;
    public static List<Animal> breedingByTagAllSmartList;
    public static List<Animal> breedingByTagCowsSmartList;
    public static List<Animal> breedingByTagHeifersSmartList;
    public static List<Animal> breedingByWeightAllSmartList;
    public static List<Animal> breedingByWeightCowsSmartList;
    public static List<Animal> breedingByWeightHeifersSmartList;
    public static List<Animal> bullsByAgeAISmartList;
    public static List<Animal> bullsByAgeAllSmartList;
    public static List<Animal> bullsByAgeBreedingSmartList;
    public static List<Animal> bullsByTagAISmartList;
    public static List<Animal> bullsByTagAllSmartList;
    public static List<Animal> bullsByTagBreedingSmartList;
    public static List<Animal> bullsByWeightAISmartList;
    public static List<Animal> bullsByWeightAllSmartList;
    public static List<Animal> bullsByWeightBreedingSmartList;
    public static List<Animal> calved90AllSmartList;
    public static List<Animal> calved90CowSmartList;
    public static List<Animal> calved90HeiferSmartList;
    public static List<Animal> calvesByAgeAllSmartList;
    public static List<Animal> calvesByAgeBullsSmartList;
    public static List<Animal> calvesByAgeHeifersSmartList;
    public static List<Animal> calvesByTagAllSmartList;
    public static List<Animal> calvesByTagBullsSmartList;
    public static List<Animal> calvesByTagHeifersSmartList;
    public static List<Animal> calvesByWeightAllSmartList;
    public static List<Animal> calvesByWeightBullsSmartList;
    public static List<Animal> calvesByWeightHeifersSmartList;
    private static ClientSettings clientSettings;
    public static List<Animal> cycle1724AllSmartList;
    public static List<Animal> cycle1724CowSmartList;
    public static List<Animal> cycle1724HeiferSmartList;
    public static List<Animal> cycle3845AllSmartList;
    public static List<Animal> cycle3845CowSmartList;
    public static List<Animal> cycle3845HeiferSmartList;
    public static List<Animal> cycle90AllSmartList;
    public static List<Animal> cycle90CowSmartList;
    public static List<Animal> cycle90HeiferSmartList;
    public static Integer dbVersion = Integer.valueOf(7);
    public static List<Animal> due7AllSmartList;
    public static List<Animal> due7CowSmartList;
    public static List<Animal> due7HeiferSmartList;
    private static Editor editor;
    public static List<Animal> historicByCulledAllSmartList;
    public static List<Animal> historicByCulledBullsSmartList;
    public static List<Animal> historicByCulledCalvesSmartList;
    public static List<Animal> historicByCulledCowsHeifersSmartList;
    public static List<Animal> historicBySoldAllSmartList;
    public static List<Animal> historicBySoldBullsSmartList;
    public static List<Animal> historicBySoldCalvesSmartList;
    public static List<Animal> historicBySoldCowsHeifersSmartList;
    public static List<Animal> historicByTagAllSmartList;
    public static List<Animal> historicByTagBullsSmartList;
    public static List<Animal> historicByTagCalvesSmartList;
    public static List<Animal> historicByTagCowsHeifersSmartList;
    public static String host;
    public static List<Animal> inCalfAllSmartList;
    public static List<Animal> inCalfCowSmartList;
    public static List<Animal> inCalfHeiferSmartList;
    public static List<Animal> inHeat12AllSmartList;
    public static List<Animal> inHeat12CowSmartList;
    public static List<Animal> inHeat12HeiferSmartList;
    public static List<Animal> inHeat90AllSmartList;
    public static List<Animal> inHeat90CowSmartList;
    public static List<Animal> inHeat90HeiferSmartList;
    public static List<Animal> inseminated1724AllSmartList;
    public static List<Animal> inseminated1724CowSmartList;
    public static List<Animal> inseminated1724HeiferSmartList;
    public static List<Animal> inseminated3845AllSmartList;
    public static List<Animal> inseminated3845CowSmartList;
    public static List<Animal> inseminated3845HeiferSmartList;
    public static List<Animal> inseminated90AllSmartList;
    public static List<Animal> inseminated90CowSmartList;
    public static List<Animal> inseminated90HeiferSmartList;
    public static boolean isLaunch = true;
    private static LruCache<Integer, Bitmap> postImageMemoryCache;
    private static SharedPreferences pref;
    public static String socialHost;
    private static User user;

    public static LruCache<Integer, Bitmap> getPostImageMemoryCache() {
        return postImageMemoryCache;
    }

    public static void setPostImageMemoryCache(LruCache<Integer, Bitmap> postImageMemoryCache) {
        postImageMemoryCache = postImageMemoryCache;
    }

    public static LruCache<String, Bitmap> getAnimalImageMemoryCache() {
        return animalImageMemoryCache;
    }

    public static void setAnimalImageMemoryCache(LruCache<String, Bitmap> animalImageMemoryCache) {
        animalImageMemoryCache = animalImageMemoryCache;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        user = user;
    }

    public static Account getAccount() {
        return account;
    }

    public static void setAccount(Account account) {
        account = account;
    }

    public static void setClientSettings(ClientSettings clientSettings) {
        clientSettings = clientSettings;
    }

    public static Editor getEditor() {
        return editor;
    }

    public static void credentialSetup(SharedPreferences myPref) {
        pref = myPref;
        editor = pref.edit();
    }

    public static void wakeApp(Context context) {
        host = context.getResources().getString(C0530R.string.host);
        socialHost = context.getResources().getString(C0530R.string.socialHost);
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
        if (pref.getString("username", null) != null) {
            Account account = new Account();
            Account.setUsername(pref.getString("username", null));
            Account.setPassword(pref.getString("password", null));
            Account.setName(pref.getString("name", null));
            Account.setMyMoocall(Boolean.valueOf(pref.getBoolean("myMoocall", false)));
            Account.setEmail(pref.getString("email", null));
            Account.setMaxPhones(Integer.valueOf(pref.getInt("maxPhones", 5)));
            Account.setMaxCalving(Integer.valueOf(pref.getInt("maxCalving", 20)));
            Account.setMaxCow(Integer.valueOf(pref.getInt("maxCow", BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT)));
            Account.setUpdate(Boolean.valueOf(pref.getBoolean("update", false)));
        }
    }

    public static Boolean removeCredentialsFromPreferences(final Context context) {
        isLaunch = true;
        setUser(null);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(67108864);
        intent.addFlags(268435456);
        context.startActivity(intent);
        if (editor != null) {
            editor.clear();
            editor.commit();
        }
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("signout", true).apply();
        new Thread(new Runnable() {
            public void run() {
                try {
                    InstanceID.getInstance(context).deleteInstanceID();
                } catch (Exception bug) {
                    bug.printStackTrace();
                }
            }
        }).start();
        return Boolean.valueOf(true);
    }

    public static String getSha1(String password) {
        try {
            return Utils.SHA1(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static String loadFileFromAsset(String fileName, Context context) {
        try {
            InputStream is = context.getAssets().open(fileName);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            String result = new String(buffer, "UTF-8");
            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
