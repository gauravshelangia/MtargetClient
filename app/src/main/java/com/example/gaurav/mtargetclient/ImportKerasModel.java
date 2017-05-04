package com.example.gaurav.mtargetclient;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.SystemClock;

import org.apache.commons.lang3.ObjectUtils;
import org.bytedeco.javacpp.RealSense;
import org.deeplearning4j.nn.modelimport.keras.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.UnsupportedKerasConfigurationException;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.*;
import java.util.List;

import static com.example.gaurav.mtargetclient.MainActivity.appcontext;

/**
 * Created by gaurav on 12/4/17.
 */


public class ImportKerasModel extends Activity {
    private static Logger log = LoggerFactory.getLogger(ImportKerasModel.class);

    public int ModelEval(float[] RSSI) throws IOException {

        //String modelJsonFilename = Environment.getExternalStorageState()+"Mtarget/model_json";
        //String weightsHdf5Filename = Environment.getExternalStorageState()+"Mtarget/Mtarget_weights";
        //String modelHdf5Filename = Environment.getExternalStorageState()+"Mtarget/mtarget_model_full.h5";
        //String modelzip = Environment.getExternalStorageState()+"/Mtarget/MyMultiLayerNetwork.zip";
        //boolean enforceTrainingConfig = false;
        File dir = new File(Environment.getExternalStorageDirectory(),"/MTarget");
        File modelzip = new File(dir,"MyMultiLayerNetwork.zip");
//        AssetManager am = getApplicationContext().getAssets();
        //if(am == null){
         //   System.out.println("Null berereavass");
        //}
        //InputStream inputStream = appcontext.getResources().openRawResource(getResources().
        //        getIdentifier("mymultilayernetwork","raw",getPackageName()));
        //File modelzip = createFileFromInputStream(inputStream);

        // load model from two different file one : json flie having json config and another: weights file
        //MultiLayerNetwork model =  KerasModelImport.importKerasSequentialModelAndWeights(modelJsonFilename,weightsHdf5Filename);
        System.out.println("size of model is " );
        // load model from a single file h5 (created by save.model('filenametosave.h5') )
        //MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(modelHdf5Filename);

        System.out.println("printing something : " + modelzip);
        //Load the model
        MultiLayerNetwork restored = ModelSerializer.restoreMultiLayerNetwork(modelzip);

        // print model config
        //System.out.println(restored.conf().toJson());
        // create row vector to pass to model
        INDArray myArray = Nd4j.zeros(1, 26); // one row 4 column array

        float[] rssiinput = RSSI;
        INDArray fromjavaarray = Nd4j.create(rssiinput);

        int tile_number = 0;
        INDArray result = restored.output(fromjavaarray);
        //System.out.println("Tile location is " + result);

        INDArray tile_as_indarray = Nd4j.argMax(result);
        tile_number = tile_as_indarray.getInt(0,0);
        // TODO get the tile number from result return it
        System.out.println("tile number is " + tile_number);
        return tile_number;
    }
    private File createFileFromInputStream(InputStream inputStream) {

        try{
            File f = new File("MyMultiLayerNetwork.zip");
            OutputStream outputStream = new FileOutputStream(f);
            byte buffer[] = new byte[1024];
            int length = 0;

            while((length=inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,length);
            }

            outputStream.close();
            inputStream.close();

            return f;
        }catch (IOException e) {
            //Logging exception
        }

        return null;
    }
}
