package com.example.gaurav.mtargetclient;

import android.os.Environment;
import android.os.SystemClock;

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
import java.io.IOException;
import java.nio.*;
import java.util.List;

/**
 * Created by gaurav on 12/4/17.
 */


public class ImportKerasModel {
    private static Logger log = LoggerFactory.getLogger(ImportKerasModel.class);

    public int ModelEval(float[] RSSI) throws UnsupportedKerasConfigurationException, IOException, InvalidKerasConfigurationException {

        //String modelJsonFilename = Environment.getExternalStorageState()+"Mtarget/model_json";
        //String weightsHdf5Filename = Environment.getExternalStorageState()+"Mtarget/Mtarget_weights";
        //String modelHdf5Filename = Environment.getExternalStorageState()+"Mtarget/mtarget_model_full.h5";
        //String modelzip = Environment.getExternalStorageState()+"/Mtarget/MyMultiLayerNetwork.zip";
        //boolean enforceTrainingConfig = false;
        File dir = new File(Environment.getExternalStorageDirectory(),"/MTarget");
        File modelzip = new File(dir,"MyMultiLayerNetwork.zip");

        // load model from two different file one : json flie having json config and another: weights file
        //MultiLayerNetwork model =  KerasModelImport.importKerasSequentialModelAndWeights(modelJsonFilename,weightsHdf5Filename);

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
}
