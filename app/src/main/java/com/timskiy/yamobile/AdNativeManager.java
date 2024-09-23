package com.timskiy.yamobile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;
import com.yandex.mobile.ads.nativeads.NativeAd;
import com.yandex.mobile.ads.nativeads.NativeAdEventListener;
import com.yandex.mobile.ads.nativeads.NativeAdException;
import com.yandex.mobile.ads.nativeads.NativeAdLoadListener;
import com.yandex.mobile.ads.nativeads.NativeAdLoader;
import com.yandex.mobile.ads.nativeads.NativeAdRequestConfiguration;
import com.yandex.mobile.ads.nativeads.NativeAdView;
import com.yandex.mobile.ads.nativeads.NativeAdViewBinder;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AdNativeManager {

    //    private Activity activity;
    private Context activity;
    private FrameLayout adContainerView;
    private NativeAdLoader mNativeAdLoader;
    private String adUnitId;

    public AdNativeManager(Context context, FrameLayout adContainerView, String adUnitId) {
        this.activity = context;
        this.adContainerView = adContainerView;
        this.adUnitId = adUnitId;
    }

    public void loadNativeAd() {
        HashMap<String, String> parameters = new HashMap<String, String>() {{
            put("feedback_image", "feedback_dark_dots");
        }};

        NativeAdRequestConfiguration config = new NativeAdRequestConfiguration.Builder(adUnitId)
                .setShouldLoadImagesAutomatically(true)
                .setParameters(parameters)
                .build();

        mNativeAdLoader = createNativeAdLoader();
        if (mNativeAdLoader != null) {
            mNativeAdLoader.loadAd(config);
        }

    }

    private NativeAdLoader createNativeAdLoader() {
        if (mNativeAdLoader != null) {
            return mNativeAdLoader;
        }

        NativeAdLoader newNativeAdLoader = new NativeAdLoader(activity);
        newNativeAdLoader.setNativeAdLoadListener(new NativeAdLoadListener() {
            @Override
            public void onAdLoaded(@NonNull NativeAd nativeAd) {
                NativeAdView nativeAdView = (NativeAdView)
                        LayoutInflater.from(activity).inflate(R.layout.ad_native, adContainerView, false);

                try {
                    showAd(nativeAd, nativeAdView);
                } catch (Exception e) {
                    Log.e("AD_ERROR", e.getMessage());
                }
                adContainerView.removeAllViews();
                adContainerView.addView(nativeAdView);
            }

            @Override
            public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) {
                String error = adRequestError.getDescription();
                String errorCode = String.valueOf(adRequestError.getCode());
                log("failed: errorCode: " + errorCode + " | description: " + error);
            }
        });
        return newNativeAdLoader;
    }

    private void showAd(NativeAd nativeAd, NativeAdView mNativeAdView) {
        NativeAdViewBinder nativeAdViewBinder = new NativeAdViewBinder.Builder(mNativeAdView)
                .setAgeView(mNativeAdView.findViewById(R.id.ad_age))
                .setBodyView(mNativeAdView.findViewById(R.id.ad_ya_body))
                .setCallToActionView(mNativeAdView.findViewById(R.id.ad_ya_call_to_action))
                .setDomainView(mNativeAdView.findViewById(R.id.ad_ya_domain))
                .setFeedbackView(mNativeAdView.findViewById(R.id.ad_ya_feedback))
                .setIconView(mNativeAdView.findViewById(R.id.ad_ya_icon))
                .setMediaView(mNativeAdView.findViewById(R.id.ad_ya_media))
                .setPriceView(mNativeAdView.findViewById(R.id.ad_ya_price))
                .setSponsoredView(mNativeAdView.findViewById(R.id.ad_sponsored))
                .setTitleView(mNativeAdView.findViewById(R.id.ad_ya_title))
                .setWarningView(mNativeAdView.findViewById(R.id.ad_ya_warning))
                .build();

        try {
            nativeAd.bindNativeAd(nativeAdViewBinder);
            mNativeAdView.setVisibility(View.VISIBLE);
            nativeAd.setNativeAdEventListener(new NativeAdEventListener() {
                @Override
                public void onAdClicked() {

                }

                @Override
                public void onLeftApplication() {

                }

                @Override
                public void onReturnedToApplication() {

                }

                @Override
                public void onImpression(@Nullable ImpressionData impressionData) {
                    log("Ad impression");
                }
            });
        } catch (NativeAdException exception) {
            Log.e("AD_ERROR", exception.getMessage());

        }
    }

    private void log(String message) {
        Log.d("AD_ERROR", "native| " + message);
    }
}
