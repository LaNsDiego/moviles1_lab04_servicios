package com.example.chrono;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    BoundService mBoundService;
    boolean mServiceBound = false;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BoundService.MyBinder myBinder = (BoundService.MyBinder) iBinder;
            mBoundService = myBinder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mServiceBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView timestampText = findViewById(R.id.timestamp_text);
        Button printTimestampButton = findViewById(R.id.print_timestamp);
        Button stopServiceButton = findViewById(R.id.stop_service);

        printTimestampButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timestampText.setText(mBoundService.getTimestamp());
            }
        });

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mServiceBound){
                    unbindService(mServiceConnection);
                    mServiceBound = false;
                }
                Intent intent = new Intent(MainActivity.this, BoundService.class);
                stopService(intent);
            }
        });

    }


    @Override
    protected  void  onStart() {
        super.onStart();
        Intent intent = new Intent(this, BoundService.class);
        startService(intent);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected  void onStop(){
        super.onStop();
        if(mServiceBound){
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

}
