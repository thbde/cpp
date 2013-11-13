package edu.uaskl.cpp.model.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.uaskl.cpp.map.meta.WayNodeOSM;
import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uaskl.cpp.model.path.PathExtended;

public class Exporter {
	
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
	
	
	public static void exportPathToHTML(PathExtended<NodeCppOSM> path, File folder){
		final List<NodeCppOSM> nodes = path.getNodes();
		NodeCppOSM previousNode = nodes.get(0);
		NodeCppOSM currentNode;
		try (Writer fw = new FileWriter( new File(folder, "overlay.js" ));) {
			fw.write("var segments = [");;
			boolean first = true;
			for(int index = 1; index < nodes.size() ; ++index){
				currentNode = nodes.get(index);
				//TODO use unvisited edges
				final EdgeCppOSM edge = previousNode.getEdgeToNode(currentNode);
				final List<WayNodeOSM> metaNodes = edge.getMetadata().getNodes();
				if (((Long)metaNodes.get(0).getID()).equals(currentNode.getId())){
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
