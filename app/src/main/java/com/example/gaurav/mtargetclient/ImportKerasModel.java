package com.example.gaurav.mtargetclient;

import android.os.Environment;
import org.deeplearning4j.nn.modelimport.keras.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.UnsupportedKerasConfigurationException;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.nd4j.linalg.factory.Nd4j;
import java.io.IOException;
import java.nio.*;

/**
 * Created by gaurav on 12/4/17.
 */


public class ImportKerasModel {
    private static Logger log = LoggerFactory.getLogger(ImportKerasModel.class);

    public int ModelEval() throws UnsupportedKerasConfigurationException, IOException, InvalidKerasConfigurationException {

        String modelJsonFilename = Environment.getExternalStorageState()+"Mtarget/model_json";
        String weightsHdf5Filename = Environment.getExternalStorageState()+"Mtarget/Mtarget_weights";
        String modelHdf5Filename = Environment.getExternalStorageState()+"Mtarget/mtarget_model_full.h5";
        boolean enforceTrainingConfig = false;

        // load model from two different file one : json flie having json config and another: weights file
        MultiLayerNetwork model =  KerasModelImport.importKerasSequentialModelAndWeights(modelJsonFilename,weightsHdf5Filename);

        // load model from a single file h5 (created by save.model('filenametosave.h5') )
        //MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(modelHdf5Filename);

        // print model config
        System.out.println(model.conf().toJson());
        // create row vector to pass to model
        INDArray myArray = Nd4j.zeros(1, 26); // one row 4 column array

        int[] rssiinput = {-100,-50,-100,-71,-100,-100,-100,-100,-100,-100,-67,-66,-100,-100,-100,-100,-67,-100,-100,-100,-49,-67,-71,-100,-100,-100} ;
        INDArray fromjavaarray = Nd4j.create(rssiinput);

        int tile_number = 0;
        INDArray result = model.output(fromjavaarray);
        System.out.println("Tile location is " + result);
        //Toast.makeText(getApplicationContext(),"model output is "+result.toString(),Toast.LENGTH_LONG).show();
        //INDArray tile = result.get(NDArrayIndex.point(0),NDArrayIndex.point(0) );

        // TODO get the tile number from result return it

        return tile_number;
    }
}
