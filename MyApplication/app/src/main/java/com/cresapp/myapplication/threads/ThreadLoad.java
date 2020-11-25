package com.cresapp.myapplication.threads;

import android.os.Environment;
import android.util.Log;

import com.cresapp.myapplication.Downloader;
import com.cresapp.myapplication.Settings;

import java.io.File;

/**
 * Created by Adminb on 12.01.2016.
 */
public class ThreadLoad implements Runnable{
    Settings settings;
    Downloader downloader;
    String fileName;
   public  boolean isLoaded = false;


    public ThreadLoad(Settings sets, Downloader down, String fileName){
        this.downloader = down;
        this.settings = sets;
        this.fileName = fileName;
    }

    @Override
    public void run(){
        String dir = "/storage/" + settings.sdcard + "/" + settings.to;
        File f = new File(dir);




        Log.d("TAGG", "THREADLOAD RUNNED! " + dir);

        if(!f.exists()){
            Log.d("TAGG", "THREADLOAD NOTFOUND! " + dir);
            if(f.mkdir()) {
                Log.d("TAGG", "THREADLOAD DIR CREATED! " + dir);
                isLoaded = downloader.Load(fileName, dir);
            }

        }
        else if(f.exists())
            isLoaded =  downloader.Load(fileName, dir);
            else
                Log.d("TAGG", "In ThreadLoad in run() in else in else 2 : can't write");

    }

}
