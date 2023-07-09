package com.rfbsoft.ads;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.utils.Logger;

public class AdsProcessorAdaptor implements AdsProcessor {

    private static final Logger log = new Logger(AdsProcessorAdaptor.class.getName(), Application.LOG_DEBUG);

    @Override
    public void loadRewardedInterstitialAd() {
        log.debug("loadRewardedInterstitialAd");

    }

    @Override
    public void loadRewardedAd() {
        log.debug("loadRewardedAd");

    }

    @Override
    public void loadInterstitialAd() {
        log.debug("loadInterstitialAd");

    }

    @Override
    public void loadBannerView() {
        log.debug("loadBannerView");

    }

    @Override
    public void ins5() {
        log.debug("ins5");

    }
}
