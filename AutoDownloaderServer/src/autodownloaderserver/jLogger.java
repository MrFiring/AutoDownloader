/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autodownloaderserver;

import java.io.*;
import java.util.Calendar;
import java.util.*;
import javax.swing.JTextArea;
/**
 *
 * @author Adminb
 */
public class jLogger {
    
    public static jLogger logger;
    
    private PrintWriter out;
    private List<String> log_queue;
    private mainFrame frm;
    
    public jLogger(File to){
        
        
        try{
        if(to != null)
            out = new PrintWriter(new FileOutputStream(to, true), true);
        }
        catch(Exception ex){
            System.out.println("Error in logger in constructor() in File != null catch : " +  ex.toString());
        }
        
        //this.frm = frm;
        
        log_queue = new ArrayList<>();
        log_queue.add("HELLO WORLD");
      
       
        jLogger.logger = this;
        
    }
    
    public void setFrm(mainFrame frm){
        this.frm = frm;
    }
    
    public void println(String text){
        Calendar c = Calendar.getInstance();
        
        
        String date = c.get(Calendar.DAY_OF_MONTH)+"." + (c.get(Calendar.MONTH)+1)+"." + c.get(Calendar.YEAR) + " " 
                    + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        String buf = date + " |@| " + text + "\n";
        
        
       
        
        print(buf);
    }
    
    public void println_error(String text){
        Date d = new Date();
        
        
        String buf = d.toString() + " |-ERROR-| " + text + "\n";
        
        print(buf);
    }
    
    public void println_dbg(String text){
        Date d = new Date();
        
        
        String buf = d.toString() + " |/DEBUG\\| " + text + "\n";
        
        print(buf);
    }
    
    public void println_warning(String text){
        Date d = new Date();
        
        
        String buf = d.toString() + " |@WARNING@| " + text + "\n";
        
        print(buf);        
    }
    
    
    private void print(String text){
      
        if(frm != null)
        frm.appendLog(text);
        if(out != null){
            out.println(text);
        }
        
        System.out.print(text);
    
        
    }
    
    
    public void clear(){
        if(out != null)
        this.out.close();
        if(log_queue != null)
            log_queue.clear();
    }
    
}
