package edu.uaskl.cpp.model.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

import edu.uaskl.cpp.map.meta.WayNodeOSM;
import edu.uaskl.cpp.map.meta.WayOSM;
import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uaskl.cpp.model.node.NodeExtended;
import edu.uaskl.cpp.model.path.PathExtended;

public class Exporter {
	
	/**
	 * @param metaNodes list of waypoints
	 * @param id counting index
	 * @return the string containing the segment as a Javascipt array of [lat,lon] arrays
	 */
	private static String createSegment(List<WayNodeOSM>metaNodes,int id){
		final StringBuilder output= new StringBuilder();
		output.append("[[");
		output.append(metaNodes.get(0).getLatitude());
		output.append(",");
		output.append(metaNodes.get(0).getLongitude());
		output.append("]");
		for(int i = 1; i < metaNodes.size() ; ++i) {
			output.append(",[");
			output.append(metaNodes.get(i).getLatitude());
			output.append(",");
			output.append(metaNodes.get(i).getLongitude());
			output.append("]");
		}
		output.append("]");
		return output.toString();
	}
		
	/**
	 * Exports the needed overlay.js for index.html and others.
	 * The underlying graph must be reset before calling this function.
	 * @param path path to be exported to the map
	 * @param folder folder wherein to place overlay.js
	 */
	public static void exportPathToHTML(PathExtended<NodeCppOSM> path, File folder){
		final List<NodeCppOSM> nodes = path.getNodes();
		NodeCppOSM previousNode = nodes.get(0);
		NodeCppOSM currentNode;
		try (Writer fw = new FileWriter( new File(folder, "overlay.js" ));) {
			fw.write("var segments = [");;
			boolean first = true;
			for(int index = 1; index < nodes.size() ; ++index){
				// find a unvisited edge between previousNode and currentNode and then write the .js file
				currentNode = nodes.get(index);
				final EdgeCppOSM edge = getUnvisitedEdge(previousNode, currentNode);
				final List<WayNodeOSM> metaNodes = edge.getMetadata().getNodes();
				if ((metaNodes.get(0).getID()) == currentNode.getId()){
					Collections.reverse(metaNodes);
				}
				if(first){
					first = false;
				}
				else {
					fw.write(",");
				}
				fw.write(createSegment(metaNodes,index));
				fw.append(System.lineSeparator());
				previousNode = currentNode;
			}
			fw.write("];");
		} catch (IOException e) {
            System.out.println("could not open the file");
			System.exit(0);
		}
	}
	
	/**
	 * @param nodeFrom node from which to search
	 * @param nodeTo target node
	 * @return an unvisited from nodeFrom to nodeTo edge or null;
	 */
	private static EdgeCppOSM getUnvisitedEdge(NodeCppOSM nodeFrom, NodeCppOSM nodeTo) {
		long nodeToId = nodeTo.getId();
		long nodeFromId = nodeFrom.getId();
		for(EdgeCppOSM edge : nodeFrom.getEdges()) {
			// check both nodes due to loops
			if ((edge.getNode2().getId() == nodeToId && edge.getNode1().getId() == nodeFromId) || (edge.getNode1().getId() == nodeToId && edge.getNode2().getId() == nodeFromId)) {
				if (!edge.isVisited()){
					edge.setVisited();
					if(edge.getMetadata()==null){
						WayOSM metaData = getMissingMetadata(nodeFrom,nodeTo,edge.getWeight());
						edge.setMetadata(metaData);
					}
					return edge;
				}
			}
		}
		System.out.println("Error "+ nodeFrom.getName() + " "+nodeTo.getName());
		System.exit(0);
		return null;
	}
	
	private static WayOSM getMissingMetadata(NodeExtended<NodeCppOSM,EdgeCppOSM> node1,NodeExtended<NodeCppOSM,EdgeCppOSM> node2, double weight) {
		long nodeFromId = node1.getId();
		long nodeToId = node2.getId();
		for(EdgeCppOSM edge : node1.getEdges()) {
			if(edge.getWeight() == weight && (edge.getMetadata() != null) &&(edge.getNode2().getId() == nodeToId && edge.getNode1().getId() == nodeFromId) || (edge.getNode1().getId() == nodeToId && edge.getNode2().getId() == nodeFromId)) {
				return edge.getMetadata();
			}
		}
		return null;
	}
}
