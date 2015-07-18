/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.entity;

import aml.global.Config;
import aml.global.Enums.NodeType;
import java.io.Serializable;

/**
 * Class rapresents a bank transaction in the network
 *
 * @author ddefalco
 */
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String idSource;
    private String idTarget;
    private double amount;
    private short month;
    private short year;
    private NodeType sourceType;
    private NodeType targetType;
    private String honest;
    private boolean existLaundererParents, existLaundererPartners, existLaundererDummies;

    /**
     * Create new instance of Transaction
     */
    public Transaction() {
    }

    /**
     * Create new instance of Transaction
     *
     * @param id String transaction identifier
     */
    public Transaction(String id) {
        this.id = id;
    }

    /**
     * Create new instance of Transaction
     *
     * @param id transaction identifier
     * @param idSource source identifier
     * @param idTarget target identifier
     * @param sourceType type of source
     * @param targetType type of target
     * @param amount amount
     * @param month month
     * @param year year
     * @param honest source node honest true/false
     */
    public Transaction(String id, String idSource, String idTarget, NodeType sourceType, NodeType targetType, double amount, short month, short year, String honest) {
        this.id = id;
        this.idSource = idSource;
        this.idTarget = idTarget;
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.amount = amount;
        this.month = month;
        this.year = year;
        this.honest = honest;
    }

    /**
     * Identifier of the transaction
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Set identifier of transaction
     *
     * @param id string identifier of transaction
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Source of the transaction
     *
     * @return string source id
     */
    public String getIdSource() {
        return idSource;
    }

    /**
     * Set source identifier of transaction
     *
     * @param idSource string source identifier
     */
    public void setIdSource(String idSource) {
        this.idSource = idSource;
    }

    /**
     * Target of the transaction
     *
     * @return string target identifier
     */
    public String getIdTarget() {
        return idTarget;
    }

    /**
     * Set target identifier of transaction
     *
     * @param idTarget string target identifier
     */
    public void setIdTarget(String idTarget) {
        this.idTarget = idTarget;
    }

    /**
     * Amount of the transaction
     *
     * @return double amount of transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set the amount of the transaction
     *
     * @param amount double amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Month of the transaction
     *
     * @return
     */
    public short getMonth() {
        return month;
    }

    /**
     * Set month of transaction
     *
     * @param month short month of transaction
     */
    public void setMonth(short month) {
        this.month = month;
    }

    /**
     * Year of the transaction
     *
     * @return
     */
    public short getYear() {
        return year;
    }

    /**
     * Set year of transaction
     *
     * @param year short year of transaction
     */
    public void setYear(short year) {
        this.year = year;
    }

    /**
     * Category of source node of the transaction
     *
     * @return NodeType of source node
     */
    public NodeType getSourceType() {
        return sourceType;
    }

    /**
     * Set node type of transaction's source
     *
     * @param sourceType NodeType of source
     */
    public void setSourceType(NodeType sourceType) {
        this.sourceType = sourceType;
    }

    /**
     * Category of target agent of the transaction
     *
     * @return NodeType of target node
     */
    public NodeType getTargetType() {
        return targetType;
    }

    /**
     * Set node type of transaction's target
     *
     * @param targetType NodeType of target
     */
    public void setTargetType(NodeType targetType) {
        this.targetType = targetType;
    }

    /**
     * Is honest? YES or NO
     *
     * @return
     */
    public String getHonest() {
        return honest;
    }

    /**
     * Is source honest?
     *
     * @param honest string "YES"/"NO"
     */
    public void setHonest(String honest) {
        this.honest = honest;
    }

    /**
     * Z-Score for the honest node
     *
     * @return zScoreHonest value
     */
    public double getZScoreHonest() {
        double zScore = 0;
        switch (sourceType) {
            case EMPLOYEE:
                zScore = (amount - Config.instance().getEmployeeMeanHonest()) / Config.instance().getEmployeeStdDevHonest();
                break;
            case FREELANCE:
                zScore = (amount - Config.instance().getFreelanceMeanHonest()) / Config.instance().getFreelanceStdDevHonest();
                break;
            case SMALLCOMPANY:
                zScore = (amount - Config.instance().getSmallCompanyMeanHonest()) / Config.instance().getSmallCompanyStdDevHonest();
                break;
            case BIGCOMPANY:
                zScore = (amount - Config.instance().getBigCompanyMeanHonest()) / Config.instance().getBigCompanyStdDevHonest();
                break;
        }
        return zScore;
    }

    /**
     * Z-Score for the launderer node
     *
     * @return zScoreLaunderer value
     */
    public double getZScoreLaunderer() {
        double zScore = 0;
        switch (sourceType) {
            case EMPLOYEE:
                zScore = (amount - Config.instance().getEmployeeMeanLaunderer()) / Config.instance().getEmployeeStdDevLaunderer();
                break;
            case FREELANCE:
                zScore = (amount - Config.instance().getFreelanceMeanLaunderer()) / Config.instance().getFreelanceStdDevLaunderer();
                break;
            case SMALLCOMPANY:
                zScore = (amount - Config.instance().getSmallCompanyMeanLaunderer()) / Config.instance().getSmallCompanyStdDevLaunderer();
                break;
            case BIGCOMPANY:
                zScore = (amount - Config.instance().getBigCompanyMeanLaunderer()) / Config.instance().getBigCompanyStdDevLaunderer();
                break;
        }
        return zScore;
    }

    /**
     * Exist a launderer parent/s
     *
     * @return boolean true/false
     */
    public boolean getExistLaundererParents() {
        return existLaundererParents;
    }

    /**
     * Set if exists launderer parents
     *
     * @param existLaundererParents boolean true/false
     */
    public void setExistLaundererParents(boolean existLaundererParents) {
        this.existLaundererParents = existLaundererParents;
    }

    /**
     * Exist a launderer partner/s
     *
     * @return boolean true/false
     */
    public boolean getExistLaundererPartners() {
        return existLaundererPartners;
    }

    /**
     * Set if exists launderer partners
     *
     * @param existLaundererPartners boolean true/false
     */
    public void setExistLaundererPartners(boolean existLaundererPartners) {
        this.existLaundererPartners = existLaundererPartners;
    }

    /**
     * Exist a launderer dummy/s
     *
     * @return boolean true/false
     */
    public boolean getExistLaundererDummies() {
        return existLaundererDummies;
    }

    /**
     * Set if exists launderer dummies
     *
     * @param existLaundererDummies boolean true/false
     */
    public void setExistLaundererDummies(boolean existLaundererDummies) {
        this.existLaundererDummies = existLaundererDummies;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "aml.entity.Transaction[ id=" + id + " ]";
    }

}
