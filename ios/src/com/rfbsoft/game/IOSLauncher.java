package com.rfbsoft.game;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.backends.iosrobovm.objectal.OALSimpleAudio;
import com.rfbsoft.game.ads.AdsProcessorImpl;
import com.rfbsoft.main.Root;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        final IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationLandscape = true;
        config.orientationPortrait = false;
        config.allowIpod = true;
        OALSimpleAudio.sharedInstance().setUseHardwareIfAvailable(true);
        Root root = new Root();
        IOSApplication iosApplication = new IOSApplication(root, config);
        root.adsProcessor = new AdsProcessorImpl(iosApplication);
        return iosApplication;
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}