package edu.uaskl.cpp.importer;



import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import edu.uaskl.cpp.importer.OsmImporter.OsmNode;
import edu.uaskl.cpp.importer.OsmImporter;
import static org.junit.Assert.*;
import edu.uaskl.cpp.model.graph.*;

public class OsmImporterTest {

	@Test
	public void testGetDom() {
		OsmImporter testImporter = new OsmImporter();
		Document dom = testImporter.getDomFromFile("fh_way_no_meta.osm");
		NodeList testnodes = dom.getDocumentElement().getElementsByTagName("node");
		assert(dom != null);
		assert(testnodes.getLength()!=0);
		assert(dom.getElementsByTagName("node") != null);
	}
	
	@Test
	public void testNanoSec(){
		OsmImporter testImporter = new OsmImporter();
		long lat = testImporter.get100NanoDegrees("49.2572968");
		long lon = testImporter.get100NanoDegrees("49.259868");
		assertEquals("nanoseconds parser does not work",lat, 492572968l);
		assertEquals("nanoseconds parser does not work",lon , 492598680l);
	}
	
	@Test
	public void testGetOsmNodes(){
		OsmImporter testImporter = new OsmImporter();
		Document dom = testImporter.getDomFromFile("fh_way_no_meta.osm");
		HashMap<String, OsmNode> map = testImporter.getOsmNodes(dom);
		assertFalse("Node map should not be empty",map.isEmpty());
	}

	@Test
	public void testImportOsmUndirected(){
		OsmImporter testImporter = new OsmImporter();
		//GraphUndirected graph = testImporter.importOsmUndirected("zweibruecken_way_no_meta.osm");
		GraphUndirected graph = testImporter.importOsmUndirected("fh_way_no_meta.osm");
		assertNotEquals("Graph should have nodes",graph.getNumberOfNodes(),0);
		assertNotEquals("Graph should have edges",graph.getGetNumberOfEdges(),0);
		//System.out.println(graph.toString());
		//System.out.println(graph.getStatistics());
	}
}
