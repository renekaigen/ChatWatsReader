package com.example.root.whatsappmst;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 17/03/15.
 */

/***
 *  MiDbProvider
 *  Clase relacionada a la DB Whats para MST
 *  @param - Se necesita un Contexto
 */
public class MiDbProvider {
    private String TAG = this.getClass().getSimpleName();
    private boolean LogDebug=true;
    private Context context;
    private MiTimeFunctions mistiempos;
    private MiSqlOperation operacionesSqlite;

    private static final String KEY_IDW = "_idWhats";
    private static final String KEY_CELULAR = "celular";
    private static final String KEY_FECHA_UNIX_RECIBIDO = "fechaRecibido";
    private static final String KEY_FECHA_UNIX_ENVIO_REAL_DE_CLIENTE = "fechaEnvioCliente";
    private static final String KEY_MENSAJE_ENVIADO = "mensajeEnviado";

    public MiDbProvider(Context c) {
          this.context =c;
        if(LogDebug)  Log.d(TAG,context.getString(R.string.constructor));
    }

    public boolean getPackageReceived (String pack) {
        if(LogDebug)  Log.d(TAG,context.getString(R.string.getPackageReceived));
        /*Comprobar que sea el package correcto para copiar la DB*/
        if(pack.equals("com.whatsapp")) { copyDbToSdcard(); return true;} //si es whats copia DB
        else return false;
    }

    public boolean getNumberWhatss (String tagNumber){
        if(LogDebug)  Log.d(TAG,context.getString(R.string.getNumberWhatss) + tagNumber);
        // de esta manera vien el tagNUmebr Mensaje de ‪+52 1 833 154 9242‬
        /*Pasos
        * 1- quitar espacios en blanco
        * 2- Obtener el indice de inicio de  +
          3- SOlo tener los numeros
        * */
           tagNumber=tagNumber.trim().replace(" ", "");
           tagNumber=tagNumber.substring(tagNumber.indexOf("+") + 1);
        /*Obtener intervalos UNIX*/
           mistiempos= new  MiTimeFunctions(this.context);
           long unixTimes[]= mistiempos.getUnixTimeInterval();
           if(LogDebug) {
               Log.d(TAG, context.getString(R.string.getNumberWhatss) + tagNumber);
               Log.d(TAG,context.getString(R.string.PreUnixTIme) + unixTimes[0]+
                               context.getString(R.string.UnixTIme) + unixTimes[1]+
                               context.getString(R.string.PostUnixTIme) + unixTimes[2]);
           }
        /*Realizar query en la copia de la DB que se ubica en SD*/
        getDatainTime(tagNumber,unixTimes);

        return false;
    }

    /***
     *  copyDBToSdcard
     *  Genera una copia reciente de whats a la sd
     */
    public void copyDbToSdcard(){
        try{
            String comando = "cp -r /data/data/com.whatsapp/databases/msgstore.db /storage/sdcard0/Copias.db";
            Process suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());
            os.writeBytes(comando + "\n");
            os.flush();
            os.writeBytes("exit\n");
            os.flush();
            try{
                int suProcessRetval = suProcess.waitFor();
                if(255 != suProcessRetval)
                {
                    //
                }
                else
                {
                    //
                }
            }
            catch (Exception ex)
            {
                Log.e("ERROR-->", ex.toString());
            }
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    /***
     *
     */
    public void getDatainTime(String celular, long [] fechas ) {
        File dbFile = new File("/storage/sdcard0/Copias.db");
        if (dbFile.exists()) {
            ArrayList<HashMap<String, String>> diccionarioWhatss = new ArrayList<HashMap<String, String>>();

            SQLiteDatabase db = SQLiteDatabase.openDatabase("/storage/sdcard0/Copias.db", null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
            Cursor fila = db.rawQuery(
                    "select _ID,KEY_REMOTE_JID,DATA,RECEIVED_TIMESTAMP,TIMESTAMP from messages where (RECEIVED_TIMESTAMP>="+ fechas[0] +" and RECEIVED_TIMESTAMP<=" +
                            fechas[2]+") and ORIGIN=1 and STATUS=0 order by _ID DESC", null);
            String celularWhats;
            while(fila.moveToNext()) {

                HashMap<String, String> mapa = new HashMap<String, String>();
                mapa.put(KEY_IDW, fila.getString(0));

                //quitarle el @s.whatsapp.net y el 521
                celularWhats=fila.getString(1).trim().replace(" ", "");
                celularWhats=fila.getString(1).substring(3, celularWhats.indexOf("@"));

                mapa.put(KEY_CELULAR, celularWhats);
                mapa.put(KEY_FECHA_UNIX_RECIBIDO, fila.getString(3));
                mapa.put(KEY_FECHA_UNIX_ENVIO_REAL_DE_CLIENTE, fila.getString(4));
                mapa.put(KEY_MENSAJE_ENVIADO, fila.getString(2));
                diccionarioWhatss.add(mapa);  //creamos el diccionario , con la implementacion lista de Maps
                if (LogDebug) {
                    Log.d(TAG, "ID : " + fila.getString(0) +
                                    "\n CELULAR :" + celularWhats+
                                    "\n Mensaje :" + fila.getString(2) +
                                    "\n Fecha Recibido :" + fila.getString(3) +
                                    "\n Fecha Envio persona :" + fila.getString(4)
                            );
                }
            }
            db.close();
            //Una vez teniendo el diccionario hacer comprobacionINsercion a DB Propia
            operacionesSqlite= new MiSqlOperation(context);
            operacionesSqlite.open();
            operacionesSqlite.ComprobarInsertar(diccionarioWhatss,fechas);
            operacionesSqlite.close();
        }
    }

}
