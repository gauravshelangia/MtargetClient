package com.example.gaurav.mtargetclient;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.deeplearning4j.nn.modelimport.keras.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.UnsupportedKerasConfigurationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LocateMe extends Activity {

    int downloadedSize = 0;
    int totalSize = 0;
    ProgressBar pb;
    TextView cur_val;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_locate);

    }

    // on press buttoon open iiitv_ground_floor map
    public void locateme(View view){
        Toast.makeText(this,"inside locateme method",Toast.LENGTH_LONG).show();
        //boolean model_exists = modelexist();
        //if(model_exists){
            // call modeleval from kerasmodel import class


                GetRssi getRssi = new GetRssi(getApplicationContext());
                Pair<ArrayList<Integer>,ArrayList<Integer>> rssivec =  getRssi.getrssivec();
                ArrayList<Integer> rssi2ghz = rssivec.first;
                float[] rssi2ghz_float = convettofloat(rssi2ghz);
                Task1 task1 = new Task1(rssi2ghz_float);
                task1.execute();



            System.out.println("yes model exists or downloaded the data");
            // set to the
       // }else{

        //}



    }

    // check if model exist or download if does not exist
    boolean modelexist () {
        boolean exist = false;

        File dir = new File(Environment.getExternalStorageDirectory(), "/MTarget");
        if (dir.exists()) {
            File modelfile =new File(dir + "MyMultiLayerNetwork.zip") ;
            //System.out.println("modelfile " +modelfile.exists());
            if (!modelfile.isFile()) {
                System.out.print("file doesnot exists creating a new one ");
                modelfile = new File(dir, "MyMultiLayerNetwork.zip");
                try {
                    boolean filecreated = modelfile.createNewFile();
                    System.out.println("file created or  not is " + filecreated);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final String filepath = "http://ec2-35-154-36-86.ap-south-1.compute.amazonaws.com/" +
                        "MTarget_Server/MyMultiLayerNetwork.zip";
                showProgress(filepath);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadfile(filepath, "MyMultiLayerNetwork.zip");
                    }
                }).start();

                Log.d("yes model exist", "yes");
                exist = true;
            }
        } else {
            dir.mkdir();
            File modelfile =new File(dir + "MyMultiLayerNetwork.zip") ;
            //System.out.println("modelfile " +modelfile.exists());
            if (!modelfile.isFile()) {
                System.out.print("file doesnot exists creating a new one ");
                modelfile = new File(dir, "MyMultiLayerNetwork.zip");
                try {
                    boolean filecreated = modelfile.createNewFile();
                    System.out.println("file created or  not is " + filecreated);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final String filepath = "http://ec2-35-154-36-86.ap-south-1.compute.amazonaws.com/" +
                        "MTarget_Server/MyMultiLayerNetwork.zip";
                showProgress(filepath);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadfile(filepath, "MyMultiLayerNetwork.zip");
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
        System.out.print("download function called\n");
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

    void showProgress(String file_path){
        dialog = new Dialog(LocateMe.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.myprogressdialog);
        dialog.setTitle("Download Progress");

        TextView text = (TextView) dialog.findViewById(R.id.tv1);
        text.setText("Downloading file from ... " + file_path);
        cur_val = (TextView) dialog.findViewById(R.id.cur_pg_tv);
        cur_val.setText("Starting download...");
        dialog.show();

        pb = (ProgressBar)dialog.findViewById(R.id.progress_bar);
        pb.setProgress(0);
        pb.setProgressDrawable(getResources().getDrawable(R.drawable.green_progress));
    }

    // async task to compute the tile number from rssivector
    class Task1 extends AsyncTask<Void, Void, String> {
        float[] rssi2ghz;
        public Task1(float[] rssi2ghz){
            super();
            this.rssi2ghz = rssi2ghz;
        }
        ImportKerasModel kerasmodel = new ImportKerasModel();
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(Void... arg0)
        {
            //Record method

            try {
                int tile_number = kerasmodel.ModelEval(rssi2ghz);
                System.out.println("Yes it is after the kerasmodel.ModelEval" + tile_number);

                // go to ground floor map
                if (tile_number<=624){
                    Intent intent = new Intent(getApplicationContext(),IiitvGroundFloor.class);
                    intent.putExtra("tile_number" , tile_number);
                    startActivity(intent);
                }
                // go to first floor map
                if (tile_number>624){
                    Intent intent1 = new Intent(getApplicationContext(),IiiitvFirstFloor.class);
                    intent1.putExtra("tile_number" , tile_number-624);
                    startActivity(intent1);
                }

            } catch (UnsupportedKerasConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidKerasConfigurationException e) {
                e.printStackTrace();
            }
            return null ;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

        }
    }

    public float[] convettofloat(ArrayList<Integer> array){
        float[] farray = new float[array.size()];
        int size = array.size();
        for (int i=0;i<size;i++){
            farray[i] = (float) array.get(i);
        }
        return farray;
    }
}
