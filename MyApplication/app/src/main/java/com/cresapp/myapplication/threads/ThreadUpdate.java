package com.cresapp.myapplication.threads;

import com.cresapp.myapplication.Downloader;

/**
 * Created by Adminb on 12.01.2016.
 */
public class ThreadUpdate implements  Runnable {
    Downloader downloader;
    public String[] files;

    public ThreadUpdate(Downloader downloader){
        this.downloader = downloader;
    }

    @Override
    public void run() {
        files = downloader.getFiles();
    }
}
