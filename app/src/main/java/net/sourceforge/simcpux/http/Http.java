package net.sourceforge.simcpux.http;

import android.support.annotation.NonNull;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Http {
    private static final Retrofit sRetrofit;

    static {
        // 创建拦截器，并将拦截器添加进OkHttpClient，以便下面初始化Retrofit对象时用
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
            }
        });
        // 设置拦截器打印  请求/响应行 + 头 + 体
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        X509TrustManager[] x509TrustManagers = new X509TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, x509TrustManagers, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        OkHttpClient client = new OkHttpClient.Builder().
                hostnameVerifier(hostnameVerifier).
                sslSocketFactory(sslContext.getSocketFactory(), x509TrustManagers[0]).
                addInterceptor(interceptor).build();

        // 初始化Retrofit对象
        sRetrofit = new Retrofit.Builder()
                .baseUrl("https://wxpay.wxutil.com/pub_v2/")
                .client(client)
                // 增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())// 注意：GsonConverterFactory需要converter-gson-2.1.0架包
                .build();
    }


    /**
     * 方法描述: 创建不同的网络请求接口
     *
     * @param clazz 接口类的class文件
     * @return 参数传递的是什么接口返回的就是对应接口的实例对象
     */
    public static <T> T createServiceInstance(Class<T> clazz) {
        return sRetrofit.create(clazz);
    }
}
