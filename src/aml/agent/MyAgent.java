/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.agent;

import aml.base.AgentBase;
import aml.entity.SynthDB;
import aml.entity.Transaction;
import aml.global.Config;
import aml.global.Enums.*;
import aml.graph.MyNode;

/**
 * Custom JADE Agent
 *
 * @author DAVIDE
 */
public final class MyAgent extends AgentBase {

    protected int END;
    protected MyAgentState state;
    protected int MESSAGE_NUMBER;
    protected SynthDB writer;

    /**
     * Constructor of the agent
     *
     * @param node every agent is related with a node in the network
     * @param writer
     */
    public MyAgent(MyNode node, SynthDB writer) {
        super(node);
        this.writer = writer;
        this.state = MyAgentState.START;
    }

    @Override
    public void setup() {
        initMessageNumber(n.getType());
        writer.writeEntity(n);
    }

    /**
     * Initialize the number of messages for each agent
     *
     * @param type Type of node
     */
    @Override
    public void initMessageNumber(NodeType type) {
        int _messagenumber;
        switch (type) {
            case EMPLOYEE:
                _messagenumber = (int) (Config.instance().getEmployeeMessageMean() + random.nextGaussian() * Config.instance().getEmployeeMessageStdDev());
                break;
            case FREELANCE:
                _messagenumber = (int) (Config.instance().getFreelanceMessageMean() + random.nextGaussian() * Config.instance().getFreelanceMessageStdDev());
                break;
            case BIGCOMPANY:
                _messagenumber = (int) (Config.instance().getSmallCompanyMessageMean() + random.nextGaussian() * Config.instance().getSmallCompanyMessageStdDev());
                break;
            case SMALLCOMPANY:
                _messagenumber = (int) (Config.instance().getBigCompanyMessageMean() + random.nextGaussian() * Config.instance().getBigCompanyMessageStdDev());
                break;
            default:
                _messagenumber = (int) (Config.instance().getEmployeeMessageMean() + random.nextGaussian() * Config.instance().getEmployeeMessageStdDev());
                break;
        }
        this.MESSAGE_NUMBER = _messagenumber;
    }

    /**
     * Count the finish message received from this agent
     *
     * @return END: number of finish message received
     */
    public int getEND() {
        return END;
    }

    public int addEND() {
        return END++;
    }

    /**
     * Rapresent the current state of the agent: - START the agent is started -
     * SEND_FINISH the agent has been sent all messages - RECEIVE_FINISH the
     * agent has been received all messages
     *
     * @return MyAgentState current state of the agent
     */
    public MyAgentState getCurrentState() {
        return state;
    }

    public void setState(MyAgentState state) {
        this.state = state;
    }

    public int getMESSAGE_NUMBER() {
        return this.MESSAGE_NUMBER;
    }
    
    public void writeReceived(Transaction t){
        this.writer.writeTransaction(t);
    }
}
