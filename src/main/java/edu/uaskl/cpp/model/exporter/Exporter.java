package edu.uaskl.cpp.model.exporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import edu.uaskl.cpp.importer.OsmNode;
import edu.uaskl.cpp.model.edge.EdgeCpp;
import edu.uaskl.cpp.model.node.NodeCpp;
import edu.uaskl.cpp.model.path.PathCpp;;

public class Exporter {
	private static String nanoDegreeToString(long nanoDeg){
		return nanoDeg/10000000 +"."+nanoDeg%10000000;
	}
	
	private static String createSegment(List<OsmNode>metaNodes,int id){
		List<String> colors = new LinkedList<String>();
		colors.add("b1563f");
		colors.add("b4693f");
		colors.add("b67b3f");
		colors.add("b98f3f");
		colors.add("bba33f");
		colors.add("beb83f");
		colors.add("b2c040");
		colors.add("a0c141");
		colors.add("8ec242");
		colors.add("7cc344");
		StringBuilder output= new StringBuilder();
		output.append("var line");
		output.append(id);
		output.append(" = L.polyline([[");
		output.append(nanoDegreeToString(metaNodes.get(0).lat));
		output.append(",");
		output.append(nanoDegreeToString(metaNodes.get(0).lon));
		output.append("]");
		for(int i=1;i<metaNodes.size();++i){
			output.append(",[");
			output.append(nanoDegreeToString(metaNodes.get(i).lat));
			output.append(",");
			output.append(nanoDegreeToString(metaNodes.get(i).lon));
			output.append("]");
		}
		output.append("], {weight: 3,opacity: 1,color:'#");
		output.append(colors.get(id%colors.size()));
		output.append("'}).addTo(map);\nline");
		output.append(id);
		output.append(".setText('\u25BA ");
		output.append(id);
		output.append(" ', {repeat: true,offset: 0,attributes: {fill:'black'}});\n");
		return output.toString();
	}
	
	
	public static void exportPathToHTML(PathCpp path, File folder){
		List<NodeCpp> nodes = path.getNodes();
		NodeCpp previousNode = nodes.get(0);
		NodeCpp currentNode;
		Writer fw = null;
		try {
			fw = new FileWriter( folder.toString() + "overlay.js" );
		
			for(int index = 1;index<nodes.size();++index){
				currentNode = nodes.get(index);
				EdgeCpp edge = previousNode.getEdgeToNode(currentNode);
				List<OsmNode> metaNodes = edge.getMetaNodes();
				if (metaNodes.get(0).id.equals(currentNode.getId())){
					Collections.reverse(metaNodes);
				}
				fw.write(createSegment(metaNodes,index));
				fw.append(System.lineSeparator());
				previousNode = currentNode;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			  if ( fw != null )
			    try { fw.close(); } catch ( IOException e ) { e.printStackTrace(); }
			}
	}
}
