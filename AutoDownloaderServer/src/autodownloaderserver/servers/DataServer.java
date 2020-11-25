/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autodownloaderserver.servers;


import autodownloaderserver.*;
import java.net.*;
import java.io.*;

/**
 *
 * @author Adminb
 */
public class DataServer implements Runnable {
    private ServerSocket serv;
    
    
    
    public DataServer(int portData){
        try{
            serv = new ServerSocket(portData);
        }
        catch(Exception ex){
            jLogger.logger.println_error("Exception in DataServer constructor: " + ex.toString());
        }
    }
            
    @Override
    public void run(){
        Socket secondary = null;
        try{
            while(true){
                if((secondary = serv.accept()) != null){
                    String name = AutoDownloaderServer.getName(secondary);
                    if(name == null)
                        continue;
                    ClientPool.addClient(new Client(name, secondary, null));
                    if(secondary.isClosed()){
                        jLogger.logger.println_error("SOCKET CLOSED!");
                        return;
                                
                    }
                    Thread thr = new Thread(new DataControl(name, ClientPool.getClient(name).dataSock));
                    thr.start();
                    jLogger.logger.println("Client accepted " + name);
                }
            }
        }
        catch(Exception ex){
            jLogger.logger.println_error("Exception in DataServer in run() : " + ex.toString());
        }

    }
    /*
    @Override
    protected void finalize() throws Throwable{
          try{

                if(serv != null)
                    serv.close();
            }
            catch(Exception ex){
                System.out.println("Exception in DataServer in run() in destroy() :" + ex.toString());
            }
          super.finalize();
    }
    */
}
