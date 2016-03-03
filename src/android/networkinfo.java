package se.ramnet.cordovaplugin.networkinfo;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class networkinfo extends CordovaPlugin {
	private static String TAG = "cordova-plugin-networkinfo";

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try {
			Log.i(TAG, "Action: "+action);
			if ("getNetworkInfo".equals(action)) {
				Log.i(TAG, "TEST");
				callbackContext.success("TEST");
				return true;
			} else {
				callbackContext.error("Error no such method '" + action + "'");
				return false;
			}
		} catch(Exception e) {
			callbackContext.error("Error while retrieving network information: " + e.getMessage());
			return false;
		}
	}

}
