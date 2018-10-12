package net.sourceforge.simcpux;


import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.http.Http;
import net.sourceforge.simcpux.http.HttpPay;
import net.sourceforge.simcpux.http.PayInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends Activity {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay);

        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");

        Button appayBtn = (Button) findViewById(R.id.appay_btn);
        appayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button payBtn = (Button) findViewById(R.id.appay_btn);
                payBtn.setEnabled(false);
                Toast.makeText(PayActivity.this, "从服务端获取支付信息...", Toast.LENGTH_SHORT).show();
                Call<PayInfo> payInfo = Http.createServiceInstance(HttpPay.class).getPayInfo();
                payInfo.enqueue(new Callback<PayInfo>() {
                    @Override
                    public void onResponse(Call<PayInfo> call, Response<PayInfo> response) {
                        PayInfo body = response.body();
                        if (body != null) {
                            PayReq req = new PayReq();
                            req.appId = body.getAppid();
                            req.partnerId = body.getPartnerid();
                            req.prepayId = body.getPrepayid();
                            req.nonceStr = body.getNoncestr();
                            req.timeStamp = body.getTimestamp() + "";
                            req.packageValue = body.getPackageName();
                            req.sign = body.getSign();
                            req.extData = "app data"; // optional
                            api.sendReq(req);
                        }
                    }

                    @Override
                    public void onFailure(Call<PayInfo> call, Throwable t) {
                        System.out.println("获取支付信息失败失败失败失败失败失败失败失败失败失败失败失败失败失败失败了");
                    }
                });
                payBtn.setEnabled(true);
            }
        });
        Button checkPayBtn = (Button) findViewById(R.id.check_pay_btn);
        checkPayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                Toast.makeText(PayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
