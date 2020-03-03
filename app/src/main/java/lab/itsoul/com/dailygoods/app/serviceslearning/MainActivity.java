package lab.itsoul.com.dailygoods.app.serviceslearning;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button startService;
    private Button stopService;
    private Button buttonOnBind;
    private Button buttonOnUnbind;
    private Button buttonGetRandomNumber;
    private TextView randomnumberText;
    private ServiceConnection serviceConnection;
    private Boolean isServiceBound;
    private MyServices myServices;
    private Intent startServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(getString(R.string.service_demo_tag), "The main thred id is " + Thread.currentThread().getId());

        startService = findViewById(R.id.button1);
        stopService = findViewById(R.id.button2);
        buttonOnBind = findViewById(R.id.buttonOnBind);
        buttonOnUnbind = findViewById(R.id.buttonOnUnbind);
        buttonGetRandomNumber = findViewById(R.id.buttonGetRandomNumber);
        randomnumberText = findViewById(R.id.randomnumberText);


        startServiceIntent = new Intent(getApplicationContext(), MyServices.class);

        initListeners();

    }

    private void initListeners() {

        this.startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(startServiceIntent);
            }
        });

        this.stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(startServiceIntent);
            }
        });

        this.buttonOnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindServices();
            }
        });

        this.buttonOnUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unBindServices();
            }
        });

        this.buttonGetRandomNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRandomNumber();
            }
        });
    }

    private void setRandomNumber() {
        if (isServiceBound) {
            this.randomnumberText.setText("Random Number :" + myServices.getRandomNumber());
        } else {
            this.randomnumberText.setText("Service Not Bound");
        }
    }

    private void unBindServices() {
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
    }

    private void bindServices() {
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    Log.d(getString(R.string.service_demo_tag), "component name "+getComponentName().flattenToString());
                    Log.d(getString(R.string.service_demo_tag), "service "+service.toString());
                    MyServices.MyServiceBinder myServiceBinder = (MyServices.MyServiceBinder) service;
                    myServices = myServiceBinder.getService();
                    isServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound = false;
                }
            };
        }
        bindService(startServiceIntent, serviceConnection, BIND_AUTO_CREATE);
    }
}
