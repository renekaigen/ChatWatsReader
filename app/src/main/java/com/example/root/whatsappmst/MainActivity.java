package com.example.root.whatsappmst;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {
    private TextView txtView;
    private Button btnDB;
    private boolean LogDebug=true;
    private String TAG = this.getClass().getSimpleName(); //TAG para debug
    private int siguiente=0;
    private EditText txtMensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Activando el NotificationReceiver*/
        if(LogDebug) Log.d(TAG, getString(R.string.LogCicleLife_OnCreate));
        txtView = (TextView) findViewById(R.id.textView);
        long unixTime = System.currentTimeMillis();
        long postUnixTime=unixTime + 300000;
        long preUnixTime= unixTime - 300000;
        txtView.setText("Hace 5 minutos "+preUnixTime + "<br> Tiempo actual : " + unixTime+
        "<br> 5 minutos en el futuro : "+ postUnixTime);

        btnDB=(Button) findViewById(R.id.btnDB);
        txtMensaje= (EditText) findViewById(R.id.txtMensaje);

        btnDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDatainTime(txtMensaje.getText().toString());
            }
        });
    }

    public void getDatainTime(String mensaje) {

        Log.d(TAG, "metodo");


        File dbFile = new File("/data/data/com.whatsapp/databases/msgstore.db");
        if (dbFile.exists()) {

            Log.d(TAG, "nnani");

            ArrayList<HashMap<String, String>> diccionarioWhatss = new ArrayList<HashMap<String, String>>();

            SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.whatsapp/databases/msgstore.db", null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
            Cursor fila = db.rawQuery(
                    "select * from messages order by _ID DESC LIMIT 1", null);
            String[] columnNames = fila.getColumnNames();
            String unico="(";
            int m=columnNames.length;
            for(int i=0;i<columnNames.length;i++){
                if(i<m-1) {
                    unico += columnNames[i] + ",";
                }
                else{
                    unico += columnNames[i] ;
                }
            }
            unico+=")";
            Log.d(TAG, "clumnas :" + unico);
            while(fila.moveToNext()) {


               // celularWhats=fila.getString(1).trim().replace(" ", "");
               // celularWhats=fila.getString(1).substring(3, celularWhats.indexOf("@"));
                if (LogDebug) {
                    Log.d(TAG, "ID : " + fila.getString(0) +
                                    "\n CELULAR :" +fila.getString(1)+
                                    "\n key from me:" + fila.getString(2) +
                                    "\n key id :" + fila.getString(3) +
                                    "\n status :" + fila.getString(4)+
                                    "\n mesnaje :" + fila.getString(6)+
                                    "\n orgine :" + fila.getString(25)

                    );
                int n=fila.getColumnCount();
                    String valores="VALUES (";
                    for(int i=0;i<n;i++){
                        if(i<n-1) {
                            valores += fila.getString(i) + ",";
                        }else
                        {
                            valores += fila.getString(i);
                        }
                    }
                    valores+=")";
                    Log.d(TAG, "parte final : " + valores);
                /*    Cursor filas = db.rawQuery(
                            "insert into  messages "+unico+ " ("+ +") ", null);
*/

            }

            }
            siguiente++;
            String key_id= String.valueOf(System.currentTimeMillis());
            key_id=key_id.substring(0,10);
            key_id+="-"+siguiente;

            fila.close();
            db.execSQL("INSERT INTO messages " +
                    "(key_remote_jid,key_from_me,key_id,status,needs_push,data,timestamp,media_url,media_mime_type,media_wa_type,media_size,media_name,media_caption,media_hash,media_duration,origin,latitude,longitude,thumb_image,remote_resource,received_timestamp,send_timestamp,receipt_server_timestamp,receipt_device_timestamp,read_device_timestamp,played_device_timestamp,raw_data,recipient_count,participant_hash) " +
                    "VALUES ('5218332891977@s.whatsapp.net',1,"+key_id+",14,0,'"+mensaje+"',"+System.currentTimeMillis()+",null,null,0,0,null,null,null,0,0,0,0,null,null,"+System.currentTimeMillis()+",-1,-1,-1,null,null,null,null,null)");
            db.close();

            Log.d(TAG, "INSERT INTO messages " +
                    "(key_remote_jid,key_from_me,key_id,status,needs_push,data,timestamp,media_url,media_mime_type,media_wa_type,media_size,media_name,media_caption,media_hash,media_duration,origin,latitude,longitude,thumb_image,remote_resource,received_timestamp,send_timestamp,receipt_server_timestamp,receipt_device_timestamp,read_device_timestamp,played_device_timestamp,raw_data,recipient_count,participant_hash) " +
                    "VALUES ('5218332891977@s.whatsapp.net',1," + key_id + ",14,0,'"+mensaje+"'," + System.currentTimeMillis() + ",null,null,0,0,null,null,null,0,0,0,0,null,null," + System.currentTimeMillis() + ",-1,-1,-1,null,null,null,null,null)");
            WifiManager wifiManager = (WifiManager) this.getSystemService(getApplicationContext().WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
            wifiManager.setWifiEnabled(false);
            wifiManager.setWifiEnabled(true);
            //habilito y deshabilito las conexiones para que whatsapp envie los mensajes
            /*Requisitos : haber entrado con la app Es FILE Explorer hasta la carpeta contenedora de base de datos
             darle permisos a todos los usuarios, hacer lo mismo con la db msgstore.db , y pasar del permiso
             00_a57 a 00_a55 luego otra vez al 00_a57 , hacemos una copia de esta db pero renombrada con
              "msgstore.db-", ahora tenderemos 2 dbs,
              entonces la base de datos no respodnera con el sqlite manager app
             quitamos y ponemos el wifi y enviamos un whatss a quien quieras con esto ya la DB de whatss es nuestra para manipularla al gusto :)
            CONsiderar que existen algunas llaves unicas por eso esta el código de abajo
            */
        }
    }



    public void onClickWhatsApp2(View view) {
       // Uri mUri = Uri.parse("smsto:+8087275446");
        Uri mUri = Uri.parse("smsto:+8332891977");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
        mIntent.setPackage("com.whatsapp");
        mIntent.putExtra("sms_body", "The text goes here");
        mIntent.putExtra("chat",true);
        startActivity(mIntent);
    }


    public void onClickWhatsApp(View view) {

        PackageManager pm=getPackageManager();
        try {

            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            String text = "YOUR TEXT HERE";

            PackageInfo info=pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            //Check if package exists or not. If not then code
            //in catch block will be called
            waIntent.setPackage("com.whatsapp");

            waIntent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(Intent.createChooser(waIntent, "Share with"));

        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(LogDebug) Log.d(TAG, getString(R.string.LogCicleLife_OnDestroy));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
