package test_beacon_strips;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by andrea on 01/07/16.
 */
public class urlrequestTest {

    public urlrequestTest(Context cx) {
        AppInfoTest(cx);
        LoginTest(cx);
    }

    public void AppInfoTest(Context cx) {
        urlrequest.RequestMaker.getAppInfo(cx, new AppInfoListener(cx));
    }

    public void LoginTest(Context cx) {
        urlrequest.RequestMaker.login(cx, "Mario", "Biondi");
    }
}

class AppInfoListener extends urlrequest.AbstractListener {
    Context cx;
    AppInfoListener(Context cx) {
        this.cx=cx;
    }
    public void onResponse(JSONObject response) {
        System.out.println("La risposta è giunta nel nostro listener!");
        Log.d("tag", response.toString());
    }
    public void onError(VolleyError error) {
        System.out.println("Al nostro listener è giunta la notifica di un errore");
    }
}
