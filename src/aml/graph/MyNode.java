/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.graph;

import aml.global.Config;
import aml.global.Enums.*;
import aml.base.NodeBase;
import org.graphstream.graph.implementations.*;

/**
 * Node of the graph
 *
 * @author ddefalco
 */
public final class MyNode extends NodeBase {

    /**
     * Node of the network
     *
     * @param graph Network
     * @param id identifier of the Person
     * @param type type of the vertex
     */
    public MyNode(AbstractGraph graph, String id, NodeType type) {
        super(graph, id, type);
        this.fraudPotential = (Config.instance().getFraudPotential() == 0) ? 1 : Math.random();        
    }

    @Override
    public void initPartners() {
        int _count = 0;
        int _maxNumber = random.nextInt(Config.instance().getMaxNumberPartners());
        while (_count < _maxNumber) {
            String _id = String.valueOf(random.nextInt(graph.getNodeCount()));
            partners.add(_id);
            _count++;
        }
    }

    @Override
    public void initParents() {
        int _count = 0;
        int _maxNumber = random.nextInt(Config.instance().getMaxNumberParents());
        while (_count < _maxNumber) {
            MyNode v = graph.getNode(random.nextInt(graph.getNodeCount()));
            if (type == NodeType.EMPLOYEE || type == NodeType.FREELANCE) {
                parents.add(v.getId());
            }
            _count++;
        }
    }

    /**
     * The fraud potential of the current node.
     * If it's launderer, after a fixed number of fraud transactions, 
     * it begin to send honest transactions.
     * @return double fraud potential vaue
     */
    public double getFraudPotential() {
        return (isHonest()) ? 0 : fraudPotential;
    }

    @Override
    public void initDummies() {
        int _count = 0;
        int _maxNumber = random.nextInt(Config.instance().getMaxNumberDummies());
        while (_count < _maxNumber) {
            MyNode v = graph.getNode(random.nextInt(graph.getNodeCount()));
            dummies.add(v.getId());
            _count++;
        }
    }
}
