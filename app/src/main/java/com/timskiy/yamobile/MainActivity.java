package com.timskiy.yamobile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.android.gms.ads.MobileAds;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AdNativeManager adNativeManager;
    private FrameLayout containerNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeMobileAdsSdk();

        Button btnDebug = findViewById(R.id.btnDebug);
        btnDebug.setOnClickListener(v -> {
            MobileAds.openDebugMenu(this, "ca-app-pub-3940256099942544/2247696110");
        });
    }

    private void initializeMobileAdsSdk(){
        new Thread(() -> {
            MobileAds.initialize(this, initializationStatus -> {});
            runOnUiThread(() -> {
                adNative();
            });
        }).start();
    }

    private void adNative(){
        containerNative = findViewById(R.id.ad_view_container);
        String adMob = "ca-app-pub-3940256099942544/2247696110";
        adNativeManager = new AdNativeManager(this, containerNative, adMob);
        adNativeManager.loadNativeAd();
    }

    @Override
    protected void onDestroy() {
        if (adNativeManager != null){
            adNativeManager.destroy();
        }
        super.onDestroy();
    }
}