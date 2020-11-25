/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ads_util;

import java.util.Vector;
import autodownloaderserver.Client;
import autodownloaderserver.ClientPool;
import autodownloaderserver.jLogger;
/**
 *
 * @author Admin
 */
public class Util {
   
    
    static public Vector fromClientToStrings(){ //Возвращает вектор со строками в которых хранятся имена клиентов из пула клиентов. ///проще говоря парсинг имен клиентов
        Vector<Client> clients = ClientPool.getClients();
        Vector<String> names = new Vector<>();
        
        if(clients.isEmpty()){
            jLogger.logger.println_error("Clients list is empty! [null has been returned]");
            return new Vector<String>();
        }
        
        for(Client cl : clients){
            if(cl.name.length() > 0)
                names.add(cl.name);
        }
        
        if(names.isEmpty()){
            jLogger.logger.println_error("Names list is empty! [null has been returned]");
            return new Vector<String>();
        }
        
        return names;
    }
    
}
