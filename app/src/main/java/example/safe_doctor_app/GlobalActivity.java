package example.safe_doctor_app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;

import java.util.List;

public class GlobalActivity extends Application {
    public String url;
    public String IMEI;
    public String phoneNumber;
    public Double latitude;
    public Double longitude;

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLongitude() {

        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

//    public String getIMEI() {
//        TelephonyManager telephonyManager=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//        IMEI=telephonyManager.getDeviceId();
//        return IMEI;
//    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getUrl() {
        String phoneUrl="http://192.168.43.58/internportal/";
        String serverUrl="http://192.168.43.58/internportal/";
        //use phone generated IP address
        url=phoneUrl;
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public String getPhoneNumber() {
//        TelephonyManager telephonyManager=(TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
//        phoneNumber=telephonyManager.getLine1Number();
//        return phoneNumber;
//    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
