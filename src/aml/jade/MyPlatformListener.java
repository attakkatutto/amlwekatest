/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.jade;

import jade.wrapper.PlatformController;
import jade.wrapper.PlatformEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom JADE platform listener implements interface PlatformController.Listener
 * 
 * The collection agents is synchronized because the handler of bornAgent and deadAgent
 * are not thread-safe
 * 
 * @author ddefalco
 */
public class MyPlatformListener
        implements PlatformController.Listener {

    /**
     * Create new instance of MyPlatformListener
     * @param subject 
     */
    public MyPlatformListener(MyPlatformManager subject) {
        this.subject = subject;
    }

    MyPlatformManager subject; /* Instance of MyPlatformManager to be notified when all agents are dead */

    List<String> agents = Collections.synchronizedList(new ArrayList<String>()); /* Synchronized collection of agents alive*/

    @Override
    public void deadAgent(PlatformEvent anEvent) {
        // invoked when an agent is born
        String name = anEvent.getAgentGUID();
        System.out.println(" - "
                + name
                + " dead ");
        agents.remove(name);
        if (agents.isEmpty()) {
            System.out.println(" - "
                    + " JADE end! ");
            subject.halt();
        }
    }

    @Override
    public void bornAgent(PlatformEvent anEvent) {
        // invoked when an agent is dead
        String name = anEvent.getAgentGUID();
        System.out.println(" - "
                + name
                + " born ");
        agents.add(name);
    }

    @Override
    public void startedPlatform(PlatformEvent pe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void suspendedPlatform(PlatformEvent pe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void resumedPlatform(PlatformEvent pe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void killedPlatform(PlatformEvent pe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
