package ksmori.hu.ait.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Date;

public class MyTimeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean enabled = false;

    public class MyTimeThread extends Thread {
        public void run() {
            Handler handler = new Handler(MyTimeService.this.getMainLooper());

            while (enabled) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyTimeService.this,
                                "Time: "+new Date(System.currentTimeMillis()).toString(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });

                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        enabled = true;
        new MyTimeThread().start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        enabled = false;
        super.onDestroy();
    }
}