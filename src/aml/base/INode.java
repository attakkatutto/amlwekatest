/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.base;

/**
 * Interface Node
 *
 * @author ddefalco
 */
public interface INode {

    void initPartners();

    void initParents();

    void initDummies();

    double getRevenues(short month, short year);

    void setRevenues(double revenue, short month, short year);

    void setCosts(double cost, short month, short year);

    double getCosts(short month, short year); 
}
