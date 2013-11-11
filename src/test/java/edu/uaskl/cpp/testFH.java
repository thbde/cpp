package edu.uaskl.cpp;

import static edu.uaskl.cpp.importer.OsmImporter.importOsmUndirected;
import static edu.uaskl.cpp.model.exporter.Exporter.exportPathToHTML;
import static org.junit.Assert.*;

import java.util.ArrayList;

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
	public void test() {
		GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importOsmUndirected(getClass().getResource("fh_way_no_meta.osm").toString());
		assertTrue("FH graph should be connected",graph.getAlgorithms().isConnected());
		assertFalse("FH graph has no Euler circle",graph.getAlgorithms().hasEulerCircle());
		graph = graph.getAlgorithms().matchGraph(graph);
		assertTrue("matched graph",graph.getAlgorithms().hasEulerCircle());
		NodeCppOSM start = graph.getNode(260070555l);
		ArrayList<NodeCppOSM> circle = graph.getAlgorithms().getEulerianCircle(start);
		PathExtended<NodeCppOSM,EdgeCppOSM> path = new PathExtended<NodeCppOSM,EdgeCppOSM>(circle);
		//TODO find the right folder for the real output
		exportPathToHTML(path, folder.getRoot());
		assertTrue(true);
		
	}

}
