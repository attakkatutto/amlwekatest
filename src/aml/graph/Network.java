/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aml.graph;

import aml.global.Enums.*;
import aml.global.Config;
import aml.global.Enums;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.graph.EdgeFactory;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.NodeFactory;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.graph.implementations.AbstractGraph;

/**
 * Network of Nodes connected by Edges
 *
 * @author DAVIDE
 */
public final class Network extends SingleGraph {

    //Random number generator
    private final Random random;

    //**** Constructor
    /**
     * Create new instance of Network
     *
     * @param id identifier of the net
     * @param strictChecking
     * @param autoCreate auto create the network
     */
    public Network(String id, boolean strictChecking, boolean autoCreate) {
        super(id, strictChecking, autoCreate);
        random = new Random();
        if (Config.instance().isGuiEnabled()) {
            System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
            initStyle();
        }
        initFactories();
    }

    /**
     * Create new instance of Network
     *
     * @param id string network identifier
     */
    public Network(String id) {
        this(id, true, false);
    }

    /**
     * Initialize the factories of MyNode and MyEdge
     */
    private void initFactories() {
        setNodeFactory(new NodeFactory() {
            @Override
            public MyNode newInstance(String id1, Graph graph) {
                MyNode base;
                switch (random.nextInt(4)) {
                    case 0:
                        base = new MyNode((AbstractGraph) graph, id1, NodeType.EMPLOYEE);
                        return base;
                    case 1:
                        base = new MyNode((AbstractGraph) graph, id1, NodeType.SMALLCOMPANY);
                        return base;
                    case 2:
                        base = new MyNode((AbstractGraph) graph, id1, NodeType.FREELANCE);
                        return base;
                    case 3:
                        base = new MyNode((AbstractGraph) graph, id1, NodeType.BIGCOMPANY);
                        return base;
                    default:
                        base = new MyNode((AbstractGraph) graph, id1, NodeType.EMPLOYEE);
                        return base;
                }
            }
        });

        setEdgeFactory(new EdgeFactory() {
            @Override
            public MyEdge newInstance(String id1, Node src, Node dst, boolean directed) {
                return new MyEdge(id1, (MyNode) src, (MyNode) dst);
            }
        });

    }

    /**
     * Read the stylesheet from the css file in the resources
     *
     * @return @throws IOException
     */
    public String readStylesheet() throws IOException {
        File file = new File("." + File.separator + "res" + File.separator + "MyStyle.css");
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");
        try {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    /**
     * Initialize style of the graph if the render configuration option is
     * enabled
     */
    private void initStyle() {
        String ss;
        try {
            ss = readStylesheet();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            ss = "";
        }
        setAttribute("stylesheet", ss);
        addAttribute("ui.quality");
        addAttribute("ui.antialias");
    }

    /**
     * Start to build the network:
     * <ul>
     * <li>Generate the Scale Free Network</li>
     * <li>Initialize relations between nodes</li>
     * <li>Set the launderers and the honests of the network</li>
     * </ul>
     */
    public void build() {
        generateBarabasiNetwork();
        initRelations();
        setLaunderersAndHonests();
    }

    /**
     * Generate random Barabasi Network for the prototype
     */
    private void generateBarabasiNetwork() {
        BarabasiAlbertGenerator b = new BarabasiAlbertGenerator(Config.instance().getMaxEdgesPerStep(), false);
        b.setDirectedEdges(true, true);
        b.addSink(this);
        b.begin();
        while (getNodeCount() < Config.instance().getNumberOfNode()) {
            try {
                b.nextEvents();
                if (Config.instance().isGuiEnabled()) {
                    for (Node node : getNodeSet()) {
                        node.addAttribute("ui.label", String.format("%s", node.getId()));
                        if (((MyNode) node).getType() == Enums.NodeType.EMPLOYEE
                                || ((MyNode) node).getType() == Enums.NodeType.FREELANCE) {
                            node.addAttribute("ui.class", "person");
                        } else {
                            node.addAttribute("ui.class", "company");
                        }
                    }
                    Thread.sleep(50);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                Logger.getLogger(Network.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        b.end();
    }

    /**
     * Calculate scores of each node in the network
     */
    public void calculateScores() {
        for (Node n : getNodeSet()) {
            MyNode _mn = (MyNode) n;
        }
    }

    /**
     * Initialize relations between nodes
     */
    private void initRelations() {
        for (Node n : getEachNode()) {
            MyNode mn = (MyNode) n;
            mn.initParents();
            mn.initPartners();
            mn.initDummies();
        }
    }

    /**
     * Set the number of honests and launderers agents in the network
     */
    private void setLaunderersAndHonests() {
        int _numberLaunderer = (Config.instance().getNumberOfNode() * Config.instance().getLaundererPercentage()) / 100;
        int _countLaunderer = 0;
        int _index1 = 0;
        List<MyNode> _nodes = new ArrayList(getNodeSet());
        Collections.sort(_nodes);

        /*
         * while the current number of launderer is less then the max number
         * of launderer of the network 
         * get the _index1 node of the sorted collection
         */
        while (_countLaunderer < _numberLaunderer) {
            MyNode _n1 = _nodes.get(_index1);
            _n1.setHonest(false);
            _countLaunderer++;
            int _index2 = 0;
            int _index3 = 0;
            int _index4 = 0;
            /*
             * get the _index2 parent of the current node _n1 and set if it's launderer or not:
             * selecting a random number and verifing if it's less then the parent probability
             */
            while (_index2 < _n1.getParents().size() && _countLaunderer < _numberLaunderer) {
                double currprob = random.nextDouble();
                if (currprob <= Config.instance().getParentProbability()) {
                    MyNode _n2 = ((MyNode) getNode(_n1.getParents().get(_index2)));
                    _n2.setHonest(false);
                    _n1.addCountLaundererParents();
                    _countLaunderer++;
                }
                _index2++;
            }
            /*
             * get the _index3 partner of the current node _n1 and set if it's launderer or not:
             * selecting a random number and verifing if it's less then the partner probability
             */
            while (_index3 < _n1.getPartners().size() && _countLaunderer < _numberLaunderer) {
                double currprob = random.nextDouble();
                if (currprob <= Config.instance().getPartnerProbability()) {
                    MyNode _n3 = ((MyNode) getNode(_n1.getPartners().get(_index3)));
                    _n3.setHonest(false);
                    _n1.addCountLaundererPartners();
                    _countLaunderer++;
                }
                _index3++;
            }

            /*
             * get the _index4 dummy of the current node _n1 and set if it's launderer or not:
             * selecting a random number and verifing if it's less then the dummy probability
             */
            while (_index4 < _n1.getDummies().size() && _countLaunderer < _numberLaunderer) {
                double currprob = random.nextDouble();
                if (currprob <= Config.instance().getDummyProbability()) {
                    MyNode _n4 = ((MyNode) getNode(_n1.getDummies().get(_index4)));
                    _n4.setHonest(false);
                    _n1.addCountLaundererDummies();
                    _countLaunderer++;
                }
                _index4++;
            }
            _index1++;
        }
    }

}
