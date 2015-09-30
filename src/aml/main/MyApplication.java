/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.main;

import aml.global.Config;
import aml.graph.Network;
import aml.jade.MyPlatformManager;
import aml.weka.MyWekaManager;
import java.io.File;

/**
 * MyApplication class manage the multiple execution of the simulator for
 * testing with WEKA algorithm
 *
 * @author ddefalco
 */
public class MyApplication {

    private int counter;
    private double currstep;
    protected File file;
    private final String PARAMETER_NAME = Config.instance().getParameterName();
    private final int RANGE_VALUE = Config.instance().getRangeValue();
    private final double STEP_VALUE = Config.instance().getStepValue();

    /**
     * Constructor for MyApplication class
     */
    public MyApplication() {
        this.counter = 0;        
        switch (PARAMETER_NAME) {
            case "P1":
                this.currstep  = Config.instance().getParentProbability();
                break;
            case "P2":
                this.currstep  = Config.instance().getPartnerProbability();
                break;
            case "P3":
                this.currstep  = Config.instance().getDummyProbability();
                break;
            case "P4":
                this.currstep  = Config.instance().getLaundererPercentage();
                break;
        }
    }

    /**
     * Execute the main step of the simulator
     */
    public void exec() {
        Network graph = new Network("AML Synthetic DB");
        if (Config.instance().isGuiEnabled()) {
            graph.enableGUI();
        }
        graph.build();
        switch (PARAMETER_NAME) {
            case "P1":
                Config.instance().setParentProbability(currstep);
                break;
            case "P2":
                Config.instance().setPartnerProbability(currstep);
                break;
            case "P3":
                Config.instance().setDummyProbability(currstep);
                break;
            case "P4":
                Config.instance().setLaundererPercentage((int) currstep);
                break;
        }
        MyPlatformManager f = new MyPlatformManager(graph);
        f.register(this);
        file = f.getTransactionFile();
        f.exec();
    }    

    /**
     * Stop the application if the cycle finishes
     */
    public void halt() {
        MyWekaManager weka = new MyWekaManager(file);
        double param = 0;
        switch (PARAMETER_NAME) {
            case "P1":
                param = Config.instance().getParentProbability();
                break;
            case "P2":
                param = Config.instance().getPartnerProbability();
                break;
            case "P3":
                param = Config.instance().getDummyProbability();
                break;
            case "P4":
                param = Config.instance().getLaundererPercentage();
                break;
        }
        weka.calculateResults(PARAMETER_NAME, param);
        if (counter < RANGE_VALUE) {
            counter++;
            currstep += STEP_VALUE;
            exec();
        } else {
            System.exit(0);
        }
    }
}
