package com.example.a310270843.alarm_test;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    static Button btnSet, btnOK;
    static TimePicker timePicker;
    static TextView textViewTime, textViewText, textViewLog;
    static NotificationManager nm;
    static final String BAMA_ALARM_ACTION = "bama.alarm.test";
    AlarmManager am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        MainReciever mr = new MainReciever();
        IntentFilter ifr = new IntentFilter();
        ifr.addAction(BAMA_ALARM_ACTION);
        registerReceiver(mr,ifr);

        btnSet = (Button) findViewById(R.id.btnSet);
        btnOK = (Button) findViewById(R.id.btnOK);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        textViewTime = (TextView) findViewById(R.id.textviewseletetime);
        textViewText = (TextView) findViewById(R.id.editText2);
        textViewLog = (TextView) findViewById(R.id.tvlog);

        timePicker.setVisibility(View.INVISIBLE);
        btnOK.setVisibility(View.INVISIBLE);
        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.setVisibility(View.VISIBLE);
                btnOK.setVisibility(View.VISIBLE);
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker arg0, int hour, int minute) {
                textViewTime.setText(hour + ":" + minute);
            }
        });

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePicker.setVisibility(View.INVISIBLE);
                btnOK.setVisibility(View.INVISIBLE);
            }
        });

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textViewTime.getText() == null || textViewTime.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "时间不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textViewText.getText() == null || textViewText.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "提醒内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(BAMA_ALARM_ACTION);
                intent.putExtra("text", textViewText.getText());
                PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                // We want the alarm to go off 10 seconds from now.
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR, Integer.parseInt(textViewTime.getText().toString().split(":")[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(textViewTime.getText().toString().split(":")[1]));
                calendar.set(Calendar.SECOND, 0);
                // Schedule the alarm!
                am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5 * 60 * 1000, sender);
                Toast.makeText(MainActivity.this, "提醒设置成功！", Toast.LENGTH_SHORT).show();
                log(textViewText.getText() + "：" + textViewTime.getText());
                timePicker.setVisibility(View.INVISIBLE);
                btnOK.setVisibility(View.INVISIBLE);
                char[] ss = "(点击设置时间)".toCharArray();
                textViewTime.setText(ss, 0, ss.length);
                textViewText.setText("".toCharArray(), 0, 0);
            }
        });
    }

    public static void log(String ss) {
        String s = textViewLog.getText().toString() + "\n" + ss;
        textViewLog.setText(s);
    }
}
