package com.example.root.whatsappmst;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by root on 13/03/15.
 */
public class MiNotificationListener extends NotificationListenerService {
    /*Heredar de un NotificationListenerService , esto se comporta como un servicio que esuchara unicamente
    *  notificaciones */
    /*Para usar este tipo de Servicio los requisitos necesarios son:
    * - Estar presente en Manifest
      - Tener los override de onCreate,onNotificationPosted y onNotificationRemoved
      */
      private boolean LogDebug=true;
      private String TAG = this.getClass().getSimpleName(); //TAG para debug
      private MiBroadcastReceiver mibroadcastreceiver; //variable del tipo BroadcastReceiver
      private MiDbProvider miDbprocess;
      private boolean NextProcess=false;

     @Override
    public void onCreate(){
         super.onCreate();
         /*Aqui mi codigo*/
         if(LogDebug) Log.d(TAG, getString(R.string.LogCicleLife_OnCreate));
         mibroadcastreceiver= new MiBroadcastReceiver(); // nueva instancia de BroadcastReceiver
         IntentFilter filter = new IntentFilter();
         filter.addAction("com.example.root.whatsappmst.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");/*Se agrega un filtro de accion que pertencezca  a nuestro package*/
         registerReceiver(mibroadcastreceiver,filter); //Registra un BroadcastReceiver para ser ejecutado en el Hilo de un main activity.
     }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(LogDebug) {Log.d(TAG, getString(R.string.LogBR_Receiver_S2));
            Log.d(TAG, getString(R.string.LogCicleLife_OnDestroy));}
        unregisterReceiver(mibroadcastreceiver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn)
    {
        /*Cuando llega alguna notificacion entra aqui */
        /*Aqui mi codigo*/
        if(LogDebug) Log.d(TAG, getString(R.string.Log_NR1));
        //Log.i(TAG,"ID :" + sbn.getId() + "\t" +"\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        //String pack = sbn.getPackageName();
        final  String ticker = sbn.getNotification().tickerText.toString();
        /*Aqui hare el filtrado de que package proviene*/
        miDbprocess = new MiDbProvider(getApplicationContext());
        NextProcess=miDbprocess.getPackageReceived(sbn.getPackageName());
        if(NextProcess) { if(LogDebug) Log.d(TAG, getString(R.string.nextprocessYes));
            /*continuando a buscar a ese individuo en especial*/
            miDbprocess.getNumberWhatss (sbn.getNotification().tickerText.toString());
        }
        else { if(LogDebug) Log.d(TAG, getString(R.string.nextprocessNO));}

        MiNotificationListener.this.cancelAllNotifications();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        /*Aqui mi codigo*/
        if(LogDebug) Log.d(TAG, getString(R.string.Log_NR2));
    }
    /* AQUI MiBroadcast Receiver no esta en una clase aparte puesto tenia que utilizar variables
    * de la class MiNOtificationListener (prefereible analizar como hacerle)*/

    /***
     * MiBroadcastReceiver hereda de un BroadcastReceiver
     * Enviara los broadcast de los eventos de notificaciones.
     */
    class MiBroadcastReceiver extends BroadcastReceiver {
        /*Necesario hacer Override del metodo onReceive para implementarlo*/
        @Override
        public void onReceive(Context context, Intent intent){
         /*ESte metodo es llamadao cuando el BroadcastReceiver es un Intento del tipo broadcast.*/
            /*Aqui mi codigo*/
           if(LogDebug)Log.d(TAG, getString(R.string.LogBR_Receiver_S1));
            MiNotificationListener.this.cancelAllNotifications();
        }
    }

 }