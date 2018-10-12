package net.sourceforge.simcpux.http;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ZHQ on 2018/10/12.
 */

public interface HttpPay {
    @GET("app/app_pay.php")
    Call<PayInfo> getPayInfo();
}
