package com.cresapp.myapplication;

/**
 * Created by Adminb on 12.01.2016.
 */
public class Settings {
    public int dataPort;
    public int filePort;
    public String sdcard;
    public String to;
    public String ip;

    public Settings(int dataPort, int filePort, String sdcard, String to, String ip){
        this.dataPort = dataPort;
        this.filePort = filePort;
        this.to = to;
        this.ip = ip;
        this.sdcard = sdcard;
    }

}
