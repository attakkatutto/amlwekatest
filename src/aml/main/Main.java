/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.main;

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
        // TODO code application logic here
        MyApplication ma = new MyApplication();
        ma.exec();
    }
    
    /**
     * Entry point of the AML Synthetic data generator
     *
     * @param args
     */
//    public static void main(String[] args) {
//        Network graph = new Network("AML Synthetic DB");
//        if (Config.instance().isGuiEnabled()) {
//            enableGUI(graph);
//        }
//        graph.build();
//        MyPlatformManager f = new MyPlatformManager(graph);
//        f.exec();
//    }
    
}
