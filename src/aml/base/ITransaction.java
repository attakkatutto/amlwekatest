/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.base;

import aml.global.Enums.*;

/**
 * Interface Transaction
 *
 * @author ddefalco
 */
public interface ITransaction {

    public String getId();

    public void setId(String id);

    public String getIdSourceAgent();

    public void setIdSourceAgent(String idSourceAgent);

    public String getIdTargetAgent();

    public void setIdTargetAgent(String idTargetAgent);

    public double getAmount();

    public void setAmount(double amount);

    public int getMonth();

    public void setMonth(short month);

    public short getYear();

    public void setYear(short year);

    public NodeType getSourceType();

    public void setSourceType(NodeType sourceType);

    public NodeType getTargetType();

    public void setTargetType(NodeType targetType);

    public String getHonest();

    public void setHonest(String honest);

    public double getZScoreHonest();

    public double getZScoreLaunderer();
}
