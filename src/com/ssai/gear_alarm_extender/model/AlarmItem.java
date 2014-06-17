package com.ssai.gear_alarm_extender.model;

import android.content.ContentValues;
import android.util.Log;

import java.util.Calendar;

public final class AlarmItem {
    public int activate;
    public long alarmAlertTime;
    public String alarmName;
    public int alarmSoundTone;
    public int alarmSoundType;
    public int alarmTime;
    public int alarmVolume;
    public long createTime;
    public boolean dailyBriefing;
    public int id;
    public int notificationType;
    public int repeatType;
    public boolean snoozeActivate;
    public int snoozeDoneCount;
    public int snoozeDuration;
    public int snoozeRepeat;
    public String soundUri;
    public boolean subdueActivate;
    public int subdueDuration;
    public int subdueTone;
    public int subdueUri;

    public static final int[] ALARM_SNOOZE_COUNT_TABLE = new int[]{1, 2, 3, 5, 10};
    public static final int[] ALARM_DURATION_TABLE = new int[]{3, 5, 10, 15, 30};

    public AlarmItem() {
        super();
        this.id = -1;
        this.activate = 0;
        this.createTime = -1;
        this.alarmAlertTime = -1;
        this.alarmTime = -1;
        this.repeatType = 0;
        this.notificationType = 0;
        this.snoozeActivate = false;
        this.snoozeDuration = 1;
        this.snoozeRepeat = 2;
        this.snoozeDoneCount = 0;
        this.dailyBriefing = false;
        this.subdueActivate = false;
        this.subdueDuration = 1;
        this.subdueTone = 0;
        this.alarmSoundType = 0;
        this.alarmSoundTone = 0;
        this.alarmVolume = 6;
        this.subdueUri = 0;
        this.soundUri = "";
        this.alarmName = "";
    }

    public void calculateFirstAlertTime() {
        int v11 = 60000;
        String v13 = "currentMillis:";
        String v12 = "alarmAlertTime";
        String v10 = "com.ssai.gear_alarm_extender.model.AlarmItem";
        Log.d(v10, "calculateFirstAlertTime");
        Calendar v0 = Calendar.getInstance();
        long v3 = v0.getTimeInMillis();
        Calendar.getInstance().setTimeInMillis(this.alarmAlertTime);
        Log.d(v10, "calendar:" + v3 + "system:" + System.currentTimeMillis());
        Log.d(v10, v12 + this.alarmAlertTime + v13 + v3);
        Calendar v2 = Calendar.getInstance();
        v2.setTimeInMillis(this.alarmAlertTime);
        v2.add(6, -1);
        this.alarmAlertTime = this.getNextAlertTime(v2);
        if(this.activate == 3) {
            long v5 = this.alarmAlertTime - (((long)(ALARM_DURATION_TABLE[this.subdueDuration] * v11)));
            if(this.alarmAlertTime > v3 && v5 < v3) {
                this.activate = 1;
                return;
            }

            if(v5 > v3) {
                this.alarmAlertTime = v5;
                return;
            }

            if(this.alarmAlertTime >= v3) {
                return;
            }

            v0.setTimeInMillis(this.alarmAlertTime);
            this.alarmAlertTime = this.getNextAlertTime(v2);
            this.alarmAlertTime -= ((long)(ALARM_DURATION_TABLE[this.subdueDuration] * v11));
        }
        else {
            Log.d(v10, v12 + this.alarmAlertTime + v13 + v3);
            if(this.alarmAlertTime >= v3) {
                return;
            }

            v0.setTimeInMillis(this.alarmAlertTime);
            this.alarmAlertTime = this.getNextAlertTime(v0);
        }
    }

    public ContentValues getContentValues() {
        ContentValues v1 = new ContentValues();
        int v0 = 0;
        if(this.snoozeActivate) {
            v0 = 256;
        }

        if(this.dailyBriefing) {
            v0 |= 16;
        }

        if(this.subdueActivate) {
            v0 |= 1;
        }

        v1.put("active", Integer.valueOf(this.activate));
        v1.put("createtime", Long.valueOf(this.createTime));
        v1.put("alerttime", Long.valueOf(this.alarmAlertTime));
        v1.put("alarmtime", Integer.valueOf(this.alarmTime));
        v1.put("repeattype", Integer.valueOf(this.repeatType));
        v1.put("notitype", Integer.valueOf(v0));
        String v2 = "snzactive";
        int v3 = this.snoozeActivate ? 1 : 0;
        v1.put(v2, Integer.valueOf(v3));
        v1.put("snzduration", Integer.valueOf(this.snoozeDuration));
        v1.put("snzrepeat", Integer.valueOf(this.snoozeRepeat));
        v1.put("snzcount", Integer.valueOf(this.snoozeDoneCount));
        v1.put("dailybrief", Boolean.valueOf(this.dailyBriefing));
        v2 = "sbdactive";
        v3 = this.subdueActivate ? 1 : 0;
        v1.put(v2, Integer.valueOf(v3));
        v1.put("sbdduration", Integer.valueOf(this.subdueDuration));
        v1.put("sbdtone", Integer.valueOf(this.subdueTone));
        v1.put("alarmsound", Integer.valueOf(this.alarmSoundType));
        v1.put("alarmtone", Integer.valueOf(this.alarmSoundTone));
        v1.put("volume", Integer.valueOf(this.alarmVolume));
        v1.put("sbduri", Integer.valueOf(this.subdueUri));
        v1.put("alarmuri", this.soundUri);
        v1.put("name", this.alarmName);
        return v1;
    }

