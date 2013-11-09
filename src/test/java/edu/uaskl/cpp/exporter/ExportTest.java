package edu.uaskl.cpp.exporter;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static edu.uaskl.cpp.importer.OsmImporter.*;
import static edu.uaskl.cpp.model.exporter.Exporter.*;
import edu.uaskl.cpp.model.edge.EdgeOSM;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeOSM;
import edu.uaskl.cpp.model.path.PathExtended;

public class ExportTest {
	@Rule 
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void test() {
		GraphUndirected<NodeOSM, EdgeOSM> graph = importOsmUndirected(getClass().getResource("../zweibruecken_way_no_meta.osm").toString());
		List<NodeOSM> nodes = new LinkedList<NodeOSM>();
		nodes.add(graph.getNode("279266215"));
		nodes.add(graph.getNode("279266248"));
		nodes.add(graph.getNode("279266252"));
		nodes.add(graph.getNode("279266215"));
		
		PathExtended<NodeOSM, EdgeOSM> path = new PathExtended<NodeOSM, EdgeOSM>(nodes);
		exportPathToHTML(path, folder.getRoot());
		assertTrue(true);
	}


}
