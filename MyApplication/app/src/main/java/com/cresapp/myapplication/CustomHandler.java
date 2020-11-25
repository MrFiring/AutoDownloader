package com.cresapp.myapplication;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by Admin on 05.11.2016.
 */

public class CustomHandler extends Handler {
    private Context curContext;

    public CustomHandler() {
        super();
    }

    public CustomHandler(Callback callback) {
        super(callback);
    }

    public CustomHandler(Looper looper) {
        super(looper);
    }

    public CustomHandler(Looper looper, Callback callback) {
        super(looper, callback);
    }

    public void setContext(Context cont){this.curContext = cont;}

    public Context getCurContext() {
        return curContext;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }

    @Override
    public void dispatchMessage(Message msg) {
        super.dispatchMessage(msg);
    }

    @Override
    public String getMessageName(Message message) {
        return super.getMessageName(message);
    }

    @Override
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        return super.sendMessageAtTime(msg, uptimeMillis);
    }
}
