package edu.uaskl.cpp.importer;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCpp;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OsmImporter {
	protected class OsmNode {
		public long lat=0;
		public long lon=0;
		public String id="";
		public OsmNode(String id,long lat, long lon){
			this.id  = id;
			this.lat = lat;
			this.lon = lon;
		};
	}
	
	protected int getDistance(OsmNode a, OsmNode b){
		long diffLat = a.lat - b.lat;
		long diffLon = a.lon - b.lon;
		return (int) Math.sqrt(diffLat*diffLat+diffLon*diffLon);
	}
	
	protected Document getDomFromFile(String filename) {
		DocumentBuilderFactory factory =   DocumentBuilderFactory.newInstance();
		Document document = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse( new File(filename) );
			document.getDocumentElement().normalize();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.out.println("could not open the file");
			e.printStackTrace();
			System.exit(0);
		}
		return document;
	}
	
	protected long get100NanoDegrees(String parsed){
		long value = 1;
		// different amount of decimal places (up to 7)
		String decimalPlaces = parsed.split("\\.")[1];
		for(int i=0;i<(7-decimalPlaces.length());++i){
			value *=10;
		}
		parsed = parsed.replace(".", "");
		value *= Long.parseLong(parsed, 10);
		
		return value;
	}
	
	protected HashMap<String, OsmNode> getOsmNodes(Document dom){
		HashMap<String, OsmNode> osmNodes = new HashMap<String, OsmNode> ();
		Element documentElement = dom.getDocumentElement();
		NodeList nodes = documentElement.getElementsByTagName("node");
		for (int i = 0; i< nodes.getLength();++i){
			Element node = (Element) nodes.item(i);
			String id = node.getAttribute("id");
			long lat = get100NanoDegrees(node.getAttribute("lat"));
			long lon = get100NanoDegrees(node.getAttribute("lon"));
			OsmNode osmNode = new OsmNode(id,lat,lon);
			osmNodes.put(id, osmNode);
		}
		return osmNodes;
	}
	
	protected GraphUndirected createNaive(Document osmFile,HashMap<String,OsmNode> osmNodes){
		// add all waypoints to the graph
		GraphUndirected osmGraph = new GraphUndirected();
		Collection<OsmNode> wayPoints = osmNodes.values();
		for(Iterator<OsmNode> wayPointsIterator = wayPoints.iterator();wayPointsIterator.hasNext();){
			OsmNode wayPoint = wayPointsIterator.next();
			NodeCpp newNode = new NodeCpp(wayPoint.id,wayPoint.lat,wayPoint.lon);
			osmGraph.addNode(newNode);
		}
		
		//connect them
		Element documentElement = osmFile.getDocumentElement();
		NodeList ways = documentElement.getElementsByTagName("way");
		
		for(int i=0;i< ways.getLength();++i){
			NodeList childNodes = ways.item(i).getChildNodes();
			String lastWaypoint = null;
			for(int j=0;j<childNodes.getLength();++j){
				Element childNode = (Element) childNodes.item(j);
				if(lastWaypoint == null && childNode.getNodeName()=="nd"){
					lastWaypoint = childNode.getAttribute("ref");
				}
				else{
					if( childNode.getNodeName()=="nd"){
						int distance = getDistance(osmNodes.get(lastWaypoint),osmNodes.get(childNode.getAttribute("ref")));
						
						nodea.connectWithNodeAndWeigth(nodeb,distance);
						
						
						//add edge
					}
					// for non naive: check for roundabout
				}
			}
		}
		
		return osmGraph;
	};
	
	GraphUndirected importOsmUndirected(String filename) {
		Document osmFile = getDomFromFile(filename);
		HashMap<String,OsmNode> osmNodes = getOsmNodes(osmFile);
		GraphUndirected osmGraph = createNaive(osmFile,osmNodes);
		
		/**
		 * TODO create edges
		 * for each way-element:
		 * 		create the nodes and connect them but not for roundabout
		 * for each way which is a roundabout:
		 * 		find the existing nodes and connect them
		 */
		
		
		return osmGraph;
	};
	
	
	
}
