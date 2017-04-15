package com.example.gaurav.mtargetclient;

import org.deeplearning4j.nn.graph.ComputationGraph;
import  org.slf4j.Logger;
import  org.slf4j.LoggerFactory;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;



import org.deeplearning4j.*;


/**
 * Created by gaurav on 12/4/17.
 */


public class ImportKerasModel {
    private static Logger log = LoggerFactory.getLogger(ImportKerasModel.class);

    public void ModelEval(){

        String modelJsonFilename = "PATH TO EXPORTED JSON FILE";
        String weightsHdf5Filename = "PATH TO EXPORTED WEIGHTS HDF5 ARCHIVE";
        String modelHdf5Filename = "PATH TO EXPORTED FULL MODEL HDF5 ARCHIVE";
        boolean enforceTrainingConfig = false;

        MultiLayerNetwork model =  Kera
        //ComputationGraph model = KerasModelImport.importKerasModelAndWeights(modelJsonFilename, weightsHdf5Filename, enforceTrainingConfig);



    }
}
