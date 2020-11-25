/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autodownloaderserver;

import autodownloaderserver.servers.FileServer;
import java.net.*;
import java.io.*;
/**
 *
 * @author Adminb
 */
public class DataControl implements Runnable {
    private BufferedReader reader;
    private PrintWriter writer;
    private String currentName = null; //Name of current client;
    
    public DataControl(String currentName, Socket sock){
        
        if(ClientPool.getClient(currentName).dataSock != null && ClientPool.getClient(currentName).dataSock.isClosed())
            System.out.println("Client failed");
        
        
        
        try{
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            writer = new PrintWriter(sock.getOutputStream(), true);
           this.currentName = currentName;
        }
        catch(Exception ex){
            System.out.println("Exception in DataControl in Constructor : " + ex.toString());
        }
        
    }
    
    
    @Override
    public void run(){
        String buf = null;
        
       /* if(ClientPool.serv.getAutoSync())
            this.writer.println("@SYNC@");
        else
            this.writer.println("@NOSYNC@");
        */
        while(true){
            try{
                if(reader == null)
                    System.out.println("Reader is null");
                
                if((buf = this.reader.readLine())!= null){
                    System.out.println("Received " + buf);
                    processing(buf);
                }
            }
            catch(Exception ex){
                System.out.println("Exception in DataControl in run(): "+ ex.toString());
            }
        }
    }
   
    private void processing(String command){
        if(command != null && !command.isEmpty())
            switch(command){
                case "@GETFILES@":
                    String[] files = null;
                    if(ClientPool.serv != null)
                        files = ClientPool.serv.getFilesList();
                    if(files != null){
                        String filesStr = "FILES@";
                        for(String str : files){
                            filesStr += str + '@';
                        }
                        writer.println(filesStr);
                    }

                    break;

                default:
                    if(command.startsWith("GET@"))
                    {
                        String get = command.split("@")[1];
                        File file = new File(ClientPool.serv.getDirectory() + "/" + get);
                        long size = file.getTotalSpace();
                        writer.println("DOWNLOAD@" + get + "@" + Long.toString(size));
                        try{
                        FileServer.send(ClientPool.getClient(this.currentName),  file);
                        jLogger.logger.println("Get File from " + this.currentName + " |File: " + file);
                        }
                        catch(Exception ex){
                            System.out.println("Exception in DataControl in processing() in case in default : send() " + ex.toString());
                        }
                    }
                    break;

            }
    }
    
    
/*
    @Override
    protected void finalize() throws Throwable {
        try{
            if(reader != null)
                reader.close();
            if(writer != null)
                writer.close();
        }
        catch(Exception ex){
            System.out.println("Exception in DataControl in finalize(): " + ex.toString());
        }
        super.finalize(); 
    }

    */
}
