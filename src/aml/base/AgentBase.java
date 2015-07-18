/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.base;

import aml.global.Enums.*;
import aml.graph.MyNode;
import jade.core.Agent;
import java.util.Random;

/**
 * Base class of custom JADE Agent
 *
 * @author Davide
 */
public abstract class AgentBase extends Agent {

    protected MyNode n;
    protected Random random = new Random();
    protected String id;

    public AgentBase(MyNode n) {
        super();
        this.n = n;
        this.id = n.getId();
    }

    /**
     * Type of the agent
     *
     * @return type of the agent
     */
    public String getId() {
        return id;
    }

    public abstract void initMessageNumber(NodeType type);
}
