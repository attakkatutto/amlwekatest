/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.base;

import aml.entity.Transaction;
import aml.global.Config;
import static aml.global.Constant.*;
import aml.global.Enums.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.graphstream.graph.implementations.AbstractGraph;
import org.graphstream.graph.implementations.AdjacencyListNode;

/**
 * Abstract base class of EntityBase
 *
 * @author ddefalco
 */
public abstract class NodeBase extends AdjacencyListNode implements INode, Comparable {

    protected NodeType type;
    protected boolean honest = true;

    protected double fraudPotential;

    //Random List of busness partners,parents,dummies 
    protected ArrayList<String> partners, parents, dummies;

    //Scores of current node
    protected double[] suspectScore, fraudScore, deficitScore;

    //Transactions with other nodes with a relation 
    //parent/partner/dummy
    protected ArrayList<Transaction> relationshipTransactions;
    
    //Random generator
    protected Random random = new Random();

    //Costs and revenues of current node
    protected Map<Short, double[]> revenues, costs;

    //Counterer of the launderers of this node
    protected int countLaundererParents, countLaundererPartners, countLaundererDummies;

    // *** Constructor ***
    /**
     * Create new instance of NodeBase of the network
     *
     * @param graph rapresent the current network
     * @param id identifier of the node
     * @param type type of node NodeType enumeration
     */
    public NodeBase(AbstractGraph graph, String id, NodeType type) {
        super(graph, id);
        this.type = type;
        this.partners = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.dummies = new ArrayList<>();
        this.relationshipTransactions = new ArrayList<>();
        initMaps();
    }

    /**
     * Initialize the Maps that contains revenues and costs one item represents
     * one year with its 12 months
     */
    private void initMaps() {
        this.revenues = new HashMap<>();
        this.costs = new HashMap<>();
        for (int index = 0; index < Config.instance().getYearsNumber(); index++) {
            revenues.put((short) (START_YEAR + index), new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
            costs.put((short) (START_YEAR + index), new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        }
    }

    /**
     * Collection of the partners of the node
     *
     * @return ArrayList of partners
     */
    public ArrayList<String> getPartners() {
        return partners;
    }

    /**
     * Collection of the parents of the node
     *
     * @return ArrayList of parents
     */
    public ArrayList<String> getParents() {
        return parents;
    }

    /**
     * Collection of the dummies of the node
     *
     * @return ArrayList of dummies
     */
    public ArrayList<String> getDummies() {
        return dummies;
    }

    @Override
    public void setIndex(int index) {
        super.setIndex(index);
    }

    /**
     * Type of node
     *
     * @return NodeType enum of node
     */
    public NodeType getType() {
        return type;
    }

    /**
     * Set the type of the node
     *
     * @param type NodeType to set
     */
    public void setType(NodeType type) {
        this.type = type;
    }

    /**
     * Get revenues of the EntityBase
     *
     * @param month of the revenue
     * @param year of the revenue
     * @return revenues
     */
    @Override
    public double getRevenues(short month, short year) {
        double[] _tmp = revenues.get(year);
        return _tmp[month];
    }

    /**
     * Set revenue of the EntityBase and increment total revenues
     *
     * @param revenue of the EntityBase
     * @param month of the revenue
     * @param year of the revenue
     */
    @Override
    public void setRevenues(double revenue, short month, short year) {
        double[] _tmp = revenues.get(year);
        _tmp[month] += revenue;        
    }

    /**
     * Set cost of the EntityBase and increment total costs
     *
     * @param cost of the EntityBase
     * @param month of the cost
     * @param year of the cost
     */
    @Override
    public void setCosts(double cost, short month, short year) {
        double[] _tmp = costs.get(year);
        _tmp[month] += cost;       
    }

    /**
     * Get costs of the EntityBase
     *
     * @param month of the cost
     * @param year of the cost
     * @return costs
     */
    @Override
    public double getCosts(short month, short year) {
        double[] _tmp = costs.get(year);
        return _tmp[month];
    }

    /**
     * Are you honest?
     *
     * @return true/false calcolo onesto o disonesto sulla base degli archi
     * uscenti in modo da ottenere disonesti per il 5% (xml) del totale dei
     * nodi. media e deviazione std per disonesti (quella che è config nel xml
     * rimane così ed è quella degli onesti)
     *
     */
    public boolean isHonest() {
        return honest;
    }

    /**
     * Set if the node is honest or not
     *
     * @param honest boolean true/false
     */
    public void setHonest(boolean honest) {
        if (Config.instance().isGuiEnabled() && !honest) {
            addAttribute("ui.style", "fill-color: red;");
        }
        this.honest = honest;
    }

    /**
     * Are you dummy?
     *
     * @param id
     * @return true/false
     */
    public boolean isDummy(String id) {
        return dummies.contains(id);
    }

    /**
     * Suspect score of the node
     *
     * @param time int window time position
     * @return double suspect score
     */
    public double getSuspectScore(int time) {
        return suspectScore[time];
    }

    /**
     * Set the suspect score of this node
     *
     * @param suspectScore double score to set
     * @param time int window time position
     */
    public void setSuspectScore(double suspectScore, int time) {
        this.suspectScore[time] += suspectScore;
    }

    /**
     * Fraud score of the node
     *
     * @param time int window time position
     * @return double fraud score
     */
    public double getFraudScore(int time) {
        return fraudScore[time];
    }

    /**
     * Set the fraud score of this node
     *
     * @param fraudScore double score to set
     * @param time int window time position
     */
    public void setFraudScore(double fraudScore, int time) {
        this.fraudScore[time] = fraudScore;
    }

    /**
     * Deficit score of the node
     *
     * @param time int window time position
     * @return double deficit score
     */
    public double getDeficitScore(int time) {
        return deficitScore[time];
    }

    /**
     * Set the deficit score of this node
     *
     * @param deficitScore double score to set
     * @param time int window time position
     */
    public void setDeficitScore(double deficitScore, int time) {
        this.deficitScore[time] = deficitScore;
    }

    /**
     * Initialize the parents of the current node PERSON
     */
    @Override
    public abstract void initParents();

    /**
     * Initialize the partners of the current node
     */
    @Override
    public abstract void initPartners();

    /**
     * Counter of launderer parents
     *
     * @return int counter
     */
    public int getCountLaundererParents() {
        return countLaundererParents;
    }

    /**
     * Counter of launderer partners
     *
     * @return int counter
     */
    public int getCountLaundererPartners() {
        return countLaundererPartners;
    }

    /**
     * Counter of launderer dummies
     *
     * @return int counter
     */
    public int getCountLaundererDummies() {
        return countLaundererDummies;
    }

    /**
     * Increment the launderer parents counter
     */
    public void addCountLaundererParents() {
        countLaundererParents++;
    }

    /**
     * Increment the launderer partners counter
     */
    public void addCountLaundererPartners() {
        countLaundererPartners++;
    }

    /**
     * Increment the launderer dummies counter
     */
    public void addCountLaundererDummies() {
        countLaundererDummies++;
    }

    /**
     * Initialize the dummies of the current node PERSON
     */
    @Override
    public abstract void initDummies();

    @Override
    public int compareTo(Object o) {
        if (this.getOutDegree() < ((NodeBase) o).getOutDegree()) {
            return 1;
        }
        if (this.getOutDegree() > ((NodeBase) o).getOutDegree()) {
            return -1;
        }
        return 0;
    }
}
