package com.eip.roucou_c.spred;

import com.eip.roucou_c.spred.DAO.Manager;
import com.eip.roucou_c.spred.Entities.TokenEntity;
import com.eip.roucou_c.spred.Interceptor.AuthInterceptor;
import com.eip.roucou_c.spred.Interceptor.ConnexionInterceptor;
import com.eip.roucou_c.spred.Interceptor.TokenAuthenticator;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cleme_000 on 29/02/2016.
 */
public class ServiceGeneratorApi {

    /**
     * DEDIEE
     */
    public static final String API_BASE_URL = "sharemyscreen.fr";
    public final static String CLIENT = "dfbDgOnQjjqRgISU";
    public final static String SECRET = "eXmWuVBcoo6llNDUcvFf6pXaoJrjS6cu";
    public final static String PORT = ":3000";


    public static  <S> S createService(Class<S> serviceClass, String subDomain, Manager manager) {
        return createService(serviceClass, subDomain, null, manager);
    }

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    public static <S> S createService(Class<S> serviceClass, String subDomain, TokenEntity tokenEntity, final Manager manager) {

        OkHttpClient.Builder client = new OkHttpClient.Builder();

        client.authenticator(new TokenAuthenticator(tokenEntity, manager));
        client.addInterceptor(new ConnexionInterceptor(tokenEntity, manager));
        client.addNetworkInterceptor(new AuthInterceptor(tokenEntity, manager));
        client.addInterceptor(logging);

        String url = subDomain == null ? API_BASE_URL+"/v1/" : subDomain + "." + API_BASE_URL+PORT+"/v1/";
        url = "http://"+url;

        Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(client.build()).build();

        return retrofit.create(serviceClass);
    }
}
