package com.rfbsoft.game.ads;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.utils.Logger;
import com.rfbsoft.ads.AdsProcessor;
import org.robovm.apple.coregraphics.CGSize;
import org.robovm.apple.foundation.NSArray;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.uikit.UIScreen;
import org.robovm.pods.google.mobileads.*;

import static org.robovm.pods.google.mobileads.GADRequest.GADSimulatorID;

public class AdsProcessorImpl implements AdsProcessor {
    private static final Logger log = new Logger(AdsProcessorImpl.class.getName(), Application.LOG_DEBUG);

    static {
        // configure once
        GADMobileAds.sharedInstance().getRequestConfiguration().setTestDeviceIdentifiers(new NSArray<>(GADSimulatorID()));
        GADMobileAds.sharedInstance().start(status -> {
            log.debug("GADMobileAds started with status == " + status);
        });
    }

    private final IOSApplication iosApplication;

    public AdsProcessorImpl(IOSApplication iosApplication) {
        this.iosApplication = iosApplication;
    }


    public void ins5() {
        final GADMultipleAdsAdLoaderOptions gadMultipleAdsAdLoaderOptions = new GADMultipleAdsAdLoaderOptions();
        gadMultipleAdsAdLoaderOptions.setNumberOfAds(5);
        final GADAdLoader gadAdLoaderAdTypeNative = new GADAdLoader(
                "ca-app-pub-3940256099942544/3986624511",
                iosApplication.getUIViewController(),
                new NSArray<>(new NSString("GADAdLoaderAdTypeNativeContent"), new NSString("GADAdLoaderAdTypeNtiveAppInstall")),
                new NSArray<>(gadMultipleAdsAdLoaderOptions)
        );
        gadAdLoaderAdTypeNative.setDelegate(new GADAdLoaderDelegateAdapter());
        gadAdLoaderAdTypeNative.loadRequest(new GADRequest());

    }

    public void loadBannerView() {
        final GADBannerView gadBannerView = new GADBannerView();
        gadBannerView.setAdUnitID("ca-app-pub-3940256099942544/2435281174");
        gadBannerView.setRootViewController(iosApplication.getUIViewController());
        gadBannerView.setDelegate(new GADBannerViewDelegateAdapter());
        final CGSize screenSize = UIScreen.getMainScreen().getBounds().getSize();
        log.debug(String.format("screenSize[%s, %s]", screenSize.getWidth(), screenSize.getHeight()));
        double screenWidth = screenSize.getWidth();
        gadBannerView.setAdSize(GADAdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(screenWidth));
        gadBannerView.loadRequest(new GADRequest());
        iosApplication.getUIViewController().getView().addSubview(gadBannerView);
    }

    public void loadInterstitialAd() {
        GADInterstitialAd.load("ca-app-pub-3940256099942544/4411468910",
                new GADRequest(),
                (gADInterstitialAd, nsError) -> {
                    gADInterstitialAd.setFullScreenContentDelegate(new GADFullScreenContentDelegateAdapter());
                    gADInterstitialAd.presentFromRootViewController(iosApplication.getUIViewController());
                });

    }

    public void loadRewardedAd() {
        GADRewardedAd.load("ca-app-pub-3940256099942544/1712485313",
                new GADRequest(),
                (gadRewardedAd, nsError) -> {
                    gadRewardedAd.setFullScreenContentDelegate(new GADFullScreenContentDelegateAdapter());
                    gadRewardedAd.present(iosApplication.getUIViewController(), new Runnable() {
                        @Override
                        public void run() {
                            GADAdReward adReward = gadRewardedAd.getAdReward();
                            log.debug("Initalizing GADRewardedAd getted = " + adReward.getAmount());
                        }
                    });

                });
    }

    public void loadRewardedInterstitialAd() {
        GADRewardedInterstitialAd.load("ca-app-pub-3940256099942544/6978759866", new GADRequest(), (gadRewardedInterstitialAd, nsError) -> {
            gadRewardedInterstitialAd.setFullScreenContentDelegate(new GADFullScreenContentDelegateAdapter());
            gadRewardedInterstitialAd.present(iosApplication.getUIViewController(), () -> {
                GADAdReward adReward = gadRewardedInterstitialAd.getAdReward();
                log.debug("Initalizing GADRewardedInterstitialAd getted = " + adReward.getAmount());
                loadRewardedInterstitialAd();

            });

        });
    }
}