package com.oleksii.simplechat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.oleksii.simplechat.R;

public class SplashScreenActivity extends AppCompatActivity {

    private final String TAG = "SplashScreenActivity";
    private final long SPLASH_TIME = 1000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        Intent intent;

        if (user != null) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, AuthenticationActivity.class);
        }

        new Handler().postDelayed(() -> {
            startActivity(intent);
            SplashScreenActivity.this.finish();
        }, SPLASH_TIME);
    }
}
