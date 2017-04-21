package com.example.gaurav.mtargetclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gaurav on 28/2/17.
 */

public class GetRssi  {

   static ArrayList<String> wifibssid = new ArrayList<>(Arrays.asList(
            "2c:c5:d3:23:c2:3c","58:b6:33:26:53:28","58:b6:33:26:53:2c","2c:c5:d3:23:c2:38","58:b6:33:26:51:48","58:b6:33:26:56:cc"
            ,"24:c9:a1:49:a7:28","24:c9:a1:47:29:58","24:c9:a1:47:2f:38","24:c9:a1:49:9a:c8","6c:aa:b3:48:ad:b8","24:c9:a1:49:a1:58",
            "24:c9:a1:49:9f:08",
            "2c:c5:d3:63:c2:3c","24:c9:a1:09:a7:28","24:c9:a1:07:29:58","24:c9:a1:09:a1:58","24:c9:a1:09:9a:c8","24:c9:a1:07:2f:38"
            ,"58:b6:33:66:53:2c","58:b6:33:66:53:28","6c:aa:b3:08:ad:b8","2c:c5:d3:63:c2:38","58:b6:33:66:51:48","24:c9:a1:09:9f:08",
            "58:b6:33:66:56:cc"
    ));

    static public WifiManager wifimanager;
    public WifiReciever receiverWifi;
    Context context;
    static public ArrayList<Listitem> wlist = new ArrayList<Listitem>();
    List<ScanResult> wifiList = new ArrayList<>();



    public GetRssi(Context context){
        this.context = context;
        // register wifi reciever
        wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifimanager == null)
            Log.e("errore n " ,"wifimanager");
        if (wifimanager.isWifiEnabled() == false) {
            // If wifi disabled then enable it
            Toast.makeText(context, "wifi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();
            wifimanager.setWifiEnabled(true);
        }

        receiverWifi = new WifiReciever();

        context.registerReceiver(receiverWifi,new IntentFilter(wifimanager.SCAN_RESULTS_AVAILABLE_ACTION));

    }

    class WifiReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context cont, Intent intent) {
            System.out.println("Starting scan");
            //Toast.makeText(getApplication(), "DONE SCANNING", Toast.LENGTH_LONG).show();
            System.out.println("Done scanning and size of wlist is "+wifiList.size());
        }
    }

    // convert wlist into pair of rssi2ghz and rssi5ghz
    public Pair<ArrayList<Integer>,ArrayList<Integer>> getrssivec(){
        wifimanager.startScan();
        wifiList = wifimanager.getScanResults();
        context.unregisterReceiver(receiverWifi);
        wlist.clear();

        if(wifimanager==null){
            Log.e("wifimanager is null","");
        }
        System.out.println("size of wifi list " + wifiList.size());
        for (ScanResult result : wifiList) {
            Listitem LI = new Listitem();
            LI.ssid = result.SSID;
            LI.bssid = result.BSSID;
            LI.strength = result.level;
            LI.freq = result.frequency;
            wlist.add(LI);
            //System.out.println("SSID :  " + LI.ssid + "\t BSSID : " + LI.bssid + "Freq " + LI.freq);
        }

        Pair<ArrayList<Integer> ,ArrayList<Integer> > rssivec ;
        int no_wifi = wifibssid.size();
        ArrayList<Integer> rssi2ghz = new ArrayList<>();
        ArrayList<Integer> rssi5ghz = new ArrayList<>();

        for (int i=0;i<no_wifi;i++){
            rssi2ghz.add(-100);
            rssi5ghz.add(-100);
        }

        for (Listitem l : wlist) {
            if (wifibssid.contains(l.bssid)){
                int index = wifibssid.indexOf(l.bssid);
                if(l.freq <= 2800 && l.freq >=1800){
                    rssi2ghz.set(index, l.strength);
                    //System.out.println("value is " +l.strength);
                }
                if(l.freq <= 5500 && l.freq >=4800){
                    rssi5ghz.set(index,l.strength);
                }
            }else{
                Log.e("NOt our wifi ","");
            }
        }

        rssivec = new Pair<>(rssi2ghz,rssi5ghz);
        return rssivec;
    }
}
