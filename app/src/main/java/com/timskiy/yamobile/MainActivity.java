package com.timskiy.yamobile;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import com.yandex.mobile.ads.common.MobileAds;

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
            MobileAds.showDebugPanel(this);
        });
    }

    private void initializeMobileAdsSdk(){
        new Thread(() -> {
            MobileAds.initialize(this, () -> {});
            runOnUiThread(() -> {
                adNative();
            });
        }).start();
    }

    private void adNative(){
        containerNative = findViewById(R.id.ad_view_container);
        String adMob = "demo-native-admob";
        String yandex = "demo-native-app-yandex";
        adNativeManager = new AdNativeManager(this, containerNative, adMob);
        adNativeManager.loadNativeAd();
    }
}