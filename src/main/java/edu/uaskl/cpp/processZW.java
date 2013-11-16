package edu.uaskl.cpp;

import static edu.uaskl.cpp.importer.OsmImporter.importKL;
import static edu.uaskl.cpp.model.exporter.Exporter.exportPathToHTML;

import java.io.File;

import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uaskl.cpp.model.path.PathExtended;

public class processZW {

	public static void main(String[] args) throws Exception {
        final GraphUndirected<NodeCppOSM, EdgeCppOSM> graph = importKL();
        graph.getAlgorithms().matchPerfect();
        final PathExtended<NodeCppOSM> path = graph.getAlgorithms().getEulerianCircle();
        graph.resetStates();
        exportPathToHTML(path, new File("./src/test/resources/edu/uaskl/cpp/exporter"));
	}

}
