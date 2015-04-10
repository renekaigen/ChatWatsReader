package com.example.root.whatsappmst;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 18/03/15.
 */
public class SqliteConexion extends SQLiteOpenHelper {
    /*Aqui crearemos las N tablas necesarias para la interaccion:
     whatsapp - app -  internal operations- server */

    private static final String DATABASE_NAME = "RecargasWas.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_MENSAJES_RECIBIDOS = "MensajesRecibidos";

    //estas constantes las deje publicas para poderlas usar en la clase de MiSqlOperation
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_IDW = "_idWhats";
    public static final String COLUMN_CELULAR = "celular";
    public static final String COLUMN_FECHA_UNIX_RECIBIDO = "fechaRecibido";
    public static final String COLUMN_FECHA_UNIX_ENVIO_REAL_DE_CLIENTE = "fechaEnvioCliente";
    public static final String COLUMN_MENSAJE_ENVIADO = "mensajeEnviado";

    public static final String COLUMN_REQUEST_DONE = "solicitudHecha"; // 1 si ya se hizo el request al servidor , 0 default -no se ha realizado request
    public static final String COLUMN_TIPO_OPERACION="tipoOperacion";
    public static final String COLUMN_RESPUESTA_SERVIDOR = "respuestaServidor";

    /*donde tipoOPeracion:
    * 1- Recarga
    * 2-
    * 3-*/

    /* Sentencia para crear la tabla de Base de Datos
       NOTA: Por ser SQLite solo acepta 5 tipo de datos:
       REAL : es el float
       INTEGER : es int
       TEXT : es string
       BLOB
       NULL
    */


    /*Al hacer la sentencia de TABLA recordar
    separar las palabras reservadas del nombre de columna por un espacio en blanco para que el nombre sea correcto*/
    private static final String TABLE_DATABASE_CREATE = "create table "
            + TABLE_MENSAJES_RECIBIDOS+ "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_IDW + " integer , "
            + COLUMN_CELULAR + " text not null ,"
            + COLUMN_FECHA_UNIX_RECIBIDO  + " integer not null ,"
            + COLUMN_FECHA_UNIX_ENVIO_REAL_DE_CLIENTE + " integer not null  ,"
            + COLUMN_MENSAJE_ENVIADO + " text not null  ,"
            + COLUMN_REQUEST_DONE + " integer  default 0 ,"
            + COLUMN_TIPO_OPERACION + " integer not null ,"
            + COLUMN_RESPUESTA_SERVIDOR + " TEXT "
            +" );";

    public SqliteConexion(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        /*SI no existe la db la crea */
        database.execSQL(TABLE_DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*SI hay una nueva version se realizan operaciones aqui*/
    }

}
