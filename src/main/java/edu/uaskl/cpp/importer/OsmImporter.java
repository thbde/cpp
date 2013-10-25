package edu.uaskl.cpp.importer;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.uaskl.cpp.model.edge.EdgeCpp;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCpp;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OsmImporter {
	public class OsmNode {
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
			NodeCpp newNode = new NodeCpp(wayPoint.id);
			osmGraph.addNode(newNode);
		}
		
		//connect them
		Element documentElement = osmFile.getDocumentElement();
		NodeList ways = documentElement.getElementsByTagName("way");
		for(int i=0;i< ways.getLength();++i){
			NodeList childNodes = ways.item(i).getChildNodes();
			String lastWaypoint = null;
			for(int j=0;j<childNodes.getLength();++j){
				Node cNode =  childNodes.item(j);
				if(cNode.getNodeType() == Node.ELEMENT_NODE){
					Element childNode = (Element) cNode;
					if(lastWaypoint == null && childNode.getNodeName()=="nd"){
						lastWaypoint = childNode.getAttribute("ref");
						if(!osmNodes.containsKey(lastWaypoint)){
							lastWaypoint = null;
						}
					}
					else{
						if( childNode.getNodeName()=="nd"){
							String nodeId = childNode.getAttribute("ref");
							NodeCpp node = osmGraph.getNode(nodeId);
							if (!(node == null)){
								int distance = getDistance(osmNodes.get(lastWaypoint),osmNodes.get(childNode.getAttribute("ref")));
								osmGraph.getNode(lastWaypoint).connectWithNodeAndWeigth(node,distance);
								lastWaypoint = childNode.getAttribute("ref");
							}
						}
						// for non naive: check for roundabout
					}
				}
			}
		}
		
		return osmGraph;
	};
	
	protected GraphUndirected createFiltered(Document osmFile,HashMap<String,OsmNode> osmNodes){
		GraphUndirected osmGraph = new GraphUndirected();

		Element documentElement = osmFile.getDocumentElement();
		NodeList ways = documentElement.getElementsByTagName("way");
		for(int i=0;i< ways.getLength();++i){ //for each way
			NodeList childNodes = ways.item(i).getChildNodes();
			String lastWaypoint = null;
			List<String> metaIds = new LinkedList<String>();
			int distance = 0;
			String currentWaypoint = null;
			boolean roundabout = false;
			for(int j=0;j<childNodes.getLength();++j){ //go through the nodes
				Node cNode =  childNodes.item(j);
				if(cNode.getNodeType() == Node.ELEMENT_NODE){
					Element childNode = (Element) cNode;
					if(childNode.getNodeName()=="nd"){
						currentWaypoint = childNode.getAttribute("ref");
						if(osmNodes.containsKey(currentWaypoint)){
							if(!(lastWaypoint == null)){
								distance+=getDistance(osmNodes.get(lastWaypoint),osmNodes.get(currentWaypoint));
							}
							lastWaypoint = currentWaypoint;
							metaIds.add(currentWaypoint);
							
						}
					}
					else {
						if(childNode.getNodeName()=="tag"){
							if(childNode.hasAttribute("k") && childNode.getAttribute("k").equals("junction") && childNode.getAttribute("v").equals("roundabout")){
								roundabout = true;
								break;
							}
							
						}
					}
					
				}
			}
			if(roundabout){
				distance = 0;
			}
			for(int j =1;j<metaIds.size();++j) {
				String startNodeId = metaIds.get(j-1);
				String lastNodeId = metaIds.get(j);
				if(osmGraph.getNode(startNodeId)==null){
					NodeCpp newNode = new NodeCpp(startNodeId);
					osmGraph.addNode(newNode);
				}
				if(osmGraph.getNode(lastNodeId)==null){
					NodeCpp newNode = new NodeCpp(lastNodeId);
					osmGraph.addNode(newNode);
				}
				List<OsmNode> metaNodes = new LinkedList<OsmNode>();
				metaNodes.add(osmNodes.get(startNodeId));
				metaNodes.add(osmNodes.get(lastNodeId));
				osmGraph.getNode(startNodeId).connectWithNodeAndWeigth(osmGraph.getNode(lastNodeId), distance, metaNodes);
			}
		}
		//TODO simplify
		Iterator<NodeCpp> iteratorNodes = osmGraph.getNodes().values().iterator();
		while(iteratorNodes.hasNext()){
			NodeCpp node =iteratorNodes.next();
			if(node.getDegree()==2){
				String currentNodeId = node.getId();
				List<EdgeCpp> edges = node.getEdges();
				EdgeCpp edge1 = edges.get(0);
				EdgeCpp edge2 = edges.get(1);
				String node1id = edge1.getNode1().getId().equals(currentNodeId) ? edge1.getNode2().getId() : edge1.getNode1().getId(); 
				String node2id = edge2.getNode1().getId().equals(currentNodeId) ? edge2.getNode2().getId() : edge2.getNode1().getId(); 
				//concat the list in the right way
				List<OsmNode> newMetaNodes = edge1.getMetaNodes(), metaNodes2 = edge2.getMetaNodes();
				//newMetaNodes = metaNodes1.get(0).id==node1id ? metaNodes1 : Collections.reverse(metaNodes1);
				if (newMetaNodes.get(0).id.equals(currentNodeId)) {Collections.reverse(newMetaNodes);}
				if (!metaNodes2.get(0).id.equals(currentNodeId)) {Collections.reverse(metaNodes2);}
				newMetaNodes.addAll(metaNodes2);
				//add a new edge
				osmGraph.getNode(node1id).connectWithNodeAndWeigth(osmGraph.getNode(node2id), edge1.getWeight()+edge2.getWeight(), newMetaNodes);
				//remove the old node
				node.removeAllEdges();
				iteratorNodes.remove();
			}
		}
		
		
		
		return osmGraph;
	}
	
	public GraphUndirected importOsmUndirected(String filename) {
		Document osmFile = getDomFromFile(filename);
		HashMap<String,OsmNode> osmNodes = getOsmNodes(osmFile);
		GraphUndirected osmGraph = createFiltered(osmFile,osmNodes);
		
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
