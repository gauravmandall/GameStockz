package com.gamestockz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Connection  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (isConnected(context)){
            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Not Connected ", Toast.LENGTH_SHORT).show();
        }

    }
    public boolean isConnected(Context context){
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }


    }
}
