package com.example.cryptomoney;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingelton {
    private static MySingelton mInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private MySingelton(Context context)
    {
        this.context = context;
        requestQueue = getRequestQueue();

    }

    public RequestQueue getRequestQueue(){
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        }
        return  requestQueue;

    }
    public static synchronized MySingelton getInstance(Context contetxt){
        if(mInstance == null){

            mInstance = new MySingelton(contetxt);

        }
        return mInstance;
    }
    public<T> void addToRequsetQueue(Request request){
        requestQueue.add(request);
    }
}