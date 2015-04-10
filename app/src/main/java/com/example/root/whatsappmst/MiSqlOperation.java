package com.example.root.whatsappmst;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/***
 * Clase para manipular las operaciones simples con db local como son insert,delete,update,etc
 */
public class MiSqlOperation {

    // Database fields
    private SQLiteDatabase database;
    private SqliteConexion sqliteconexion;
    private String[] allColumns = { SqliteConexion.COLUMN_ID,
            SqliteConexion.COLUMN_CELULAR };

    private static final String KEY_IDW = "_idWhats";
    private static final String KEY_CELULAR = "celular";
    private static final String KEY_FECHA_UNIX_RECIBIDO = "fechaRecibido";
    private static final String KEY_FECHA_UNIX_ENVIO_REAL_DE_CLIENTE = "fechaEnvioCliente";
    private static final String KEY_MENSAJE_ENVIADO = "mensajeEnviado";

    private String[] columnasDiccionario = { SqliteConexion.COLUMN_IDW,
            SqliteConexion.COLUMN_CELULAR , SqliteConexion.COLUMN_FECHA_UNIX_RECIBIDO,
            SqliteConexion.COLUMN_FECHA_UNIX_ENVIO_REAL_DE_CLIENTE ,SqliteConexion.
            COLUMN_MENSAJE_ENVIADO};

    public MiSqlOperation(Context context) {
        sqliteconexion = new SqliteConexion(context);  //conexion y/o creacion de DB
    }

    public void open() throws SQLException {
        database = sqliteconexion.getWritableDatabase(); // disponible para escribir.
    }

    public void close() {
        sqliteconexion.close(); //cerrar db
    }

    /***
     *  ComprobarInsertar()  Comprueba con la DB propia vs el diccionario dado para ver cuales son las rows nuevas para agregar
     *  @param diccionario  , recibe un  rrayList<HashMap<String, String>>  , es un diccionario con las llaves :
     *                            _idWhats
     *                            celular
                                  fechaRecibido
                                  fechaEnvioCliente
                                  mensajeEnviado
     */
    public void ComprobarInsertar( ArrayList<HashMap<String, String>> diccionario,long [] fechas ){
        Cursor cursor = database.query(SqliteConexion.TABLE_MENSAJES_RECIBIDOS,
                columnasDiccionario,
                "("+SqliteConexion.COLUMN_FECHA_UNIX_RECIBIDO+">="+ fechas[0] +" and " +SqliteConexion.COLUMN_FECHA_UNIX_RECIBIDO+"<=" +fechas[2]+") ",
                null, null, null, "_ID DESC", "1"); ///seleccionar el primer registro  desc entre las fechas dadas
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            Long idw=Long.parseLong(cursor.getString(0));//obteniendo el idw , es el id PK de whatss
            /*Recorrer hasta encontrar incidencias, las no incidencias se agregan a DB lo demas se ignora*/
            for (int i=0;i<diccionario.size();i++)
            {
                HashMap<String, String> valorX = diccionario.get(i);
                if(Long.parseLong(valorX.get(KEY_IDW).toString())!= idw)
                {
                    HashMap<String, String> valor = diccionario.get(i);
                    ContentValues row = new ContentValues();
                    row.put(KEY_IDW, valor.get(KEY_IDW));
                    row.put(KEY_CELULAR, valor.get(KEY_CELULAR));
                    row.put(KEY_FECHA_UNIX_RECIBIDO, valor.get(KEY_FECHA_UNIX_RECIBIDO));
                    row.put(KEY_FECHA_UNIX_ENVIO_REAL_DE_CLIENTE, valor.get(KEY_FECHA_UNIX_ENVIO_REAL_DE_CLIENTE));
                    row.put(KEY_MENSAJE_ENVIADO, valor.get(KEY_MENSAJE_ENVIADO));
                    row.put(SqliteConexion.COLUMN_REQUEST_DONE,0);
                    row.put(SqliteConexion.COLUMN_TIPO_OPERACION,1);
                    row.put(SqliteConexion.COLUMN_RESPUESTA_SERVIDOR,"NULL");
                    database.insert(SqliteConexion.TABLE_MENSAJES_RECIBIDOS, null, row);
                    continue;
                }
                else{break;}
            }
        }
        else
        {
            ///INSERTAR diccionario
            for(int i=0;i<diccionario.size() ;i++)
            {
                //ir a anexando cada valor
                HashMap<String, String> valor = diccionario.get(i);
                ContentValues row = new ContentValues();
                row.put(KEY_IDW, valor.get(KEY_IDW));
                row.put(KEY_CELULAR, valor.get(KEY_CELULAR));
                row.put(KEY_FECHA_UNIX_RECIBIDO, valor.get(KEY_FECHA_UNIX_RECIBIDO));
                row.put(KEY_FECHA_UNIX_ENVIO_REAL_DE_CLIENTE, valor.get(KEY_FECHA_UNIX_ENVIO_REAL_DE_CLIENTE));
                row.put(KEY_MENSAJE_ENVIADO, valor.get(KEY_MENSAJE_ENVIADO));
                row.put(SqliteConexion.COLUMN_REQUEST_DONE,0);
                row.put(SqliteConexion.COLUMN_TIPO_OPERACION,1);
                row.put(SqliteConexion.COLUMN_RESPUESTA_SERVIDOR,"NULL");
                database.insert(SqliteConexion.TABLE_MENSAJES_RECIBIDOS, null, row);
            }
        }
        cursor.close();
    }

}
