/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.main;

import aml.global.Config;
import static aml.global.Constant.*;
import aml.graph.Network;
import aml.jade.MyPlatformManager;

/**
 * Main class for the network
 *
 * @author ddefalco
 */
public class Main {

    /**
     * Entry point of the AML Synthetic data generator
     *
     * @param args
     */
    public static void main(String[] args) {
        switch(SIMULATOR_MODE) {
            case 0: mainSimulator();
            case 1: mainSimulatorWekaTest();
        }      
    }
    
    /**
     * Simulate data
     */
    public static void mainSimulator() {
        Network graph = new Network("AML Synthetic DB");
        if (Config.instance().isGuiEnabled()) {
            graph.enableGUI();
        }
        graph.build();
        MyPlatformManager f = new MyPlatformManager(graph);
        f.exec();
    }
    
    /**
     * Weka test of the simulated data
     */
    public static void mainSimulatorWekaTest(){
        MyApplication ma = new MyApplication();
        ma.exec();
    }
}
