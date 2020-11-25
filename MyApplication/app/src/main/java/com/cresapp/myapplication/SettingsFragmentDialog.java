package com.cresapp.myapplication;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * Created by Adminb on 12.01.2016.
 */





public class SettingsFragmentDialog extends DialogFragment implements View.OnClickListener {

    EditText ed_ports;
    EditText ed_to;
    EditText ed_ip;
    Context context;
    Spinner sdcard_spinner;

    public void setContext(Context context){
        this.context = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        getDialog().setTitle("Настройки");



        View v = inflater.inflate(R.layout.settings_dlg, null);
        v.findViewById(R.id.dlg_btn_ok).setOnClickListener(this);
        v.findViewById(R.id.btn_dlg_cancel).setOnClickListener(this);


        ed_ports = (EditText)v.findViewById(R.id.editText_ports);
        ed_to = (EditText)v.findViewById(R.id.editText_to);
        ed_ip = (EditText)v.findViewById(R.id.editText_ip);
        sdcard_spinner = (Spinner)v.findViewById(R.id.sdcard_spinner);

        String files[] = new File("/storage").list(); //Получаем спиоск директорий в папке strorage


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, files);
        sdcard_spinner.setAdapter(adapter);

        sdcard_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(), "Selected: " + parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(new File(context.getFilesDir(),"settings.bin").exists()){
            Settings s =  SettingsFragmentDialog.getSettings(context);

            if(s != null && s.filePort != 0 && s.dataPort != 0 &&  s.ip != null && s.to != null && s.sdcard != null){
                ed_ports.setText(String.valueOf(s.dataPort) + ";" + String.valueOf(s.filePort));
                ed_to.setText(s.to);

                for(int i = 0; i < sdcard_spinner.getCount(); i++)
                    if(sdcard_spinner.getItemAtPosition(i).toString().equals(s.sdcard))
                        sdcard_spinner.setSelection(i);

                ed_ip.setText(s.ip);
            }


        }

        return v;
    }

    public void onClick(View v){
        switch(v.getId()) {
            case R.id.dlg_btn_ok:{
                FileOutputStream out = null;
                try {
                    String ports = ed_ports.getText().toString();
                    String to = ed_to.getText().toString();
                    String ip = ed_ip.getText().toString();
                    String sdcard = sdcard_spinner.getItemAtPosition(sdcard_spinner.getSelectedItemPosition()).toString();
                    if (ports == null || to == null || ip == null || sdcard == null || to.length() == 0 || ports.length() == 0 || ip.length() == 0 || sdcard.length() == 0)
                        return;

                    out = new FileOutputStream(new File(context.getFilesDir(), "settings.bin"));
                    PrintWriter pw = new PrintWriter(out);
                    pw.println(ports);
                    pw.println(sdcard);
                    pw.println(to);
                    pw.println(ip);
                    pw.close();
                    Log.d("TAGG", "WRITED: " + ports + " | " + sdcard + " | "  + to + " | " + ip);
                } catch (Exception ex) {
                    Toast.makeText(context, "Exception in SetingsFragmentDialog in onClick() in case dlg_btn_ok : " + ex.toString(), Toast.LENGTH_LONG).show();
                } finally {
                    if (out != null)
                        try {
                            out.close();
                        } catch (Exception ex) {
                            Toast.makeText(context,
                                    "Exception in SettingsFragmentDialog in onClick in case dlg_btn_ok in finally block: " + ex.toString(),
                                    Toast.LENGTH_LONG).show();
                        }

                }

                super.onDismiss(this.getDialog());
                break;
            }

            case R.id.btn_dlg_cancel:
                onDismiss(this.getDialog());
                break;

            default:
                break;
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog){
        super.onDismiss(dialog);
    }



    public static Settings getSettings(Context context){
        FileInputStream input = null;
        BufferedReader br = null;
        try{
            input = new FileInputStream(new File(context.getFilesDir(), "settings.bin"));
            br = new BufferedReader(new InputStreamReader(input));
            String[] settings = new String[4];
            settings[0] = br.readLine();
            settings[1] = br.readLine();
            settings[2] = br.readLine();
            settings[3] = br.readLine();
            Log.d("TAGG", "Settings0 " + settings[0] + " settings1 " + settings[1] + " settings2 " + settings[2] + " settings3 " + settings[3]);
            return new Settings(Integer.valueOf(settings[0].split(";")[0]), Integer.valueOf(settings[0].split(";")[1]), settings[1] , settings[2], settings[3]);

        }catch(Exception ex){
            Toast.makeText(context, "Exception in SettingsFragmentDialog in getSettings():" + ex.toString(), Toast.LENGTH_LONG).show();

        }
        finally{
            try {
                if(br != null)
                    br.close();
                if(input != null){
                    input.close();
                }
            }catch(Exception ex){
                Toast.makeText(context, "Exception in SettingsFragmentDialog in getSettings() in finally block:" + ex.toString(), Toast.LENGTH_LONG).show();
            }
        }

        return null;
    }


}
