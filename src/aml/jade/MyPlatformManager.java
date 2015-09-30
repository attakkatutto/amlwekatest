/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.jade;

import aml.agent.MyAgent;
import aml.agent.Receiver;
import aml.agent.Sender;
import aml.entity.SynthDB;
import aml.global.Config;
import aml.graph.Network;
import aml.graph.MyNode;
import aml.main.MyApplication;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Concrete subject of the JADE platform
 *
 * @author DAVIDE
 */
public class MyPlatformManager {

    protected AgentContainer mainContainer;
    protected Graph graph;
    protected long start;
    protected long end;
    protected SynthDB writer;
    protected MyApplication c;

    /**
     * Create new instance of MyPlatformManager
     *
     * @param graph instance of Network to manage
     */
    public MyPlatformManager(Graph graph) {
        this.graph = graph;
        this.writer = new SynthDB();
        this.start = System.currentTimeMillis();
        Runtime rt = Runtime.instance();
        rt.setCloseVM(true);
        // Get a hold on JADE runtime
        // Create a default profile
        Profile p = new ProfileImpl();
        // Create a new main container (i.e. on this host, port 1099)      
        rt.createMainContainer(p);
        mainContainer = rt.createAgentContainer(p);
        //initialize the platform handler
        initHandler();
    }

    /**
     * Custom listener of the platform for handle agents life
     */
    private void initHandler() {
        try {
            mainContainer.addPlatformListener(new MyPlatformListener(this));
        } catch (ControllerException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(MyPlatformManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Execute the JADE containers and starts all agents of the network
     */
    public void exec() {
        /*
         * List of the agents of the JADE container
         */
        List<MyAgent> agents = new ArrayList<>();
        /*
         * Create an agent for each node of the network and start it       
         */
        for (Node n : graph.getEachNode()) {
            MyAgent a = new MyAgent((MyNode) n, writer);
            try {
                mainContainer.acceptNewAgent(a.getId(), a).start();
                agents.add(a);
            } catch (StaleProxyException ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        /*
         * After all agents of the network starts in the JADE container
         * an agent can send a message to their neighbour nodes
         */
        for (MyAgent a : agents) {
            a.addBehaviour(new Sender(a, (MyNode) graph.getNode(a.getLocalName())));
            a.addBehaviour(new Receiver(a, (MyNode) graph.getNode(a.getLocalName())));
        }
    }

    /**
     * Stop the execution of the JADE platform
     */
    public void halt() {
        try {
            mainContainer.getPlatformController().kill();
            //mainContainer.kill();
        } catch (ControllerException ex) {
            Logger.getLogger(MyPlatformManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.writer.close();
        System.out.println(" - Exit..... ");
        this.end = System.currentTimeMillis();
        System.out.println(" - time elapsed (msec): " + (end - start));
        if (Config.instance().isGuiEnabled()) {
            JOptionPane.showMessageDialog(null, "Simulation finished!", "AML Ranking", JOptionPane.INFORMATION_MESSAGE);
        }   
        if (c != null) c.halt(); else System.exit(0);
    }

    /**
     * Register the MyApplication instance to notify when 
     * the platform is halt
     * @param c MyApplication to register
     */
    public void register(MyApplication c) {
        this.c = c;
    }

    /**
     * Return the name of transaction file
     * @return 
     */
    public File getTransactionFile() {
        return new File(this.writer.getTransactionFilename());
    }
}
