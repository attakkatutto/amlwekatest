/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.agent;

import aml.entity.Transaction;
import aml.global.Config;
import static aml.global.Constant.*;
import aml.global.Enums.*;
import aml.graph.MyNode;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.graph.Edge;

/**
 * This behaviour handle message for other agents
 *
 * @author Davide
 */
public class Sender extends SimpleBehaviour {

    MyNode n; /*Rapresent the node of the netowrk*/

    Random random = new Random(); /*Random class generate number*/

    int count = 0; /*Number of sent messages*/

    int countFraud = 0; /*Number od fraud messages for launderer agent*/

    boolean finished = false; /*Have you finish?*/


    /**
     * Create new instance of Sender agent behaviour
     * @param agent MyAgent behaviuor owner
     * @param n MyNode related to agent
     */
    public Sender(MyAgent agent, MyNode n) {
        super(agent);
        this.n = n;
        this.countFraud = (int) (n.getFraudPotential() * agent.getMESSAGE_NUMBER());
    }

    /**
     * Get the next Gaussian amount for this type of node
     */
    private double getRandomAmount(NodeType type) {
        double _amount;
        switch (type) {
            case EMPLOYEE:
                if (!n.isHonest() && count < countFraud) {
                    _amount = Config.instance().getEmployeeMeanLaunderer() + random.nextGaussian()
                            * Config.instance().getEmployeeStdDevLaunderer();
                    countFraud++;
                } else {
                    _amount = Config.instance().getEmployeeMeanHonest() + random.nextGaussian()
                            * Config.instance().getEmployeeStdDevHonest();
                }
                break;
            case FREELANCE:
                if (!n.isHonest() && count < countFraud) {
                    _amount = Config.instance().getFreelanceMeanLaunderer() + random.nextGaussian()
                            * Config.instance().getFreelanceStdDevLaunderer();
                    countFraud++;
                } else {
                    _amount = Config.instance().getFreelanceMeanHonest() + random.nextGaussian()
                            * Config.instance().getFreelanceStdDevHonest();
                }
                break;
            case BIGCOMPANY:
                if (!n.isHonest() && count < countFraud) {
                    _amount = Config.instance().getBigCompanyMeanLaunderer() + random.nextGaussian()
                            * Config.instance().getBigCompanyStdDevLaunderer();
                    countFraud++;
                } else {
                    _amount = Config.instance().getBigCompanyMeanHonest() + random.nextGaussian()
                            * Config.instance().getBigCompanyStdDevHonest();
                }
                break;
            case SMALLCOMPANY:
                if (!n.isHonest() && count < countFraud) {
                    _amount = Config.instance().getSmallCompanyMeanLaunderer() + random.nextGaussian()
                            * Config.instance().getSmallCompanyStdDevLaunderer();
                    countFraud++;
                } else {
                    _amount = Config.instance().getSmallCompanyMeanHonest() + random.nextGaussian()
                            * Config.instance().getSmallCompanyStdDevHonest();
                }
                break;
            default:
                if (!n.isHonest() && count < countFraud) {
                    _amount = Config.instance().getEmployeeMeanLaunderer() + random.nextGaussian()
                            * Config.instance().getEmployeeStdDevLaunderer();
                    countFraud++;
                } else {
                    _amount = Config.instance().getEmployeeMeanHonest() + random.nextGaussian()
                            * Config.instance().getEmployeeStdDevHonest();
                }
                break;
        }
        return _amount;
    }

    @Override
    public void action() {
        MyAgent base = (MyAgent) myAgent;
        if (n.getOutDegree() > 0 && count < base.getMESSAGE_NUMBER()) {
            try {
                ACLMessage msg = createSendMessage(n);
                base.send(msg);
                count++;
                block(100);
            } //this agent wait 100 ms before send a new message
            catch (Exception ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            ACLMessage msg = createFinishMessage();
            base.send(msg);
            finished = true;
            if (base.getCurrentState() == MyAgentState.RECEIVE_FINISH) {
                base.doDelete();
            } else {
                base.setState(MyAgentState.SEND_FINISH);
            }
        }
    }

    @Override
    public boolean done() {
        return finished;
    }

    /**
     *
     * Create finish message for neighbours nodes connected by entering or
     * leaving edges
     */
    private ACLMessage createFinishMessage() {
        ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
        msg.setContent("FINISH");
        for (Edge e : n.getEachLeavingEdge()) {
            MyNode v = e.getTargetNode();
            msg.addReceiver(new AID(v.getId(), AID.ISLOCALNAME));
        }
        for (Edge e : n.getEachEnteringEdge()) {
            MyNode v = e.getSourceNode();
            msg.addReceiver(new AID(v.getId(), AID.ISLOCALNAME));
        }
        return msg;
    }

    /**
     *
     * Create a new send message for the target node n connected by leaving edge
     */
    private ACLMessage createSendMessage(MyNode n) {
        //this agent send messages
        MyNode _v = n.getLeavingEdge(random.nextInt(n.getOutDegree())).getTargetNode();
        short _month = (short) random.nextInt(MONTHS);
        short _year = (short) (random.nextInt(Config.instance().getYearsNumber()) + START_YEAR);
        double _amount = getRandomAmount(n.getType());
        n.setCosts(_amount, _month, _year);
        String _honest = (n.isHonest()) ? "YES" : "NO";
        Transaction _t = new Transaction(n.getId() + "_" + _v.getId() + "_" + System.currentTimeMillis(),
                n.getId(), _v.getId(), n.getType(), _v.getType(), _amount, _month, _year, _honest);
        _t.setExistLaundererParents(n.getCountLaundererParents()>0);
        _t.setExistLaundererPartners(n.getCountLaundererPartners()>0);
        _t.setExistLaundererDummies(n.getCountLaundererDummies()>0);
        System.out.println(" - "
                + n.getId()
                + " send to "
                + _v.getId());
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        try {
            msg.addReceiver(new AID(_v.getId(), AID.ISLOCALNAME));
            msg.setContentObject(_t);//Content(" message from " + base.getLocalName() + " to " + base.getNeighbour(i));            
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, null, ex);
        }
        return msg;
    }

}
