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
import aml.weka.MyWekaManager;
import java.io.File;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 * MyApplication class manage the multiple execution of the simulator for
 * testing with WEKA algorithm
 *
 * @author ddefalco
 */
public class MyApplication {

    private int counter;
    private int currstep;
    protected File file;

    public MyApplication() {
        this.counter = 0;
        this.currstep = 0;
    }

    /**
     * Execute the main step of the simulator
     */
    public void exec() {
        Network graph = new Network("AML Synthetic DB");
        if (Config.instance().isGuiEnabled()) {
            enableGUI(graph);
        }
        graph.build();
        double param;
        switch (PARAMETER_NAME) {
            case "P1":
                param = Config.instance().getParentProbability() + currstep;
                Config.instance().setParentProbability(param);
                break;
            case "P2":
                param = Config.instance().getPartnerProbability() + currstep;
                Config.instance().setPartnerProbability(param);
                break;
            case "P3":
                param = Config.instance().getDummyProbability() + currstep;
                Config.instance().setDummyProbability(param);
                break;
            case "P4":
                param = Config.instance().getLaundererPercentage() + currstep;
                Config.instance().setLaundererPercentage((int) param);
                break;
        }
        MyPlatformManager f = new MyPlatformManager(graph);
        f.register(this);
        file = f.getTransactionFile();
        f.exec();
    }

    /**
     * if GUI is enabled then graph and system.output are rendered in two frame
     */
    private void enableGUI(Network graph) {
        graph.display(true);

        JFrame myFrame = new JFrame("SystemMessages");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(700, 400);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        PrintStream printStream = new PrintStream(new MyOutputStream(textArea));
        System.setOut(printStream);
        System.setErr(printStream);
        myFrame.getContentPane().add(scroll);
        myFrame.setVisible(true);
    }

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