    public int getNextAlertDayOffset(Calendar arg8) {
        int v6 = 7;
        int v0 = arg8.get(v6);
        int v1 = 268435456;
        int v2;
        for(v2 = 1; v2 <= v6; ++v2) {
            int v3 = v0 + v2;
            if(v3 > v6) {
                v3 += -7;
            }

            if((this.repeatType >> 4 & v1 >> v3 * 4) > 0) {
                int v5 = v2;
                return v5;
            }
        }

        return 0;
    }

    private long getNextAlertTime(Calendar arg5) {
        Log.d("com.ssai.gear_alarm_extender.model.AlarmItem", "1 getNextAlertTime : " + arg5.getTime().toString());
        arg5.add(6, this.getNextAlertDayOffset(arg5));
        Log.d("com.ssai.gear_alarm_extender.model.AlarmItem", "2 getNextAlertTime : " + arg5.getTime().toString());
        arg5.set(11, this.alarmTime / 100);
        arg5.set(12, this.alarmTime % 100);
        arg5.set(13, 0);
        arg5.set(14, 0);
        Log.d("com.ssai.gear_alarm_extender.model.AlarmItem", "3 getNextAlertTime : " + arg5.getTime().toString());
        return arg5.getTimeInMillis();
    }

    public final String toString() {
        StringBuilder v1 = new StringBuilder();
        Calendar v0 = Calendar.getInstance();
        v0.setTimeInMillis(this.alarmAlertTime);
        v1.append("id : ");
        v1.append(this.id);
        v1.append(", \n");
        v1.append("activate : ");
        v1.append(this.activate);
        v1.append(", \n");
        v1.append("createTime : ");
        v1.append(this.createTime);
        v1.append(", \n");
        v1.append("AlertTime : ");
        v1.append(this.alarmAlertTime);
        v1.append(", \n");
        v1.append("AlertTime(at calendar) : ");
        v1.append(v0.getTime().toString());
        v1.append(", \n");
        v1.append("alarmTime : ");
        v1.append(this.alarmTime);
        v1.append(", \n");
        v1.append("repeatType : 0x");
        v1.append(Integer.toHexString(this.repeatType));
        v1.append(", \n");
        v1.append("notificationType : 0x");
        v1.append(Integer.toHexString(this.notificationType));
        v1.append(", \n");
        v1.append("snoozeActivate : ");
        v1.append(this.snoozeActivate);
        v1.append(", \n");
        v1.append("snoozeDuration : ");
        v1.append(ALARM_DURATION_TABLE[this.snoozeDuration]);
        v1.append(", \n");
        v1.append("snoozeRepeat : ");
        v1.append(ALARM_SNOOZE_COUNT_TABLE[this.snoozeRepeat]);
        v1.append(", \n");
        v1.append("snoozeDoneCount : ");
        v1.append(this.snoozeDoneCount);
        v1.append(", \n");
        v1.append("dailyBriefing : ");
        v1.append(this.dailyBriefing);
        v1.append(", \n");
        v1.append("subdueActivate : ");
        v1.append(this.subdueActivate);
        v1.append(", \n");
        v1.append("subdueDuration : ");
        v1.append(this.subdueDuration);
        v1.append(", \n");
        v1.append("subdueTone : ");
        v1.append(this.subdueTone);
        v1.append(", \n");
        v1.append("alarmSoundType : ");
        v1.append(this.alarmSoundType);
        v1.append(", \n");
        v1.append("alarmSoundTone : ");
        v1.append(this.alarmSoundTone);
        v1.append(", \n");
        v1.append("alarmVolume : ");
        v1.append(this.alarmVolume);
        v1.append(", \n");
        v1.append("subdueUri : ");
        v1.append(this.subdueUri);
        v1.append(", \n");
        v1.append("soundUri : ");
        v1.append(this.soundUri);
        v1.append(", \n");
        v1.append("alarmName : ");
        v1.append(this.alarmName);
        return v1.toString();
    }
}

