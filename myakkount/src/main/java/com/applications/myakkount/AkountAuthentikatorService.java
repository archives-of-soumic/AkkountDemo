package com.applications.myakkount;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AkountAuthentikatorService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        AkountAuthentikator akountAuthentikator = new AkountAuthentikator(this);
        return akountAuthentikator.getIBinder();
    }
}
