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
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author ddefalco
 */
public class MyApplication {
    int COUNTER = 0;
    int MAX_COUNT = 50;
    double STEP = 0.01;
    protected File file;
    
    public MyApplication(){}
          
    public void exec(){
        Network graph = new Network("AML Synthetic DB");
        if (Config.instance().isGuiEnabled()) {
            enableGUI(graph);
        }
        graph.build();
        double p1 = Config.instance().getParentProbability() + STEP;
        Config.instance().setParentProbability(p1);
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
        weka.test("Parent fraud probability", Config.instance().getParentProbability());
        if(COUNTER < MAX_COUNT) {
            COUNTER++;
            exec();
        }
        else
        {
            System.exit(0);
        }
    }
}
