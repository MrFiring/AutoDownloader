/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autodownloaderserver;



import java.net.*;
/**
 *
 * @author Adminb
 */
public class Client {
    public String name;
    public Socket dataSock;
    public Socket fileSock;
    
    
   public Client(String name, Socket data, Socket file){
       this.name = name;
       this.dataSock = data;
       this.fileSock = file;
      
   }
   
   
   
}
