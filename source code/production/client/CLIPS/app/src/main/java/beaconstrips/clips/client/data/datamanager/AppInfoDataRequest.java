package beaconstrips.clips.client.data.datamanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import beaconstrips.clips.client.data.AppInfo;
import beaconstrips.clips.client.urlrequest.AbstractUrlRequestListener;
import beaconstrips.clips.client.urlrequest.RequestMaker;
import beaconstrips.clips.client.urlrequest.ServerError;

/**
 * @file AppInfoDataRequest.java
 * @date 26/07/16
 * @version 1.0.0
 * @author Andrea Grendene
 *
 * classe derivata da DataManager dove vengono implementati tutti i metodi necessari per ottenere, dal server o, in caso avvenga qualche errore, in locale, le informazioni dell'applicazione
 */
public class AppInfoDataRequest extends DataManager<AppInfo> {
   AppInfoDataRequest(Context cx, AbstractDataManagerListener<AppInfo> listener) {
      super(cx, CachePolicy.AlwaysReplaceLocal, listener);
      execute();
   }

   protected AppInfo parseFromLocal() {
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cx);
      AppInfo appInfo = new AppInfo(preferences.getString("description",""), preferences.getString("supportemail",""), preferences.getString("websiteURL",""), preferences.getString("discoveryUUID",""));
      return appInfo;
   }

   protected void getRemoteData(AbstractUrlRequestListener listener) {
      RequestMaker.getAppInfo(cx, listener);
   }

   protected AppInfo parseFromUrlRequest(JSONObject response){
      try {
         AppInfo appInfo = new AppInfo(response.getString("description"), response.getString("supportemail"), response.getString("websiteURL"), response.getString("discoveryUUID"));
         return appInfo;
      } catch(JSONException e) {
         listener.onError(new ServerError(1002, "Error on parsing the response JSON after the execution of AppInfo request", "")); //per sicurezza, per evitare inconsistenze. L'errore 1002 indica un errore in fase di parsing della risposta;
         return new AppInfo("", "", "", "");
      }
   }

   protected void updateLocalData(AppInfo data){
      SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(cx);
      SharedPreferences.Editor editor = preferences.edit();
      editor.remove("description");
      editor.putString("description", data.description);
      editor.remove("supportemail");
      editor.putString("supportemail", data.supportEmail);
      editor.remove("websiteURL");
      editor.putString("websiteURL", data.website);
      editor.remove("discoveryUUID");
      editor.putString("discoveryUUID", data.UUID);
      editor.apply();
   }
}
