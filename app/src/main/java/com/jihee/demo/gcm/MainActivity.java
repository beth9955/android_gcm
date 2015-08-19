package com.jihee.demo.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.jihee.demo.gcm.service.RegistrationIntentService;


public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private Button mRegistrationButton;
    private ProgressBar mRegistrationProgressBar;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private TextView mInformationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registBroadcastReceiver();

        mInformationTextView = (TextView) findViewById(R.id.informationTextView);
        mRegistrationProgressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        mRegistrationButton = (Button) findViewById(R.id.registrationButton);

        mInformationTextView.setVisibility(View.GONE);
        mRegistrationProgressBar.setVisibility(ProgressBar.GONE);

        mRegistrationButton.setOnClickListener(new View.OnClickListener() {
            /**
             * 버튼을 클릭하면 토큰을 가져오는 getInstanceIdToken() 메소드를 실행한다.
             * @param view
             */
            @Override
            public void onClick(View view) {
                getInstanceIdToken();
            }
        });


    }


    public void registBroadcastReceiver(){
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals(QuickstartPreferences.REGISTRATION_READY)){
                    // 액션이 READY일 경우
                    mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    mInformationTextView.setVisibility(View.GONE);
                } else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING)){
                    // 액션이 GENERATING일 경우
                    mRegistrationProgressBar.setVisibility(ProgressBar.VISIBLE);
                    mInformationTextView.setVisibility(View.VISIBLE);
                    mInformationTextView.setText(getString(R.string.registering_message_generating));
                } else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE)){
                    // 액션이 COMPLETE일 경우
                    mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    mRegistrationButton.setText(getString(R.string.registering_message_complete));
                    mRegistrationButton.setEnabled(false);
                    String token = intent.getStringExtra("token");
                    mInformationTextView.setText(token);
                }

            }
        };
    }

    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(new Intent(QuickstartPreferences.REGISTRATION_READY));
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        localBroadcastManager.registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        localBroadcastManager.registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
