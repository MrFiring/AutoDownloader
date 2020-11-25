package autodownloaderserver;

import autodownloaderserver.servers.*;
import java.net.*;
import java.util.*;
import java.io.*;



public class AutoDownloaderServer implements Runnable{

   private String directory = "./";
   private FileServer fileServer;
   private DataServer dataServer;
   private boolean isAutoSync = false;

   
   public AutoDownloaderServer(int dataPort, int filePort){
       if(System.getProperty("os.name").contains("Linux"))
           directory = "/media/Disk_D/Development/Projects/JAVA/AutoDownloaderServer/";
       
       dataServer = new DataServer(dataPort);
       fileServer = new FileServer(filePort);
   }
    
    
   
    
    @Override
    public void run(){
        Thread thr1 = new Thread(dataServer);
        Thread thr2 = new Thread(fileServer);
        thr1.start();
        thr2.start();
    }
    
     public String[] getFilesList(){
        File dir = new File(directory);
        String[] files = dir.list();
        
        ArrayList<String> list = new ArrayList<>();
        
        for(int i = 0; i < files.length; i++)
        {
            if(files[i].endsWith(".mp3"))
                list.add(files[i]);
        }
        
        
        return  list.toArray(new String[list.size()]).length > 0 ?  list.toArray(new String[list.size()]) : null;
    }
    
    public static String getName(Socket from){
       String name = null;
       BufferedReader br = null;
       try{
           br  = new BufferedReader(new InputStreamReader(from.getInputStream()));
           
           name = br.readLine();
           
          return name;
       }
       catch(Exception ex){
           System.out.println("Exception getName() in Server: " + ex.toString());
       }
       finally{
           try{
           if(br != null)
               br = null;
           }
           catch(Exception ex){
               System.out.println("Exception finnally block in getName() in Server: " + ex.toString());
           }
       }
       return null;
   }
    
    public void setDirectory(String directory){
        this.directory = directory;
    }

    public String getDirectory(){
        return this.directory;
    }
    
    public boolean getAutoSync(){
        return this.isAutoSync;
    }
    
    public void setAutoSync(boolean is){
        this.isAutoSync = is;
    }
    
    @Override
    protected void finalize() throws Throwable{
        Vector<Client> pool = ClientPool.getClients();
        
        if(pool != null && pool.size() > 0)
            for(Client cl : pool){
                if(cl != null){
                    if(cl.dataSock != null)
                        cl.dataSock.close();
                    if(cl.fileSock != null)
                        cl.fileSock.close();

                }
                pool.remove(cl);
            }
        
        super.finalize();
    }
    
}
    
  