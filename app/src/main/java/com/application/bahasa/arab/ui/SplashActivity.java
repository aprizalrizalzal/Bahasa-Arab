package com.application.bahasa.arab.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.application.bahasa.arab.R;
import com.application.bahasa.arab.ui.sign.SignInActivity;
import com.application.bahasa.arab.ui.sign.SignUpActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread thread = new Thread(){

            @Override
            public void run() {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    finish();
                }
            }
        };
        thread.start();
    }
}