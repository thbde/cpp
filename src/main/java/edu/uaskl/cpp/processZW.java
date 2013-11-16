package edu.uaskl.cpp;

import static edu.uaskl.cpp.importer.OsmImporter.importZW;
import static edu.uaskl.cpp.model.exporter.Exporter.exportPathToHTML;

import java.io.File;

import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uaskl.cpp.model.path.PathExtended;

public class processZW {

	public static void main(String[] args) throws Exception {
		long timeStart = System.nanoTime();
        final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importZW();
        graph.getAlgorithms().matchPerfect();
        final PathExtended<NodeCppOSM> path = graph.getAlgorithms().getEulerianCircle();
        graph.resetStates();
        exportPathToHTML(path, new File("./output"));
        long timeStop = System.nanoTime();
        System.out.println((timeStop - timeStart)/1000000000 +" seconds");
	}

}
