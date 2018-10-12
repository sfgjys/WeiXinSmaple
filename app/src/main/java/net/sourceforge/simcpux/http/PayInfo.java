package net.sourceforge.simcpux.http;

import com.google.gson.annotations.SerializedName;

public class PayInfo {
    @SerializedName("appid")
    String appid;
    @SerializedName("partnerid")
    String partnerid;
    @SerializedName("package")
    String packageName;
    @SerializedName("noncestr")
    String noncestr;
    @SerializedName("timestamp")
    long timestamp;
    @SerializedName("prepayid")
    String prepayid;
    @SerializedName("sign")
    String sign;

    public String getAppid() {
        return appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public String getSign() {
        return sign;
    }
}
