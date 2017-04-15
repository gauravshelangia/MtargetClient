package com.example.gaurav.mtargetclient;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int userid;
    static String ip = "ec2-35-154-36-86.ap-south-1.compute.amazonaws.com";
    //static String ip = "10.100.109.196";
    String username;
    EditText edittextuserid,edittextusername;
    Button sendtoserver;
    String macaddr,devicemodel,deviceman,devtype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_main);

        //Intent intent = new Intent(getApplicationContext(),Floorto.class);
        //startActivity(intent);

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        getpermission();

        // find the edittext
        edittextuserid = (EditText) findViewById(R.id.userid);
        edittextusername = (EditText) findViewById(R.id.username);

        // Get mac address of device
        //WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //WifiInfo wInfo = wifiManager.getConnectionInfo();
        //macaddr = wInfo.getMacAddress();
        //System.out.println("Mac address is : " + getMacAddr());

        macaddr = getMacAddr();
        // get the device type = model and manufactrure
        devicemodel = android.os.Build.MODEL;
        deviceman = android.os.Build.MANUFACTURER;
        devtype = deviceman+devicemodel;
        System.out.println("devtype :" + devtype);

        sendtoserver = (Button) findViewById(R.id.sendandnext);
        sendtoserver.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String userids = edittextuserid.getText().toString();
        if(userids!= null){
            userid = Integer.parseInt(userids);
        }
        username = edittextusername.getText().toString();
        ArrayList<Pair<String,String>> data_to_send = new ArrayList<>();
        data_to_send.add(new Pair<String, String>("userid", userids));
        data_to_send.add(new Pair<String, String>("username", username));
        data_to_send.add(new Pair<String, String>("macaddr", macaddr));
        data_to_send.add(new Pair<String, String>("dev_type", devtype));

        // send data to server
        String addr = "http://"+ip+"/MTarget_Server/add_user_device.php";
        RequestServer rs = new RequestServer();
        RequestServer.Senduseranddevicetoserver sendtoserver = new RequestServer.Senduseranddevicetoserver(addr,data_to_send);
        sendtoserver.execute();

        //Intent intent = new Intent(getApplicationContext(),Starttakingreading.class);
        Intent intent = new Intent(getApplicationContext(),Floorto.class);
        startActivity(intent);


    }

    // method to get permissions at runtime
    public void getpermission() {
        // obtain permission for location and storage
        ActivityCompat.requestPermissions(MainActivity.this,new String []{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION}
                ,1);
    }

    public static String getMacAddr() {
        //System.out.println("inside the macaddress method");
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                //System.out.println("network name are : "+ nif.toString());
                if (!nif.getName().contains("wlan")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            System.out.println("Error in getmacaddressethod");
        }
        return "02:00:00:00:00:00";
    }

    // for permission of storage and location
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
