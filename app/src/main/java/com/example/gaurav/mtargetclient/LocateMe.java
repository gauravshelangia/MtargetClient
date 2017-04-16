package com.example.gaurav.mtargetclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LocateMe extends AppCompatActivity {

    int downloadedSize = 0;
    int totalSize = 0;
    ProgressBar pb;
    TextView cur_val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_locate);

    }

    // on press buttoon open iiitv_ground_floor map
    public void locateme(View view){
        boolean model_exists = modelexist();
        if(model_exists){
            // call modeleval from kerasmodel import class
            // set to the
        }else{

        }


        //Intent intent = new Intent(getApplicationContext(),IiitvGroundFloor.class);
        //startActivity(intent);

        Intent intent1 = new Intent(getApplicationContext(),IiiitvFirstFloor.class);
        startActivity(intent1);
    }

    // check if model exist or download if does not exist
    boolean modelexist (){
        boolean exist = false;

        File dir = new File(Environment.getExternalStorageDirectory(),"/MTarget");
        if(dir.exists()) {
            File modelfile = new File(dir, "mtarget_model_full.h5");

            if (!modelfile.exists()) {
                final String filepath = "http://ec2-35-154-36-86.ap-south-1.compute.amazonaws.com/" +
                        "MTarget_Server/mtarget_model_full.h5";

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadfile(filepath, "mtarget_model_full.h5");
                    }
                }).start();

                Log.d("yes model exist", "yes");
                exist = true;
            }
        }
        return exist;
    }

    // download file
    void downloadfile(String filelink, String file){
        String filepath = filelink;
        try {

            URL url = new URL(filepath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            // connect
            urlConnection.connect();

            File dir = new File(Environment.getExternalStorageDirectory(),"/MTarget");
            File todownload = new File(dir,file);

            FileOutputStream fileOutputStream = new FileOutputStream(todownload);
            InputStream inputStream = urlConnection.getInputStream();

            totalSize = urlConnection.getContentLength();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setMax(totalSize);
                }
            });

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutputStream.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                // update the progressbar //
                runOnUiThread(new Runnable() {
                    public void run() {
                        pb.setProgress(downloadedSize);
                        float per = ((float)downloadedSize/totalSize) * 100;
                        cur_val.setText("Downloaded " + downloadedSize + "KB / " + totalSize + "KB (" + (int)per + "%)" );
                    }
                });
            }
            //close the output stream when complete //
            fileOutputStream.close();
            runOnUiThread(new Runnable() {
                public void run() {
                    // pb.dismiss(); // if you want close it..
                }
            });


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
