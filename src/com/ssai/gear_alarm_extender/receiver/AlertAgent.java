package com.ssai.gear_alarm_extender.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.ssai.gear_alarm_extender.util.ContentProviderUtils;

/**
 * Created by Saiadian on 16.06.2014.
 */
public abstract class AlertAgent extends BroadcastReceiver {

    private static final String TAG = "AlertAgent";

    private static AlertAgent mCurrentAlertAgent = null;

    public abstract void onReceive(Context context, Intent intent);

    public abstract void onGearAlertStop(Context context);

    public abstract void onGearAlertSnooze(Context context);

    public synchronized static void gearAlertStop(Context context) {
        if (mCurrentAlertAgent != null) {
            mCurrentAlertAgent.onGearAlertStop(context);
            mCurrentAlertAgent.watchAlertStop(context);
        }
    }

    public synchronized static void gearAlertSnooze(Context context) {
        if (mCurrentAlertAgent != null) {
            mCurrentAlertAgent.onGearAlertSnooze(context);
            mCurrentAlertAgent.watchAlertStop(context);
        }
    }

    public synchronized void watchAlertStart(Context context) {
        Log.e(TAG, "watchAlertStart started");

        if (mCurrentAlertAgent != null) {
            return;
        }

        int alarmId = ContentProviderUtils.getAlarmId(context);
        if (alarmId == -1) {
            Toast.makeText(context, "Can't find alarm clock", Toast.LENGTH_LONG).show();
            return;
        }
        ContentProviderUtils.setAlarmActive(context, alarmId);
        Log.e(TAG, "Send broadcast intent com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT");
        Intent v0 = new Intent();
        v0.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STARTED_IN_ALERT");
        v0.putExtra("alertAlarmID", alarmId);
        context.sendBroadcast(v0);

        mCurrentAlertAgent = this;
    }

    public synchronized void watchAlertStop(Context context) {
        watchAlertStop(context, true);
    }

    private synchronized void watchAlertStop(Context context, boolean unregister) {
        Log.e(TAG, "watchAlertStop started");

        if (mCurrentAlertAgent == null) {
            return;
        }

        int alarmId = ContentProviderUtils.getAlarmId(context);
        if (alarmId == -1) {
            Toast.makeText(context, "Can't find alarm clock", Toast.LENGTH_LONG).show();
            return;
        }
        ContentProviderUtils.resetAlarmActive(context, alarmId);

        Log.e(TAG, "Send broadcast intent com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT");
        Intent v0 = new Intent();
        v0.setAction("com.samsung.sec.android.clockpackage.alarm.ALARM_STOPPED_IN_ALERT");
        context.sendBroadcast(v0);

        if (unregister) {
            mCurrentAlertAgent = null;
        }
    }

}
