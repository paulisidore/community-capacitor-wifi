package com.digaus.capacitor.wifi;

import android.Manifest;
import android.os.Build;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.PermissionState;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.PermissionCallback;

import java.net.*;
import java.util.*;

@CapacitorPlugin(
    name = "Wifi",
    permissions = {
        @Permission(
            alias = "fineLocation",
            strings = { Manifest.permission.ACCESS_FINE_LOCATION }
        ),
        @Permission(
            alias = "wifiState",
            strings = { Manifest.permission.ACCESS_WIFI_STATE }
        ),
        @Permission(
            alias = "networkState",
            strings = { Manifest.permission.ACCESS_NETWORK_STATE }
        ),
    }
)
public class WifiPlugin extends Plugin {

    private static final int API_VERSION = Build.VERSION.SDK_INT;

    WifiService wifiService;
    public String lastIpAdr = "";

    @Override
    public void load() {
      super.load();
      this.wifiService = new WifiService();
      this.wifiService.load(this.bridge,this);
      this.wifiService.wifi=this;
    }
    
    @PluginMethod()
    public void getIP(PluginCall call) {
       /*  String myIp = this.getMyIPAddress(true);
        Log.d("Capacitor","IPv4 trouvé est: "+myIp);
        if (myIp !=""){
            this.lastIpAdr = myIp;
            JSObject result = new JSObject();
            result.put("ip", myIp);
            notifyListeners("onIpV4Disponible",result);
            call.resolve(result);
            return ;
        } */
        Log.d("Capacitor","Demande IpV4 depuis WifiPlugIn Service getIP...");
        if (getPermissionState("networkState") != PermissionState.GRANTED ){
            Log.d("Capacitor","Demande de permission pour networkState...");
            requestPermissionForAlias("networkState", call,"accessNetworkStatePermission") ;
        }else{
            Log.d("Capacitor","Demande Adresse IpV4 en cour avec WifiPlugIn Service...");
            this.lastIpAdr = this.wifiService.getIP(call);
            //JSObject result = new JSObject();
            //result.put("ip", this.lastIpAdr);
            //notifyListeners("onIpV4Disponible",result);
            //call.resolve(result);
        }
        //if (API_VERSION >= 23 && getPermissionState("fineLocation") != PermissionState.GRANTED) {
            //requestPermissionForAlias("fineLocation", call, "accessFineLocation");
        //} else {
            
        //}
    }

    @PluginMethod()
    public void getSSID(PluginCall call) {
        if (getPermissionState("fineLocation") != PermissionState.GRANTED) {
            requestPermissionForAlias("fineLocation", call, "accessFineLocation");
        } else {
            this.wifiService.getSSID(call);
        }
    }

    @PluginMethod()
    public void connect(PluginCall call) {
        if (!call.getData().has("ssid")) {
            call.reject("Must provide an ssid");
            return;
        }
        if (API_VERSION >= 23 && getPermissionState("fineLocation") != PermissionState.GRANTED) {
            requestPermissionForAlias("fineLocation", call, "accessFineLocation");
        } else {
            this.wifiService.connect(call);
        }

    }

    @PluginMethod()
    public void connectPrefix(PluginCall call) {
        if (!call.getData().has("ssid")) {
            call.reject("Must provide an ssid");
            return;
        }
        if (API_VERSION >= 23 && getPermissionState("fineLocation") != PermissionState.GRANTED) {
            requestPermissionForAlias("fineLocation", call, "accessFineLocation");
        } else {
            this.wifiService.connectPrefix(call);
        }

    }

    @PluginMethod()
    public void disconnect(PluginCall call) {
        this.wifiService.disconnect(call);
    }

    @PermissionCallback
    private void accessFineLocation(PluginCall call) {
        if (getPermissionState("fineLocation") == PermissionState.GRANTED) {
            if (call.getMethodName().equals("getSSID")) {
                this.wifiService.getSSID(call);
            } else if (call.getMethodName().equals("getIP")) {
                this.wifiService.getIP(call);
            } else if (call.getMethodName().equals("connect")) {
                this.wifiService.connect(call);
            } else if (call.getMethodName().equals("connectPrefix")) {
                this.wifiService.connectPrefix(call);
            }
        } else {
            call.reject("User denied permission");
        }
    }
    
    @PermissionCallback
    private void accessNetworkStatePermission(PluginCall call) {
        if (getPermissionState("networkState") == PermissionState.GRANTED) {
            Log.d("Capacitor","Permission networkState OK");
        } else {
            call.reject("User denied permission pour networkState.");
        }
    }

    /**
     * Get IP address from first non-localhost interface
     * @param useIPv4   true=return ipv4, false=return ipv6
     * @return  address or empty string
     */
    public String getMyIPAddress(boolean useIPv4) {
        try {
            Log.d("Capacitor", "Recherche Adresse IP depuis la fonction getIPAddress...");
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':')<0;
                        if (useIPv4) {
                            if (isIPv4){
                                Log.d("Capacitor","Adresse IPv4 trouvée: "+sAddr);
                                return sAddr;
                            }
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) { } // for now eat exceptions
        return "";
    }

    /**
     * Informe les listerners
     * @param eventName
     * @param data
     */
    @PluginMethod()
    public void raiseEvent(String eventName, JSObject data){
        notifyListeners(eventName,data);
    }
}
