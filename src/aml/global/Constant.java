/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.global;

/**
 * Constants of the Network
 *
 * @author DAVIDE
 */
public final class Constant {    
    
    /*    
    * Number of months    
    */
    public static final int MONTHS = 12;
    
    /*    
    * Start year    
    */
    public static final short START_YEAR = 2005;

    /**
     * Default damping factor
     */
    public static final double DEFAULT_DAMPING_FACTOR = 0.85;

    /**
     * Default precision
     */
    public static final double DEFAULT_PRECISION = 1.0e-5;

    /**
     * Default rank attribute
     */
    public static final String DEFAULT_RANK_ATTRIBUTE = "PageRank";

    /**
     * WEKA constant parameters for testing simulator
     */
    public static final int WEKA_RUNS = 5;
    public static final int FOLDS_NUMBER = 10;
    
    /**
     * 0 Simulate data
     * 1 Simulate data and Weka algorithm
     */
    public static final int SIMULATOR_MODE = 1;
}
