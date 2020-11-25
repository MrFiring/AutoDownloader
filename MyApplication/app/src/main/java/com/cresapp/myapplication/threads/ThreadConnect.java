package com.cresapp.myapplication.threads;

import com.cresapp.myapplication.Downloader;
import com.cresapp.myapplication.Settings;

/**
 * Created by Adminb on 12.01.2016.
 */
public class ThreadConnect implements Runnable {

    Downloader downloader;
    Settings settings;
    public volatile  boolean isConnected = false;

    public ThreadConnect(Downloader downloader, Settings settings){
        this.downloader = downloader;
        this.settings = settings;
    }


    @Override
    public void run(){
        if(settings == null || settings.dataPort == 0 || settings.filePort == 0 || settings.ip == null || settings.sdcard == null || settings.to == null)
            return;

       isConnected  =  downloader.Connect(settings.ip, settings.dataPort, settings.filePort);
    }

}
