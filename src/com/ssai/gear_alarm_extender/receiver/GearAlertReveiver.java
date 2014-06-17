package com.ssai.gear_alarm_extender.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.ssai.gear_alarm_extender.util.ContentProviderUtils;

/**
 * Created by Saiadian on 13.06.2014.
 */
public class GearAlertReveiver extends BroadcastReceiver {
    private static final String TAG = "GearAlertReveiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.samsung.sec.android.clockpackage.alarm.ALARM_STOP".equals(intent.getAction())) {
            boolean stop = intent.getExtras().getBoolean("bDismiss", true);

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

            if (stop) {
                Log.e(TAG, "ALARM KILLED");
                AlertAgent.gearAlertStop(context);
            } else {
                Log.e(TAG, "CANCEL SNOOZE");
                AlertAgent.gearAlertSnooze(context);
            }
        }
    }
}
