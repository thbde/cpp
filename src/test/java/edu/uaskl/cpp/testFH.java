package edu.uaskl.cpp;


import static org.fest.assertions.api.Assertions.*;
import static edu.uaskl.cpp.importer.OsmImporter.importClean;
import static edu.uaskl.cpp.importer.OsmImporter.importZW;
import static edu.uaskl.cpp.importer.OsmImporter.importZWUBZ;
import static edu.uaskl.cpp.importer.OsmImporter.importZWDirected;
import static edu.uaskl.cpp.importer.OsmImporter.importKL;
import static edu.uaskl.cpp.importer.OsmImporter.importKLDirected;
import static edu.uaskl.cpp.importer.OsmImporter.importFH;
import static edu.uaskl.cpp.importer.OsmImporter.importFHDirected;
import static edu.uaskl.cpp.importer.OsmImporter.importOsmUndirected;
import static edu.uaskl.cpp.model.exporter.Exporter.exportPathToHTML;
import static edu.uaskl.cpp.model.exporter.Exporter.exportPathToHTMLDirected;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.edge.EdgeCppOSMDirected;
import edu.uaskl.cpp.model.graph.GraphDirected;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uaskl.cpp.model.node.NodeCppOSMDirected;
import edu.uaskl.cpp.model.path.PathExtended;

public class testFH {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void test() throws Exception {
        final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importZW();
//        final GraphDirected<NodeCppOSMDirected, EdgeCppOSMDirected> graph = importZWDirected();
        System.out.println("imported");
//        for(NodeCppOSMDirected node1 : graph.getNodes()){
//        	System.out.println(node1.getName());
//        }
    	assertTrue("FH graph should be connected", graph.getAlgorithms().isConnected());
//        assertFalse("FH graph has no Euler circle", graph.getAlgorithms().hasEulerCircle());
//    	NodeCppOSMDirected node = graph.getNode(281678042L);
        assertThat(graph.getNumberOfNodes()).isGreaterThan(0);
        graph.getAlgorithms().matchPerfect();
        System.out.println("matched");
        
//		  <node id="298114929" version="-1" timestamp="1969-12-31T23:59:59Z" changeset="-1" lat="49.2340166" lon="7.4021832"/>
//        <node id="260070509" version="-1" timestamp="1969-12-31T23:59:59Z" changeset="-1" lat="49.2538969" lon="7.3649591"/>
        
//        assertTrue("matched graph", graph.getAlgorithms().hasEulerCircle());
//        final PathExtended<NodeCppOSMDirected> path = graph.getAlgorithms().getEulerianCircle();
        final PathExtended<NodeCppOSM> path = graph.getAlgorithms().getEulerianCircle();
        graph.resetStates();
//        final PathExtended<NodeCppOSM> path = new PathExtended<NodeCppOSM>(graph.getAlgorithms().shortestPath(graph.getNode(267970528l),graph.getNode(330044512l)));

//        final PathExtended<NodeCppOSM> path = graph.getAlgorithms().getShortestPathAStar(graph.getNode(267970528l),graph.getNode(330044512l));
        
        // TODO find the right folder for the real output
//        exportPathToHTML(path, folder.getRoot());
        graph.resetStates();
//        exportPathToHTMLDirected(path, new File("./src/test/resources/edu/uaskl/cpp/exporter"));
        exportPathToHTML(path, new File("./src/test/resources/edu/uaskl/cpp/exporter"));
        assertTrue(true);

    }

}
