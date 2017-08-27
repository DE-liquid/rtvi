package com.gmail.bakcina.news.net;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class APIRequestsHelper {

    public static final String BASE_URL = "https://fapi.rtvi.com/api/v1/";
    //https://fapi.rtvi.com/api/v1/newslist
    public static final int CONNECTION_TIMEOUT = 15 * 1000;
    public static final int READ_TIMEOUT = 30 * 1000;

    private APIRequestsHelper() {
    }

    public static RtviApi createRetrofitObject() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClientBuilder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RtviApi.class);
    }


    private static OkHttpClient.Builder okHttpClientBuilder(){

        return new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .sslSocketFactory(getSSLSocketFactory())
                .hostnameVerifier(((hostname, session) -> true))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
    }

    /*
     * ignoring ssl errors
     */
    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
