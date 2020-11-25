/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autodownloaderserver;


import java.util.Vector;
import java.io.IOException;
/**
 *
 * @author Adminb
 */
public class ClientPool {
     private static Vector<Client> clients = new Vector<Client>();
     private static int clientsCount = 0;
     public static AutoDownloaderServer serv = null;
     public static mainFrame mFrame = null;
     
    static public int getCount(){
        return clientsCount;
    }
    
    static public Vector getClients(){
        return clients;
    }
    
     public static void addClient(Client client){
        
         if(clients.size() > 0 && clients != null)
        for(Client cl : clients){
           if(cl.name.equals(client.name))
           {
             

                    return;
               
              
           }
        }
         else
         {
   //          jLogger.logger.println_warning("Client don't have a <name>");
     //        return;
         }
         
        clientsCount++;
        clients.add(client);
        jLogger.logger.println("Client connected: " + client.name);
        if(mFrame != null)
            mFrame.requestUpdateClients(); //Обновление GUI Листа
        
    }
    
    
    public static Client getClient(String name){
        for(Client cl : clients)
            if(cl.name.equals(name))
                return cl;
        
        return null;
    }
    
    public static void deleteClient(String name){
        
       ClientPool.deleteClient(ClientPool.getClient(name));
        
    }
    
    
    public static void deleteClient(Client client){
        if(client != null){
            clients.remove(client);
            clientsCount--;
        }
        
    }
    
}
