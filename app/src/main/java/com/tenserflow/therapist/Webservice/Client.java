package com.tenserflow.therapist.Webservice;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Admin on 12/13/2018.
 */

public class Client
{


    static  Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://therapy.gangtask.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();


}
