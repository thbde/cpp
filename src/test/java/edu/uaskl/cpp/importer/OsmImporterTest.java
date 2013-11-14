package edu.uaskl.cpp.importer;

import java.util.HashMap;

import static org.fest.assertions.api.Assertions.*;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import static edu.uaskl.cpp.importer.OsmImporter.*;
import static org.junit.Assert.*;
import edu.uaskl.cpp.map.meta.WayNodeOSM;
import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.graph.*;
import edu.uaskl.cpp.model.node.NodeCppOSM;

public class OsmImporterTest {

    @Test
    public void testImportOsmUndirected() {
    	final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importOsmUndirected("target/test-classes/edu/uaskl/cpp/fh_way_no_meta.osm");
//    	final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importOsmUndirected(getClass().getResource("../fh_way_no_meta.osm").toString());
        assertNotEquals("Graph should have nodes", graph.getNumberOfNodes(), 0); // TODO use assertThat -tbach
        assertNotEquals("Graph should have edges", graph.getGetNumberOfEdges(), 0);
        // System.out.println(graph.toString());
        // System.out.println(graph.getStatistics());
    }

    @Test
    public void testImportLine() {
        // GraphUndirected graph = testImporter.importOsmUndirected("zweibruecken_way_no_meta.osm");
    	final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importOsmUndirected("target/test-classes/edu/uaskl/cpp/testLine.osm");
//        final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importOsmUndirected(getClass().getResource("../testLine.osm").toString());
        assertEquals("Graph should have 2 nodes", 2, graph.getNumberOfNodes()); // TODO use assertThat -tbach
        assertEquals("Graph should have 1 edge", 1, graph.getGetNumberOfEdges());
        final NodeCppOSM node1 = graph.getNode(260070555l);
        final EdgeCppOSM edge1 = node1.getEdges().get(0);
        assertEquals("there should be 6 meta nodes", 6, edge1.getMetadata().getNodes().size());
    }

    @Test
    public void testImportCircle() {
        // GraphUndirected graph = testImporter.importOsmUndirected("zweibruecken_way_no_meta.osm");
    	final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importOsmUndirected("target/test-classes/edu/uaskl/cpp/testCircle.osm");
//        final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importOsmUndirected(getClass().getResource("../testCircle.osm").toString());
        assertEquals("Graph should have 1 nodes", 1, graph.getNumberOfNodes()); // TODO use assertThat -tbach
        assertEquals("Graph should have 1 edge", 1, graph.getGetNumberOfEdges());
    }
}
