package com.cresapp.myapplication;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ListView;

import com.cresapp.myapplication.threads.ThreadConnect;
import com.cresapp.myapplication.threads.ThreadLoad;
import com.cresapp.myapplication.threads.ThreadUpdate;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    Button btn_load;
    Button btn_connect;
    Button btn_update;

    ListView listView;
    Downloader downloader;
    Toolbar tool;
    Settings settings;
    SettingsFragmentDialog settings_dlg;
    CustomHandler handler;

    String[] files = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        handler = new CustomHandler();

        tool = (Toolbar)this.findViewById(R.id.toolbar);
        setSupportActionBar(tool);

        listView = (ListView)this.findViewById(R.id.listView);

         settings = null;




        downloader = new Downloader();
       // downloader.cont = this;




        btn_load = (Button)this.findViewById(R.id.btn_load);
      //  btn_load.setEnabled(true);
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_load.setEnabled(false);
                handler.setContext(v.getContext());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (settings != null)
                            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                boolean isLoaded = false;

                                String dir = "/storage/" + settings.sdcard + "/" + settings.to;
                                String fileName = (String) listView.getAdapter().getItem(listView.getCheckedItemPosition());
                                File f = new File(dir);




                                Log.d("TAGG", "THREADLOAD RUNNED! " + dir);

                                if(!f.exists()){
                                    Log.d("TAGG", "THREADLOAD NOTFOUND! " + dir);
                                    if(f.mkdir()) {
                                        Log.d("TAGG", "THREADLOAD DIR CREATED! " + dir);
                                        isLoaded = downloader.Load(fileName, dir);
                                    }

                                }
                                else if(f.exists())
                                    isLoaded =  downloader.Load(fileName, dir);
                                else
                                    Log.d("TAGG", "In ThreadLoad in run() in else in else 2 : can't write");



                                if (isLoaded)
                                    Toast.makeText(handler.getCurContext(), "Файл скачан: " + (String) listView.getAdapter().getItem(listView.getCheckedItemPosition()), Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(handler.getCurContext(), "Внешнее хранилище не доступно для записи" , Toast.LENGTH_LONG).show();
                            }



                        btn_load.setEnabled(true);
                    }
                });

    /*
                if (settings != null)
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                        ThreadLoad con = new ThreadLoad(settings, downloader, (String) listView.getAdapter().getItem(listView.getCheckedItemPosition()));
                        Thread thr =  new Thread(con);
                        thr.start();
                        try {


                            thr.join();
                        }catch(Exception ex){
                            Log.d("TAGG", "Exception in MainActivity in Load in join(): " + ex.toString());

                        }

                        if (con.isLoaded)
                            Toast.makeText(v.getContext(), "Файл скачан: " + (String) listView.getAdapter().getItem(listView.getCheckedItemPosition()), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(v.getContext(), "Внешнее хранилище не доступно для записи" , Toast.LENGTH_LONG).show();
                    }



                btn_load.setEnabled(true);
                */
            }

        });

        btn_connect = (Button)this.findViewById(R.id.btn_connect);
        btn_connect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                handler.setContext(v.getContext());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(new File(handler.getCurContext().getFilesDir(), "settings.bin").exists()){
                            settings = SettingsFragmentDialog.getSettings(handler.getCurContext());
                            Toast.makeText(handler.getCurContext(), "Settings loaded!", Toast.LENGTH_SHORT).show();
                        }
                        if(settings == null || settings.dataPort == 0 || settings.filePort == 0 || settings.ip == null || settings.sdcard == null || settings.to == null)
                            return;
                        if(downloader.Connect(settings.ip, settings.dataPort, settings.filePort)){
                            Toast.makeText(handler.getCurContext(), "Connected!", Toast.LENGTH_SHORT).show();
                            btn_update.setEnabled(true);
                            btn_load.setEnabled(true);
                            btn_connect.setEnabled(false);
                        }

                    }
                });
                /*
                if(new File(v.getContext().getFilesDir(), "settings.bin").exists()){
                    settings = SettingsFragmentDialog.getSettings(v.getContext());
                    Toast.makeText(v.getContext(), "Settings loaded!", Toast.LENGTH_SHORT).show();
                }


                ThreadConnect con = new ThreadConnect(downloader, settings);
                Thread thr =  new Thread(con);
                thr.start();
                try {


                    thr.join();
                }catch(Exception ex){
                    Log.d("TAGG", "Exception in MainActivity in join(): " + ex.toString());

                }
                Log.d("TAGG", "POST join() ");
                    if (con.isConnected) {
                        Toast.makeText(v.getContext(), "Connected!", Toast.LENGTH_SHORT).show();
                        btn_update.setEnabled(true);
                        btn_load.setEnabled(true);
                        btn_connect.setEnabled(false);

                    }
            */
            }
        });




        btn_update = (Button)this.findViewById(R.id.btn_update);
      //  btn_update.setEnabled(true);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.setContext(v.getContext());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] filesL = downloader.getFiles();
                        if(filesL == null || filesL.length == 0)
                            return;
                        files = filesL;
                        ArrayAdapter<String> adapt = new ArrayAdapter<String>(handler.getCurContext(), android.R.layout.simple_list_item_1, files);
                        listView.setAdapter(adapt);
                        Toast.makeText(handler.getCurContext(),"Обновление завершено", Toast.LENGTH_SHORT).show();
                    }
                });


                /*
                ThreadUpdate con = new ThreadUpdate(downloader);
                Thread thr =  new Thread(con);
                thr.start();
                try {


                    thr.join();
                }catch(Exception ex){
                    Log.d("TAGG", "Exception in MainActivity in Update in join(): " + ex.toString());

                }

                if (con.files == null || con.files.length == 0)
                    return;

                files = con.files;
                ArrayAdapter<String> adapt = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, con.files);
                listView.setAdapter(adapt);
                // listView.invalidate();
                */
            }

        });


        settings_dlg = new SettingsFragmentDialog();
        settings_dlg.setContext(this);


        if(new File(getFilesDir(), "settings.bin").exists()){
            settings = SettingsFragmentDialog.getSettings(this);
            Toast.makeText(this, "Settings loaded!", Toast.LENGTH_SHORT).show();
        }
/*
        btn_connect.performClick();
        btn_update.performClick();
        if(downloader.isAutoSync) {
            Toast.makeText(this,"Синхронизация...Подождите..",Toast.LENGTH_LONG).show();
            downloader.sync(new File("/storage/" + settings.sdcard + "/" + settings.to), this.files, settings, downloader);
            Toast.makeText(this,"Синхронизация завершена",Toast.LENGTH_SHORT).show();
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){

            case R.id.action_settings:
                settings_dlg.show(getFragmentManager(), "SETTINGS_DLG");
                break;
            case R.id.action_sync:
                Toast.makeText(this,"Синхронизация...Подождите..",Toast.LENGTH_LONG).show();
                downloader.sync(new File("/storage/" + settings.sdcard + "/" + settings.to), this.files, settings, downloader);
                Toast.makeText(this,"Синхронизация завершена",Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){

        super.onResume();
    }


}
