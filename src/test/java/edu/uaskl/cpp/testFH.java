package edu.uaskl.cpp;


import static org.fest.assertions.api.Assertions.*;
import static edu.uaskl.cpp.importer.OsmImporter.importClean;
import static edu.uaskl.cpp.importer.OsmImporter.importZW;
import static edu.uaskl.cpp.importer.OsmImporter.importKL;
import static edu.uaskl.cpp.importer.OsmImporter.importFH;
import static edu.uaskl.cpp.importer.OsmImporter.importOsmUndirected;
import static edu.uaskl.cpp.model.exporter.Exporter.exportPathToHTML;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uaskl.cpp.model.path.PathExtended;

public class testFH {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void test() throws Exception {
//        final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importOsmUndirected(getClass().getResource("fh_way_no_meta.osm").toString());
//        final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importOsmUndirected(getClass().getResource("testDiamond.osm").toString());
        final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importZW();
    	assertTrue("FH graph should be connected", graph.getAlgorithms().isConnected());
        assertFalse("FH graph has no Euler circle", graph.getAlgorithms().hasEulerCircle());
        assertThat(graph.getNumberOfNodes()).isGreaterThan(0);
        graph.getAlgorithms().matchPerfect();
        assertTrue("matched graph", graph.getAlgorithms().hasEulerCircle());
        final NodeCppOSM start = graph.getNode(260070555l);//ZW
//        final NodeCppOSM start = graph.getNode(281170640l);//KL
        final PathExtended<NodeCppOSM> path = graph.getAlgorithms().getEulerianCircle(start);
        // TODO find the right folder for the real output
//        exportPathToHTML(path, folder.getRoot());
        exportPathToHTML(path, new File("./src/test/resources/edu/uaskl/cpp/exporter"));
        assertTrue(true);

    }

}
