package com.example.root.whatsappmst;

import android.content.Context;
import android.util.Log;

/**
 * Created by root on 17/03/15.
 */
public class MiTimeFunctions {
    private String TAG = this.getClass().getSimpleName();
    private boolean LogDebug=true;
    private Context context;


    public  MiTimeFunctions(Context c) {
        this.context=c;
    }
    /***
     * getUnixTimeInterval , devuelve el tiempo actual mas menos los tiempos dados
     * @param plus  - tiempo en milisegundos a sumar al tiempo actual
     * @param minus  - tiempo en milisegundos a restar al tiempo actual.
     * */
    public void getUnixTimeInterval(long plus, long minus)
    {
       // 300000
        long unixTime = System.currentTimeMillis();
        long postUnixTime=unixTime + (plus);
        long preUnixTime= unixTime - (minus);
    }

    /***
     * getUnixTimeInterval
     *          Devuelve un arreglo de longs (3) con el tiempo unix actual y  el mismo con +- 1 minuto.
     * */
    public long [] getUnixTimeInterval()
    {
        if(LogDebug) Log.d(TAG, context.getString(R.string.getUnixTimeInterval));
        long unixTime = System.currentTimeMillis();
        long [] unixTimes= {unixTime - (60000),unixTime,unixTime + (60000)};
        if(LogDebug) {
            Log.d(TAG, context.getString(R.string.PreUnixTIme) + unixTimes[0] +
                    context.getString(R.string.UnixTIme) + unixTimes[1] +
                    context.getString(R.string.PostUnixTIme) + unixTimes[2]);
        }
        return unixTimes;
    }
}
