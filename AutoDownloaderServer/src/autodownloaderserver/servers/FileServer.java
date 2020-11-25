/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autodownloaderserver.servers;

import autodownloaderserver.AutoDownloaderServer;
import autodownloaderserver.Client;
import autodownloaderserver.ClientPool;
import autodownloaderserver.jLogger;

import java.net.*;
import java.io.*;
import java.lang.Thread;
/**
 *
 * @author Adminb
 */
public class FileServer  implements Runnable{
   public ServerSocket serv;
   
   public FileServer(int filePort){
       try{
           serv = new ServerSocket(filePort);
       }
       catch(Exception ex){
           System.out.println("File Server exception in constructor: " + ex.toString());
       }
   }
    
   @Override
   public void run() {
       try
       {
       Socket secondary = null;
       while(true){
           if((secondary = serv.accept()) != null){
               ClientPool.getClient(AutoDownloaderServer.getName(secondary)).fileSock = secondary;
               secondary = null;
               
           }
           
       }
       }
       catch(Exception ex){
           jLogger.logger.println_error("File Server run() exception: " + ex.toString());
       }
   }
    
   public static void send(Client target, File data) throws Exception{
       BufferedInputStream stream = null;
       try{
       stream = new BufferedInputStream(new FileInputStream(data));
       byte n[] = new byte[10240];
       int c = 0;
       while( (c = stream.read(n)) != -1){
           
          // stream.read(n);
           target.fileSock.getOutputStream().write(n, 0 , c);
           Thread.sleep(10);
        }
       }
       catch(Exception ex){
           
            jLogger.logger.println_error("File Server exception in send: " + data.getName() + "|   " + ex.toString());
       }
       finally{
           if(stream != null)
               stream.close();
           if(target.fileSock != null)
               target.fileSock.close();
       }
   }
  
    
}
