package com.yly.remotebinder

import android.app.Service
import android.content.Intent
import android.os.IBinder

class RemoteService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return MyRemoteBinder()
    }
}