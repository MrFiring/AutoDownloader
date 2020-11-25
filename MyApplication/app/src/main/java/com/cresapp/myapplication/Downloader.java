package com.cresapp.myapplication;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.cresapp.myapplication.threads.ThreadLoad;

import java.io.*;
import java.net.*;
import java.util.Vector;


public class Downloader {
    private Socket dataSock;
    private Socket fileSock;
    private final String name = "NAMED2"; //Имя пользователя передаётся серверу для синхронизации сокетов fileSock и dataSock;
   // public Context cont;

    int filePort;
    String ip;
    public boolean isAutoSync = false;

    private BufferedReader dataReader; // Потоки ввода и вывода для dataSock;
    private PrintWriter dataWriter;

    private BufferedInputStream fileReader; //Поток ввода для fileSock;

    public Downloader(){

    }

    //Метод для загрузки файлов принимает имя файла для загрузки и директорию(куда поместить файл).
    public boolean Load(String fileName, String to){
        Log.d("TAGG", "Start loading " + fileName + " |to" + to);

        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(new File(to + fileName));
            this.fileSock = new Socket(this.ip, this.filePort);
            this.fileReader = new BufferedInputStream(this.fileSock.getInputStream());
            sendName(this.fileSock);

            this.dataWriter.println("GET@" + fileName);
            String fileData = this.dataReader.readLine();
            byte b[] = new byte[10240];
            int c;
            while((c = this.fileReader.read(b)) != -1){
                fileOut.write(b, 0 , c);
                Thread.sleep(10);
            }
            return true;
        }catch(Exception ex){
           // Toast.makeText(cont, "Exception in Downloader in Load() :" + ex.toString(), Toast.LENGTH_LONG).show();
            Log.e("TAGG","Exception in Downloader in Load() :" + ex.toString());
        }
        finally {
            try {
                if(this.fileSock != null)
                    this.fileSock.close();
                if(this.fileReader != null)
                    this.fileReader.close();
                if(fileOut != null)
                    fileOut.close();

            }
            catch(Exception ex){
             //   Toast.makeText(cont, "Exception in Download in Load() in finally block :" + ex.toString(), Toast.LENGTH_LONG).show();
				Log.e("TAGG","Exception in Downloader in Load() in finally block :" + ex.toString());
            }
        }

        return false;
    }

    //Получаем массив строк, которые содержат имена файлов доступных для загрузки.
    public String[] getFiles(){
        if(dataReader == null || dataWriter == null)
            return null;
        String files[] = null;
        try{
            this.dataWriter.println("@GETFILES@"); //Говорим серверу, что нам нужен список файлов. Он отправляет их цельной строкой.
            String str = this.dataReader.readLine();
            files = str.split("@"); //Нулевой и последний элемент нам не нужны , т.к не содержат имен.
            String buf[] = new String[files.length - 1];
			Log.d("TAGG","BUF contains : " + str);

            for(int i = 1; i < files.length; i++){
                buf[i-1] = files[i];
            }

            return buf;

        }catch(Exception ex){
           // Toast.makeText(cont, "Exception in Downloader in getFilest() : " + ex.toString(), Toast.LENGTH_LONG).show();
            Log.e("TAGG","Exception in Downloader in getFiles() : " + ex.toString());
        }

        return null;
    }

    //Подключаемся к серверу. Два аргумента первый порт для dataSock, второй для fileSock.
    //Сервер исплюзует два сокета один для передачи файлов, другой управляющий. Как в FTP протоколе.
    //Поддерживается одновременная передача только одного файла.
    public boolean Connect(String ip,int dataPort, int filePort){
        try {
            this.filePort = filePort;
            this.ip = ip;
            dataSock = new Socket(ip, dataPort);

            sendName(this.dataSock);

            this.dataReader = new BufferedReader(new InputStreamReader(dataSock.getInputStream()));
            this.dataWriter = new PrintWriter(this.dataSock.getOutputStream(), true);
          //  Toast.makeText(cont, "Готово!", Toast.LENGTH_SHORT).show();



            return true;
        }
        catch(Exception ex){
           // Toast.makeText(cont, "Exception in Downloader in Connect() : " + ex.toString(), Toast.LENGTH_LONG).show();
            Log.d("TAGG","Exception in Downloader in Connect() : " + ex.toString());
        }
        return false;
    }

    //Функция проводит синхронизацию файлов
    //Параметры 1-ый: директория в которой лежат файлы для проверки, 2-ой массив файлов доступных для скачивания
    //Проверяет есть ли в директории файл доступный для скачивания, если его нет то скачивает его.
    public boolean sync(File directory, String[] files, Settings setts, Downloader downloader){
        Log.d("TAGG", "--|Sync start|--");
        File dir = directory;
        if(!dir.exists() || files == null)
            return false;


        String[] dirFiles = dir.list();

        Vector<String> toLoad = new Vector<>();

        boolean Equals = false;
        for(int i = 0; i < files.length; i++){
            for(int j = 0; j < dirFiles.length; j++){
                if(files[i].equals(dirFiles[j])) {
                    Equals = true;
                    break;
                }
            }
            if(!Equals)
                toLoad.add(files[i]);

            Equals = false;
        }

        if(toLoad.size() > 0)
        for(String str : toLoad){
            ThreadLoad ld = new ThreadLoad(setts, downloader, str);
            Thread thr = new Thread(ld);
            thr.start();
            try {


                thr.join();
            }catch(Exception ex){
                Log.d("TAGG", "Exception in Downloader in sync() in join(): " + ex.toString());

            }
        }
        Log.d("TAGG", "--|Sync end|--");
        return true;
    }



    //Функция отравляет имя клиента сокету.
    //pw не закрываем потому, что он при закрытии закрывает привязанный к нему сокет.
    // Так мы просто обращаем его в ноль.
    private void sendName(Socket to){
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(to.getOutputStream(), true);
            pw.println(this.name);
            pw = null;
        }
        catch(Exception ex){
            Log.d("TAGG", "Exception in Downloader in sendName(): " + ex.toString());
           // Toast.makeText(cont , "Exception in Downloader in sendName(): " + ex.toString(), Toast.LENGTH_LONG ).show();
        }
        finally{
            pw = null;
        }
    }
}
