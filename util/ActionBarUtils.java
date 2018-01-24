package com.moocall.moocall.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ActionBarUtils {
    public static void setHasEmbeddedTabs(Object inActionBar, boolean inHasEmbeddedTabs) {
        Class<?> actionBarClass = inActionBar.getClass();
        if ("android.support.v7.app.ActionBarImplJBMR2".equals(actionBarClass.getName())) {
            actionBarClass = actionBarClass.getSuperclass().getSuperclass();
        } else if ("android.support.v7.app.ActionBarImplJB".equals(actionBarClass.getName())) {
            actionBarClass = actionBarClass.getSuperclass();
        }
        try {
            Field actionBarField = actionBarClass.getDeclaredField("mActionBar");
            actionBarField.setAccessible(true);
            inActionBar = actionBarField.get(inActionBar);
            actionBarClass = inActionBar.getClass();
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e2) {
        } catch (NoSuchFieldException e3) {
        }
        try {
            Method method = actionBarClass.getDeclaredMethod("setHasEmbeddedTabs", new Class[]{Boolean.TYPE});
            method.setAccessible(true);
            method.invoke(inActionBar, new Object[]{Boolean.valueOf(inHasEmbeddedTabs)});
        } catch (NoSuchMethodException e4) {
        } catch (InvocationTargetException e5) {
        } catch (IllegalAccessException e6) {
        } catch (IllegalArgumentException e7) {
        }
    }
}
