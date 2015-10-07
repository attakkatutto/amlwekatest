/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.weka;

import aml.entity.Result;
import static aml.global.Constant.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

/**
 * Class for manage WEKA with java code
 * @author ddefalco
 */
public final class MyWekaManager {

    private int classIndex;
    private BufferedWriter bwr;
    private final String ROW_RESULT_FILE = " %s, %s, %s, %s, %s, %s \n";

    /**
     * Constructor with file that contains dataset
     */
    public MyWekaManager() {
        try {
            this.classIndex = 0;
            createFile();
        } catch (Exception ex) {
            Logger.getLogger(MyWekaManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * WEKA cross validation classifier method that calculate the
     * f-Measure of the model
     * @param dataset
     * @param classifier Abstract classifier that build model
     * @param options for the cassifier
     * @return double fMeasure value
     * @throws Exception 
     */
    protected double crossValidation(final File dataset, AbstractClassifier classifier, String[] options) throws Exception {
        
        CSVLoader loader = new CSVLoader();
        loader.setSource(dataset);
        /**
         * Load dataset in instances variable
         */
        Instances instances = loader.getDataSet();
        /**
         * Delete the first attribute of dataset
         * it's the transaction id attribute
         */
        instances.deleteAttributeAt(0);
        instances.setClassIndex(instances.numAttributes() - 1);
        classIndex = getClassNOIndex(instances);
        
        double _fMeasure = 0;
        if (options != null) {
            classifier.setOptions(options);
        }
        for (int run = 0; run < WEKA_RUNS; run++) {
            instances.stratify(FOLDS_NUMBER);
            for (int fold = 0; fold < FOLDS_NUMBER; fold++) {
                System.out.println(" run: " + run + " fold: " + fold);
                Instances _train = instances.trainCV(FOLDS_NUMBER, fold);
                Instances _test = instances.testCV(FOLDS_NUMBER, fold);
                classifier.buildClassifier(_train);
                Evaluation _evaluation = new Evaluation(_train);
                _evaluation.evaluateModel(classifier, _test);                
                _fMeasure += _evaluation.fMeasure(1);
            }
        }
        return _fMeasure / (WEKA_RUNS * FOLDS_NUMBER);
    }

    /**
     * Create the file to write te results from WEKA
     * @throws IOException 
     */
    private void createFile() throws IOException {
        /*Create the results file*/
        File filer = new File("." + File.separator + "dbfiles" + File.separator + "WEKA_RESULTS.CSV");
        FileWriter fwr = new FileWriter(filer.getAbsoluteFile(), true);
        bwr = new BufferedWriter(fwr);
    }

    /**
     * Calculate the f-Measure results from building models with
     * J48 - SMO - IBK algorithms 
     * @param paramName
     * @param paramValue 
     */
    public void calculateResults(File dataset,String paramName, double paramValue) {
        try {
            System.out.println("- Start algorithm J48");
            double _dt = crossValidation(dataset,new J48(), null);
            System.out.println("- Start algorithm SMO");
            double _svm = crossValidation(dataset,new SMO(), null); 
            System.out.println("- Start algorithm KNN");
            double _knn = crossValidation(dataset,new IBk(), new String[]{"-K", "3"});
            System.out.println("- Start algorithm Random Forest -");
            double _rf = crossValidation(dataset,new RandomForest(),new String[]{"-I", "100", "-K", "0", "-S", "1"});
            writeResult(new Result(paramName, paramValue, _dt, _svm, _knn, _rf));
        } catch (Exception ex) {
            Logger.getLogger(MyWekaManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Write result instance to file
     * @param r 
     */
    private void writeResult(Result r) {
        if (bwr != null) {
            try {
                bwr.append(String.format(ROW_RESULT_FILE, r.getParamName(), r.getParamValue(), r.getDecisiontree(), r.getSvm(), r.getKnn(), r.getRandomForest()));
                bwr.close();
            } catch (IOException ex) {
                Logger.getLogger(MyWekaManager.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
    
    /**
     * Get index of fraud class attribute (HONEST = 'NO')
     * @return index of Fraud Class
     */
    private int getClassNOIndex(Instances instances){
        int _ret = 0;
        for(int i = 0; i < instances.numClasses(); i++) {
         if (instances.classAttribute().value(i).trim().equals("NO")) _ret = i;
       }
        return _ret;
    }
}
