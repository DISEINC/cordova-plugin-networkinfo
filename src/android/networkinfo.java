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
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

public class networkinfo extends CordovaPlugin {
	private static String TAG = "cordova-plugin-networkinfo";

	private JSONObject getInetAddressInfo(InetAddress address) throws JSONException {
		if (address == null)
			return null;
		JSONObject obj = new JSONObject();
		obj.put("canonicalHostName", address.getCanonicalHostName());
		obj.put("hostName", address.getHostName());
		obj.put("hostAddress", address.getHostAddress());
		obj.put("isAnyLocalAddress", address.isAnyLocalAddress());
		obj.put("isLinkLocalAddress", address.isLinkLocalAddress());
		obj.put("isLoopbackAddress", address.isLoopbackAddress());
		obj.put("isMCGlobal", address.isMCGlobal());
		obj.put("isMCLinkLocal", address.isMCLinkLocal());
		obj.put("isMCNodeLocal", address.isMCNodeLocal());
		obj.put("isMCOrgLocal", address.isMCOrgLocal());
		obj.put("isMCSiteLocal", address.isMCSiteLocal());
		obj.put("isSiteLocalAddress", address.isSiteLocalAddress());
		obj.put("isMulticastAddress", address.isMulticastAddress());
		return obj;
	}
	
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		try {
			Log.i(TAG, "Action: "+action);
			if ("getNetworkInfo".equals(action)) {
				JSONArray infoArray = new JSONArray();
				List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
				for (NetworkInterface networkInterface : interfaces) {
					JSONObject infoObject = new JSONObject();
					infoObject.put("name", networkInterface.getName());
					infoObject.put("displayName", networkInterface.getDisplayName());
					infoObject.put("MTU", networkInterface.getMTU());
					infoObject.put("isPointToPoint", networkInterface.isPointToPoint());
					infoObject.put("isUp", networkInterface.isUp());
					infoObject.put("isVirtual", networkInterface.isVirtual());
					infoObject.put("isLoopback", networkInterface.isLoopback());
					infoObject.put("supportsMulticast", networkInterface.supportsMulticast());

					StringBuilder buf = new StringBuilder();
					byte[] mac = networkInterface.getHardwareAddress();
					if (mac != null) {
						for (int idx=0; idx<mac.length; idx++) {
							buf.append(String.format("%02X:", mac[idx]));
						}
						if (buf.length()>0) {
							buf.deleteCharAt(buf.length()-1);
						}
						infoObject.put("hardwareAddress", buf.toString());
					}

					// This is the same as interface address(?)
					/*JSONArray addressArray = new JSONArray();
					List<InetAddress> inetAddress = Collections.list(networkInterface.getInetAddresses());
					for (InetAddress address : inetAddress) {
						addressArray.put(getInetAddressInfo(address));
					}
					infoObject.put("addresses", addressArray);*/

					JSONArray interfaceAddressArray = new JSONArray();
					List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
					for (InterfaceAddress address : interfaceAddresses) {
						JSONObject addressObject = new JSONObject();
						addressObject.put("address", getInetAddressInfo(address.getAddress()));
						addressObject.put("broadcast", getInetAddressInfo(address.getBroadcast()));
						addressObject.put("networkPrefixLength", address.getNetworkPrefixLength());
						interfaceAddressArray.put(addressObject);
					}
					infoObject.put("interfaceAddresses", interfaceAddressArray);
					infoArray.put(infoObject);
				}
				callbackContext.success(infoArray);
				return true;			
			} else {
				callbackContext.error("Error no such method '" + action + "'");
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
			callbackContext.error("Error while retrieving network information: " + e.toString() + ", " + e.getMessage());
			return false;
		}
	}
}
