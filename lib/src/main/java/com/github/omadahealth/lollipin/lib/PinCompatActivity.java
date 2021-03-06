package com.github.omadahealth.lollipin.lib;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.github.omadahealth.lollipin.lib.interfaces.LifeCycleInterface;
import com.github.omadahealth.lollipin.lib.managers.AppLockActivity;

/**
 * Created by callmepeanut on 16-1-14.
 * You must extend this Activity in order to support this library.
 * Then to enable PinCode blocking, you must call
 * {@link com.github.omadahealth.lollipin.lib.managers.LockManager#enableAppLock(android.content.Context, Class)}
 */
public class PinCompatActivity extends AppCompatActivity {
    private static LifeCycleInterface mLifeCycleListener;
    private final BroadcastReceiver mPinCancelledReceiver;

    public PinCompatActivity() {
        super();
        mPinCancelledReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter(AppLockActivity.ACTION_CANCEL);
        LocalBroadcastManager.getInstance(this).registerReceiver(mPinCancelledReceiver, filter);
    }

    @Override
    protected void onResume() {
        onResumeLocal(PinCompatActivity.this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        onPauseLocal(PinCompatActivity.this);
        super.onPause();
    }

    public static void onResumeLocal(Activity activity) {
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityResumed(activity);
        }
    }

    public static void onPauseLocal(Activity activity) {
        if (mLifeCycleListener != null) {
            mLifeCycleListener.onActivityPaused(activity);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPinCancelledReceiver);
    }

    public static void setListener(LifeCycleInterface listener) {
        if (mLifeCycleListener != null) {
            mLifeCycleListener = null;
        }
        mLifeCycleListener = listener;
    }

    public static void clearListeners() {
        mLifeCycleListener = null;
    }

    public static boolean hasListeners() {
        return (mLifeCycleListener != null);
    }
}
