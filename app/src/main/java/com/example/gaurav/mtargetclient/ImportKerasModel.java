package com.example.gaurav.mtargetclient;

import org.deeplearning4j.nn.modelimport.keras.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.UnsupportedKerasConfigurationException;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.indexing.NDArrayIndex;
import  org.slf4j.Logger;
import  org.slf4j.LoggerFactory;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.nd4j.linalg.factory.Nd4j;



import org.deeplearning4j.*;

import java.io.IOException;


/**
 * Created by gaurav on 12/4/17.
 */


public class ImportKerasModel {
    private static Logger log = LoggerFactory.getLogger(ImportKerasModel.class);

    public int ModelEval() throws UnsupportedKerasConfigurationException, IOException, InvalidKerasConfigurationException {

        //String modelJsonFilename = "PATH TO EXPORTED JSON FILE";
        //String weightsHdf5Filename = "PATH TO EXPORTED WEIGHTS HDF5 ARCHIVE";
        String modelHdf5Filename = "Mtarget/mtarget_model_full.h5";
        boolean enforceTrainingConfig = false;

        // load model from two different file one : json flie having json config and another: weights file
        //MultiLayerNetwork model =  KerasModelImport.importKerasSequentialModelAndWeights(modelJsonFilename,weightsHdf5Filename);

        // load model from a single file h5 (created by save.model('filenametosave.h5') )
        MultiLayerNetwork model = KerasModelImport.importKerasSequentialModelAndWeights(modelHdf5Filename);

        // print model config
        System.out.println(model.conf().toJson());

        // create row vector to pass to model
        INDArray myArray = Nd4j.zeros(1, 4); // one row 4 column array

        //int[] rssiinput = null ;
        //INDArray fromjavaarray = Nd4j.create(rssiinput);

        int tile_number = 0;
        INDArray result = model.output(myArray,enforceTrainingConfig);

        INDArray tile = result.get(NDArrayIndex.point(0),NDArrayIndex.point(0) );

        // TODO get the tile number from result return it

        return tile_number;
    }
}
