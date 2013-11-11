package edu.uaskl.cpp.importer;



import java.util.HashMap;

import static org.fest.assertions.api.Assertions.*;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import static edu.uaskl.cpp.importer.OsmImporter.*;
import static org.junit.Assert.*;
import edu.uaskl.cpp.map.meta.WayNodeOSM;
import edu.uaskl.cpp.model.edge.EdgeOSM;
import edu.uaskl.cpp.model.graph.*;
import edu.uaskl.cpp.model.node.NodeOSM;

public class OsmImporterTest {

	@Test
	public void testGetDom() {
		Document dom = getDomFromFile(getClass().getResource("../fh_way_no_meta.osm").toString());
		NodeList testnodes = dom.getDocumentElement().getElementsByTagName("node");
		assertThat(dom).isNotNull();
		assertThat(testnodes.getLength()).isNotEqualTo(0);
		assertThat(dom.getElementsByTagName("node")).isNotNull();
	}
	
	@Test
	public void testNanoSec(){
		long lat = get100NanoDegrees("49.2572968");
		long lon = get100NanoDegrees("49.259868");
		assertEquals("nanoseconds parser does not work",lat, 492572968l);
		assertEquals("nanoseconds parser does not work",lon , 492598680l);
	}
	
	@Test
	public void testGetOsmNodes(){
		Document dom = getDomFromFile(getClass().getResource("../fh_way_no_meta.osm").toString());
		HashMap<Long, WayNodeOSM> map = getOsmNodes(dom);
		assertFalse("Node map should not be empty",map.isEmpty());
	}

	@Test
	public void testImportOsmUndirected(){
		GraphUndirected<NodeOSM, EdgeOSM> graph = importOsmUndirected(getClass().getResource("../zweibruecken_way_no_meta.osm").toString());
		//GraphUndirected<NodeOSM, EdgeOSM> graph = importOsmUndirected(getClass().getResource("../fh_way_no_meta.osm").toString());
		assertNotEquals("Graph should have nodes",graph.getNumberOfNodes(),0);
		assertNotEquals("Graph should have edges",graph.getGetNumberOfEdges(),0);
		//System.out.println(graph.toString());
		//System.out.println(graph.getStatistics());
	}
	
	@Test
	public void testImportLine(){
		//GraphUndirected graph = testImporter.importOsmUndirected("zweibruecken_way_no_meta.osm");
		GraphUndirected<NodeOSM, EdgeOSM> graph = importOsmUndirected(getClass().getResource("../testLine.osm").toString());
		assertEquals("Graph should have 2 nodes",2,graph.getNumberOfNodes());
		assertEquals("Graph should have 1 edge",1,graph.getGetNumberOfEdges());
		NodeOSM node1 = graph.getNode(260070555l);
		EdgeOSM edge1 = node1.getEdges().get(0);
		assertEquals("there should be 6 meta nodes",6,edge1.getMetadata().getNodes().size());
	}
	
	@Test
	public void testImportCircle(){
		//GraphUndirected graph = testImporter.importOsmUndirected("zweibruecken_way_no_meta.osm");
		GraphUndirected<NodeOSM, EdgeOSM> graph = importOsmUndirected(getClass().getResource("../testCircle.osm").toString());
		assertEquals("Graph should have 1 nodes",1,graph.getNumberOfNodes());
		assertEquals("Graph should have 1 edge",1,graph.getGetNumberOfEdges());
	}
}
