package com.ssai.gear_alarm_extender.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.ssai.gear_alarm_extender.model.AlarmItem;

import java.util.Calendar;

/**
 * Created by Saiadian on 17.06.2014.
 */
public class ContentProviderUtils {

    private static final String TAG = "AlertAgent";
    private static final String ALARM_NAME = "Fit Alarm";

    public static void setAlarmActive(Context ctx, int mid) {
        Cursor cursor = ctx.getContentResolver().query(Uri.parse("content://com.samsung.sec.android.clockpackage/alarm"), null, null, null, null);

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    if (mid == id) {
                        Calendar v1 = Calendar.getInstance();
                        v1.setTimeInMillis(System.currentTimeMillis());

                        ContentValues cv = new ContentValues();
                        cv.put("alarmTime",  v1.get(Calendar.HOUR_OF_DAY) * 100 + v1.get(Calendar.MINUTE));
                        cv.put("active", Integer.valueOf(2));
                        ctx.getContentResolver().update(Uri.parse("content://com.samsung.sec.android.clockpackage/alarm"),
                                cv, "_id = " + mid, null);
                        cursor.close();
                        return;
                    }

                    if(cursor.moveToNext()) {
                        continue;
                    }

                    break;
                }
                while(true);
            }

            cursor.close();
        }
    }

    private static boolean isAlarmAvalible(Context ctx) {
        Cursor cursor = ctx.getContentResolver().query(Uri.parse("content://com.samsung.sec.android.clockpackage/alarm"), null, null, null, null);

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    if (ALARM_NAME.equals(cursor.getString(20))) {
                        cursor.close();
                        return true;
                    }

                    if(cursor.moveToNext()) {
                        continue;
                    }

                    break;
                }
                while(true);
            }
            cursor.close();
        }

        return false;
    }

    public static void resetAlarmActive(Context ctx, int mid) {
        Cursor cursor = ctx.getContentResolver().query(Uri.parse("content://com.samsung.sec.android.clockpackage/alarm"), null, null, null, null);

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    if (mid == id) {
                        ContentValues cv = new ContentValues();
                        cv.put("active", Integer.valueOf(0));
                        ctx.getContentResolver().update(Uri.parse("content://com.samsung.sec.android.clockpackage/alarm"),
                                cv, "_id = " + mid, null);
                        cursor.close();
                        return;
                    }

                    if(cursor.moveToNext()) {
                        continue;
                    }

                    break;
                }
                while(true);
            }
            cursor.close();
        }
    }

    public static int getAlarmId(Context ctx) {
        if (!isAlarmAvalible(ctx)) {
            createNewAlert(ctx);
        }

        Cursor cursor = ctx.getContentResolver().query(Uri.parse("content://com.samsung.sec.android.clockpackage/alarm"), null, null, null, null);

        if(cursor != null) {
            if(cursor.moveToFirst()) {
                do {
                    if (ALARM_NAME.equals(cursor.getString(20))) {
                        int id = cursor.getInt(0);
                        cursor.close();
                        return id;
                    }

                    if(cursor.moveToNext()) {
                        continue;
                    }

                    break;
                }
                while(true);
            }

            cursor.close();
        }

        return -1;
    }

    private static void createNewAlert(Context ctx) {
        AlarmItem item = new AlarmItem();

        Calendar v1 = Calendar.getInstance();
        v1.setTimeInMillis(System.currentTimeMillis());

        item.createTime = v1.getTimeInMillis();
        item.alarmName = ALARM_NAME;
        item.alarmTime = 1 * 100 + 0;
        item.alarmAlertTime = item.createTime;
        item.snoozeDoneCount = 0;
        item.activate = 0;
        v1.set(11, 1);
        v1.set(12, 0);
        v1.set(13, 0);
        v1.set(14, 0);
        if(v1.getTimeInMillis() < item.createTime) {
            v1.add(7, 1);
        }

        item.repeatType |= 1 << (7 - v1.get(7) + 1) * 4 & -16;
        item.repeatType |= 1;
        item.snoozeActivate = true;
        item.snoozeDuration = 0;
        item.snoozeRepeat = 0;
        item.calculateFirstAlertTime();

        ctx.getContentResolver().insert(Uri.parse("content://com.samsung.sec.android.clockpackage/alarm"), item.getContentValues());
    }
}
