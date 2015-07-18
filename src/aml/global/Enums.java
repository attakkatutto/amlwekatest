/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.global;

import javax.xml.bind.annotation.XmlEnumValue;

/**
 * Enumerations
 *
 * @author ddefalco
 */
public class Enums {

    /**
     * Node types
     */
    public enum NodeType {

        EMPLOYEE,
        FREELANCE,
        SMALLCOMPANY,
        BIGCOMPANY
    }

    /**
     * Persistence mode of the writer
     */
    public enum PersistenceMode {

        @XmlEnumValue("1")
        FILE,
        @XmlEnumValue("2")
        DATABASE,
        @XmlEnumValue("3")
        ALL
    }

    /**
     * State of the agent
     */
    public enum MyAgentState {

        START,
        SEND_FINISH,
        RECEIVE_FINISH
    }

    /**
     * Temporal window for the scores
     */
    public enum WindowSize {

        @XmlEnumValue("2")
        BIMESTER(2),
        @XmlEnumValue("3")
        QUARTER(3),
        @XmlEnumValue("4")
        TRIMESTER(4),
        @XmlEnumValue("6")
        HALF(6),
        @XmlEnumValue("12")
        YEAR(12);
        
        private int value;

        public int getValue(){return value;}
        
        private WindowSize(int value) {
            this.value = value;
        }
    }
}
