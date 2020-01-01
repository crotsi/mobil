package com.can.vucutkitle.servis;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.Chronometer;
import com.can.vucutkitle.R;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class sayac_servis extends Service {

    //from uykuService to MainActivity
    final static String KEY_INT_FROM_SERVICE = "KEY_INT_FROM_SERVICE";
    final static String KEY_STRING_FROM_SERVICE = "KEY_STRING_FROM_SERVICE";
    final static String ACTION_UPDATE_CNT = "UPDATE_CNT";
    final static String ACTION_UPDATE_MSG = "UPDATE_MSG";
    final static String ACTION_UPDATE_CR = "UPDATE_CR";
    final static String ACTION_DURAKLAT = "DURAKLAT";
    final static String ACTION_DURDUR = "DURDUR";
    final static String ACTION_BASLAT = "BASLAT";
    final static String KEY_MSG_TO_SERVICE = "KEY_MSG_TO_SERVICE";
    final static String ACTION_MSG_TO_SERVICE = "MSG_TO_SERVICE";
    final static String channelid = "sayac_notify";
    final static int notifyid = 1;
    final static String prefe_sure_adi = "sayac_sure";

    private Chronometer mChronometer;
    MyServiceReceiver myServiceReceiver;
    MyServiceThread myServiceThread;
    String sayac_txt = "-";
    int cnt;
    long kronometre;
    SharedPreferences preferences;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        myServiceReceiver = new MyServiceReceiver();
        myServiceThread = new MyServiceThread();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext().getApplicationContext());
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_MSG_TO_SERVICE);
        intentFilter.addAction(ACTION_DURAKLAT);
        intentFilter.addAction(ACTION_BASLAT);
        registerReceiver(myServiceReceiver, intentFilter);
        mChronometer = new Chronometer(this);
        mChronometer.setBase( SystemClock.elapsedRealtime() - Long.parseLong(preferences.getString("sayac_fark", "")));
        myServiceThread = new MyServiceThread();
        myServiceThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        myServiceThread.setRunning(false);
        unregisterReceiver(myServiceReceiver);
        super.onDestroy();

    }

    public class MyServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_MSG_TO_SERVICE)) {
                String msg = intent.getStringExtra(KEY_MSG_TO_SERVICE);
                msg = new StringBuilder(msg).reverse().toString();
                myServiceThread.setRunning(false);
                myServiceThread.running = false;
                Intent i = new Intent();
                i.setAction(ACTION_UPDATE_MSG);
                i.putExtra(KEY_STRING_FROM_SERVICE, msg);
                sendBroadcast(i);
            } else if (action.equals(ACTION_BASLAT)) {
                mChronometer.setBase(SystemClock.elapsedRealtime() - Long.parseLong(preferences.getString(prefe_sure_adi, "")));
                myServiceThread = new MyServiceThread();
                myServiceThread.setRunning(false);
                myServiceThread.running = false;
                myServiceThread.setRunning(true);
                myServiceThread.running = true;
                myServiceThread.start();
            }
        }
    }

    private class MyServiceThread extends Thread {

        private boolean running;

        public void setRunning(boolean running) {
            this.running = running;
        }

        @Override
        public void run() {
            cnt = 0;
            running = true;
            while (running) {
                try {
                    Thread.sleep(1000);
                    String saat = "";
                    long elapsedMillis = SystemClock.elapsedRealtime()
                            - mChronometer.getBase();
                    int hours = (int) (elapsedMillis / 3600000);
                    int minutes = (int) (elapsedMillis - hours * 3600000) / 60000;
                    int seconds = (int) (elapsedMillis - hours * 3600000 - minutes * 60000) / 1000;
                    if (hours == 0) {
                        saat = "";
                    } else {
                        saat = checkDigit(hours) + ":";
                    }
                    sayac_txt = saat + checkDigit(minutes) + ":" + checkDigit(seconds);
                    Intent intent = new Intent();
                    intent.setAction(ACTION_UPDATE_CR);
                    intent.putExtra(KEY_INT_FROM_SERVICE, String.valueOf(elapsedMillis));
                    sendBroadcast(intent);
                    showNotification(getApplicationContext());
                    cnt++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    public void showNotification2(Context context) {


        Intent yesReceive = new Intent(context, sayac.class);
        yesReceive.setAction(ACTION_DURDUR);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, 0, yesReceive, 0);

        Intent maybeReceive = new Intent(context, sayac.class);
        maybeReceive.setAction(ACTION_DURDUR);
        PendingIntent pendingIntentMaybe = PendingIntent.getActivity(this, 0, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent = new Intent(context, sayac.class);
        intent.setAction(ACTION_DURAKLAT);
        intent.putExtra("kacinci", "88");

        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action duraklat = new NotificationCompat.Action.Builder(R.drawable.btn_duraklat_150, "Duraklat", pIntent).build();
        NotificationCompat.Action durdur = new NotificationCompat.Action.Builder(R.drawable.btn_dur_150, "Durdur", pendingIntentMaybe).build();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext())

                .setSmallIcon(getNotificationIcon())
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_user))
                .setColor(context.getResources().getColor(R.color.colorAccent))
                .setContentTitle(sayac_txt)
                .setContentText("Sayac Çalýþýyor........")

                ;

        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);
        startForeground(1, mBuilder.build());

    }
    private NotificationManager notifManager;
    public void showNotification(Context mContext)
    {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext.getApplicationContext(), channelid);
        Intent ii = new Intent(mContext.getApplicationContext(), sayac.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(sayac_txt);
        bigText.setBigContentTitle("Þuan Sayaç Çalýþýyor");
        bigText.setSummaryText("Þuan Sayaç Çalýþýyor");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_action_pass);
        mBuilder.setContentTitle(sayac_txt);
        mBuilder.setContentText("Þuan Sayaç Çalýþýyor");
        mBuilder.setStyle(bigText);

        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelid,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);
            mNotificationManager.createNotificationChannel(channel);
        }
        startForeground(notifyid, mBuilder.build());

    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_action_pass : R.drawable.ic_action_pass;
    }

}