package com.codester.maris.interactivebatteryinfo;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {

    public TextView contentTxt,tv2,tv3;
    private Switch sw;
    SharedPreferences sharedPreferences;

    public BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level", 0);
            contentTxt.setText(String.valueOf(level) + "% ");

          if (level == 100){
              Toast.makeText(getApplicationContext(), "Maximum battery capacity reached", Toast.LENGTH_SHORT).show();
              addNot();
              if(sw.isChecked()){
                  final MediaPlayer sound = MediaPlayer.create(MainActivity.this, R.raw.overcharge);
                  sound.start();
              }
            }
          else if(level == 2){
              Toast.makeText(getApplicationContext(), "Im about to die, save me human", Toast.LENGTH_SHORT).show();
              addNot2();
              if(sw.isChecked()){
                  final MediaPlayer sound = MediaPlayer.create(MainActivity.this, R.raw.die);
                  sound.start();
              }
          }
            
        }
    };//end BroadcastReceiver

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentTxt = (TextView) this.findViewById(R.id.tv1);
        tv2 = (TextView) this.findViewById(R.id.tv2);
        tv3 = (TextView) this.findViewById(R.id.tv3);
        sw = (Switch) findViewById(R.id.sw);

        Intent i = new Intent(this, MyService.class);
        startService(i);

        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));



        //reading switch saved data
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sw.setChecked(sharedPreferences.getBoolean("switch", false));  //default is false


        //set up switch on or off if the button gets clicked
        sw.setOnClickListener(new Switch.OnClickListener() {
            public void onClick(View v) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("switch", sw.isChecked());
                editor.apply();
            }
        });

    }//end onCreate




    private void addNot() {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_bat_not)
                        .setContentTitle("Interactive Battery Info")
                        .setContentText("Human please save me from this torture, 100% battery reached");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }//end addNot method

    private void addNot2() {
        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_bat_not)
                        .setContentTitle("Interactive Battery Info")
                        .setContentText("Human i'm about to die, please save me. 2% battery left!");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }//end addNot2 method

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Intent i = new Intent(this, MyService.class);
        startService(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i = new Intent(this, MyService.class);
        startService(i);
    }

    @Override
    protected void onStop() {
        stopService(new Intent(getBaseContext(), MyService.class));
        super.onStop();
        Intent i = new Intent(this, MyService.class);
        startService(i);
    }

}//end MainAcitivty class