package com.ssai.gear_alarm_extender.receiver.clock;

import android.content.Context;
import android.content.Intent;
import com.ssai.gear_alarm_extender.receiver.AlertAgent;

/**
 * Created by Saiadian on 16.06.2014.
 */
public class SleepAsAndroidAlertAgent extends AlertAgent {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.urbandroid.sleep.alarmclock.ALARM_ALERT_START".equals(intent.getAction())) {
            watchAlertStart(context);
        } else if ("com.urbandroid.sleep.alarmclock.ALARM_ALERT_DISMISS".equals(intent.getAction()) ||
                "com.urbandroid.sleep.alarmclock.ALARM_SNOOZE_CLICKED_ACTION".equals(intent.getAction())) {
            watchAlertStop(context);
        }
    }

    @Override
    public void onGearAlertStop(Context context) {
        Intent v0 = new Intent();
        v0.setAction("com.urbandroid.sleep.alarmclock.ALARM_DISMISS_CAPTCHA");
        context.sendBroadcast(v0);
    }

    @Override
    public void onGearAlertSnooze(Context context) {
        Intent v0 = new Intent();
        v0.setAction("com.urbandroid.sleep.alarmclock.ALARM_SNOOZE");
        context.sendBroadcast(v0);
    }
}
