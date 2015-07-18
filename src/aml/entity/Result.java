/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.entity;

/**
 *
 * @author ddefalco
 */
public class Result {

    private String paramName;
    private double paramValue;
    private double dt;
    private double svm;
    private double knn;
    
    public Result() {
        this.dt = 0;
        this.svm = 0;
        this.knn = 0;
    }

    public Result(String paramName, double paramValue, double dt, double svm, double knn) {
        this.paramName = paramName;
        this.paramValue = paramValue;
        this.dt = dt;
        this.svm = svm;
        this.knn = knn;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String pn) {
        this.paramName = pn;
    }

    public double getParamValue() {
        return paramValue;
    }

    public void setParamValue(double pv) {
        this.paramValue = pv;
    }    
    
    public double getDecisiontree() {
        return dt;
    }

    public void setDecisiontree(double dt) {
        this.dt = dt;
    }

    public double getSvm() {
        return svm;
    }

    public void setSvm(double svm) {
        this.svm = svm;
    }

    public double getKnn() {
        return knn;
    }

    public void setKnn(double knn) {
        this.knn = knn;
    }
            
}
