package com.example.rebusmobile;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RebusNeoConnector connector = new RebusNeoConnector(this);
        connector.SendRequest(null, new IResponseListener() {
            @Override
            public void onResponse(Object response) {
                TextView text = (TextView)findViewById(R.id.test);
                text.setText(response.toString());
            }
            @Override
            public void onError(String message) {

            }
        });
    }
}
