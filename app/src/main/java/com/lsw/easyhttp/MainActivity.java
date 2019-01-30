package com.lsw.easyhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lsw.easyhttp.http.HttpProcessor;
import com.lsw.easyhttp.interfaces.ICallBack;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private static final String HENCODER_URL = "https://api.hencoder.com/author";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpProcessor.getInstance().get(HENCODER_URL, null, new ICallBack() {
                    @Override
                    public void onSuccess(final String str) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(str);
                            }
                        });
                    }

                    @Override
                    public void onFailed(final String str) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(str);
                            }
                        });
                    }
                });
            }
        });
    }

    private void initView() {
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
    }
}
