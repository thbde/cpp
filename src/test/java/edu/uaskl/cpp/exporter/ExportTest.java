package edu.uaskl.cpp.exporter;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static edu.uaskl.cpp.importer.OsmImporter.*;
import static edu.uaskl.cpp.model.exporter.Exporter.*;
import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uaskl.cpp.model.path.PathExtended;

public class ExportTest {
	@Rule 
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void test() {
		GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importOsmUndirected(getClass().getResource("../fh_way_no_meta.osm").toString());
		List<NodeCppOSM> nodes = new LinkedList<NodeCppOSM>();
		nodes.add(graph.getNode(279266215l));
		nodes.add(graph.getNode(279266248l));
		nodes.add(graph.getNode(279266252l));
		nodes.add(graph.getNode(279266215l));
		
		PathExtended<NodeCppOSM, EdgeCppOSM> path = new PathExtended<NodeCppOSM, EdgeCppOSM>(nodes);
		exportPathToHTML(path, folder.getRoot());
		assertTrue(true);
	}


}
