package ksmori.hu.ait.broadcastdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Sophie on 15-May-17.
 */

public class AirplaneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean state = intent.getBooleanExtra("state", false);
        Toast.makeText(context, "AIRPLANE: : " + state, Toast.LENGTH_SHORT).show();

    }
}
