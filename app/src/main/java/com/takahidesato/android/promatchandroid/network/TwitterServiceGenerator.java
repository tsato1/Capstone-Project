package com.takahidesato.android.promatchandroid.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tsato on 4/22/16.
 */
public class TwitterServiceGenerator {
    private static String sAuthorization = "";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        return createService(serviceClass, null, baseUrl);
    }

    public static <S> S createService(Class<S> serviceClass, String authorization, String baseUrl) {
        if (authorization != null) {
            sAuthorization = authorization;
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", sAuthorization)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            }).build();
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder
                .baseUrl(baseUrl)
                .client(client)
                .build();
        return retrofit.create(serviceClass);
    }
}
