package com.ssai.gear_alarm_extender.receiver.clock;

import android.content.Context;
import android.content.Intent;
import com.ssai.gear_alarm_extender.receiver.AlertAgent;

/**
 * Created by saiadian on 18.11.14.
 */
public class DeskclockAlertAgent extends AlertAgent {

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("com.android.deskclock.ALARM_ALERT".equals(intent.getAction())) {
            watchAlertStart(context);
        } else if ("com.android.deskclock.ALARM_DONE".equals(intent.getAction())) {
            watchAlertStop(context);
        }
    }

    @Override
    public void onGearAlertStop(Context context) {
        Intent v0 = new Intent();
        v0.setAction("com.android.deskclock.ALARM_DISMISS");
        context.sendBroadcast(v0);
    }

    @Override
    public void onGearAlertSnooze(Context context) {
        Intent v0 = new Intent();
        v0.setAction("com.android.deskclock.ALARM_SNOOZE");
        context.sendBroadcast(v0);
    }

}
