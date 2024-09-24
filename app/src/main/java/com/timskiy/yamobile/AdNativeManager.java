package com.timskiy.yamobile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;

public class AdNativeManager {

    //    private Activity activity;
    private Context context;
    private FrameLayout adContainerView;
    private AdLoader mAdLoader;
    private String adUnitId;

    private NativeAd currentNativeAd;

    public AdNativeManager(Context context, FrameLayout adContainerView, String adUnitId) {
        this.context = context;
        this.adContainerView = adContainerView;
        this.adUnitId = adUnitId;
    }

    public void loadNativeAd() {
        mAdLoader = createNaitveAdLoaderMob();

        if (mAdLoader != null) {
            mAdLoader.loadAd(new AdRequest.Builder().build());
        }
    }

    private AdLoader createNaitveAdLoaderMob() {
        if (mAdLoader != null) {
            return mAdLoader;
        }

        AdLoader newAdloader = new AdLoader.Builder(context, adUnitId)
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {


                        NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(context)
                                .inflate(R.layout.ad_native, adContainerView, false);

                        populateNativeAdView(nativeAd, nativeAdView);
                        adContainerView.removeAllViews();
                        adContainerView.addView(nativeAdView);
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        log("AdLoaded");
                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        log("AdImpression");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        log(loadAdError.getMessage() + ", errorCode: " + loadAdError.getCode());
                    }
                })
                .build();
        return newAdloader;
    }

    private void populateNativeAdView(@NonNull NativeAd nativeAd, NativeAdView nativeAdView) {
        if (currentNativeAd != null) {
            currentNativeAd.destroy();
        }
        currentNativeAd = nativeAd;

        nativeAdView.setMediaView(nativeAdView.findViewById(R.id.ad_media));

        nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_icon));
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.ad_rating));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body));
        nativeAdView.setPriceView(nativeAdView.findViewById(R.id.ad_price));
        nativeAdView.setStoreView(nativeAdView.findViewById(R.id.ad_store));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.ad_call_to_action));

        nativeAdView.getMediaView().setMediaContent(currentNativeAd.getMediaContent());
        ((TextView) nativeAdView.getHeadlineView()).setText(currentNativeAd.getHeadline());

        if (currentNativeAd.getIcon() == null) {
            nativeAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(currentNativeAd.getIcon().getDrawable());
        }

        if (currentNativeAd.getAdvertiser() == null) {
            nativeAdView.getAdvertiserView().setVisibility(View.GONE);
        } else {
            ((TextView) nativeAdView.getAdvertiserView()).setText(currentNativeAd.getAdvertiser());
        }

        if (currentNativeAd.getStarRating() == null) {
            nativeAdView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) nativeAdView.getStarRatingView()).setRating(currentNativeAd.getStarRating().floatValue());
        }

        if (currentNativeAd.getBody() == null) {
            nativeAdView.getBodyView().setVisibility(View.GONE);
        } else {
            ((TextView) nativeAdView.getBodyView()).setText(currentNativeAd.getBody());
        }

        if (currentNativeAd.getPrice() == null) {
            nativeAdView.getPriceView().setVisibility(View.GONE);
        } else {
            ((TextView) nativeAdView.getPriceView()).setText(currentNativeAd.getPrice());
        }

        if (currentNativeAd.getStore() == null) {
            nativeAdView.getStoreView().setVisibility(View.GONE);
        } else {
            ((TextView) nativeAdView.getStoreView()).setText(currentNativeAd.getStore());
        }

        if (currentNativeAd.getCallToAction() == null) {
            nativeAdView.getCallToActionView().setVisibility(View.GONE);
        } else {
            ((MaterialButton) nativeAdView.getCallToActionView()).setText(currentNativeAd.getCallToAction());
        }

        nativeAdView.setNativeAd(currentNativeAd);
    }

    public void destroy() {
        if (currentNativeAd != null) {
            currentNativeAd.destroy();
            currentNativeAd = null;
        }
    }

    private void log(String message) {
        Log.d("AD_ERROR", "native| " + message);
    }
}
